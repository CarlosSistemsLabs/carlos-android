package com.carloserp.android.core.biometric

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Keystore-backed AES-GCM crypto for biometric credential storage (biometric feature).
 *
 * The key lives in the Android Keystore (hardware-backed where available) and is
 * **bound to biometric authentication**: `setUserAuthenticationRequired(true)`
 * means the key can only be used (encrypt/decrypt) inside a successful
 * [androidx.biometric.BiometricPrompt] crypto object, and
 * `setInvalidatedByBiometricEnrollment(true)` destroys it if the device's
 * fingerprints change (so stolen ciphertext can't be unlocked with a newly
 * enrolled finger). Ciphers are handed to the prompt; `doFinal` runs only after
 * the fingerprint is verified.
 */
object BiometricCrypto {
    private const val KEYSTORE = "AndroidKeyStore"
    private const val KEY_ALIAS = "carlos_biometric_credentials_key"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val GCM_TAG_LENGTH_BITS = 128

    /** Cipher for encrypting (enabling). Its IV must be stored for later decryption. */
    fun getEncryptCipher(): Cipher =
        Cipher.getInstance(TRANSFORMATION).apply { init(Cipher.ENCRYPT_MODE, getOrCreateKey()) }

    /**
     * Cipher for decrypting with the stored [iv]. Throws
     * [KeyPermanentlyInvalidatedException] when the biometric enrollment changed
     * (the caller then clears the stored credentials and asks to re-enable).
     */
    @Throws(KeyPermanentlyInvalidatedException::class)
    fun getDecryptCipher(iv: ByteArray): Cipher =
        Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, requireKey(), GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv))
        }

    /** Removes the key (e.g. when disabling biometric login or after invalidation). */
    fun deleteKey() {
        val keyStore = KeyStore.getInstance(KEYSTORE).apply { load(null) }
        if (keyStore.containsAlias(KEY_ALIAS)) keyStore.deleteEntry(KEY_ALIAS)
    }

    private fun requireKey(): SecretKey {
        val keyStore = KeyStore.getInstance(KEYSTORE).apply { load(null) }
        val entry = keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
            ?: error("Biometric key missing")
        return entry.secretKey
    }

    private fun getOrCreateKey(): SecretKey {
        val keyStore = KeyStore.getInstance(KEYSTORE).apply { load(null) }
        (keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry)?.let { return it.secretKey }

        val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE)
        val spec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(KEY_SIZE_BITS)
            .setUserAuthenticationRequired(true)
            .setInvalidatedByBiometricEnrollment(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    setUserAuthenticationParameters(0, KeyProperties.AUTH_BIOMETRIC_STRONG)
                } else {
                    @Suppress("DEPRECATION")
                    setUserAuthenticationValidityDurationSeconds(-1)
                }
            }
            .build()
        generator.init(spec)
        return generator.generateKey()
    }

    private const val KEY_SIZE_BITS = 256
}
