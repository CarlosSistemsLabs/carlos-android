package com.carloserp.android.data.biometric

import android.content.Context
import android.util.Base64
import com.carloserp.android.core.biometric.BiometricCrypto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.crypto.Cipher
import javax.inject.Inject
import javax.inject.Singleton

/** Credentials stored (encrypted) for biometric sign-in. */
@Serializable
data class BiometricCredentials(
    val tenantId: String,
    val email: String,
    val password: String,
)

/**
 * Stores the biometric-encrypted sign-in credentials (biometric feature).
 *
 * The ciphertext + IV live in a plain prefs file, but the bytes are only
 * decryptable with [BiometricCrypto]'s biometric-bound Keystore key — i.e. after
 * a successful fingerprint. [persist]/[decrypt] receive the authenticated
 * [Cipher] from the BiometricPrompt. [email] is kept in clear only as a UI hint
 * for the login screen.
 */
@Singleton
class BiometricCredentialStore @Inject constructor(
    @ApplicationContext context: Context,
    private val json: Json,
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /** Whether biometric sign-in has been enabled on this device. */
    fun isEnabled(): Boolean = prefs.contains(KEY_CIPHERTEXT)

    /** Email hint to show on the login screen. */
    fun email(): String? = prefs.getString(KEY_EMAIL, null)

    /** The stored GCM IV needed to build the decrypt cipher, or `null` if none. */
    fun iv(): ByteArray? =
        prefs.getString(KEY_IV, null)?.let { Base64.decode(it, Base64.NO_WRAP) }

    /** Encrypts and stores [credentials] with the authenticated encrypt [cipher]. */
    fun persist(cipher: Cipher, credentials: BiometricCredentials) {
        val plaintext = json.encodeToString(BiometricCredentials.serializer(), credentials)
            .toByteArray(Charsets.UTF_8)
        val ciphertext = cipher.doFinal(plaintext)
        prefs.edit()
            .putString(KEY_CIPHERTEXT, Base64.encodeToString(ciphertext, Base64.NO_WRAP))
            .putString(KEY_IV, Base64.encodeToString(cipher.iv, Base64.NO_WRAP))
            .putString(KEY_EMAIL, credentials.email)
            .apply()
    }

    /** Decrypts the stored credentials with the authenticated decrypt [cipher]. */
    fun decrypt(cipher: Cipher): BiometricCredentials {
        val stored = prefs.getString(KEY_CIPHERTEXT, null) ?: error("No biometric credentials")
        val plaintext = cipher.doFinal(Base64.decode(stored, Base64.NO_WRAP))
        return json.decodeFromString(
            BiometricCredentials.serializer(),
            String(plaintext, Charsets.UTF_8),
        )
    }

    /** Clears the stored credentials and destroys the Keystore key. */
    fun clear() {
        prefs.edit().clear().apply()
        BiometricCrypto.deleteKey()
    }

    private companion object {
        const val PREFS_NAME = "carlos_biometric"
        const val KEY_CIPHERTEXT = "credentials_ciphertext"
        const val KEY_IV = "credentials_iv"
        const val KEY_EMAIL = "email_hint"
    }
}
