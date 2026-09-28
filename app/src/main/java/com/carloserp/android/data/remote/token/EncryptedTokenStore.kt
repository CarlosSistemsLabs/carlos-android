package com.carloserp.android.data.remote.token

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.carloserp.android.core.auth.SessionStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Encrypted, disk-backed [SessionStore] (task 50.1).
 *
 * Persists the JWT pair + tenant id in an [EncryptedSharedPreferences] file
 * (AES-256, key held in the Android Keystore), so tokens survive process death
 * but never touch plaintext storage. Replaces the temporary in-memory provider
 * from task 49.2 — the Hilt binding is repointed here with no change to the
 * network layer, which still reads only [accessToken] through [SessionStore]'s
 * [com.carloserp.android.core.network.TokenProvider] super-interface.
 */
@Singleton
class EncryptedTokenStore @Inject constructor(
    @ApplicationContext context: Context,
) : SessionStore {

    private val prefs: SharedPreferences = run {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    private val _isLoggedIn = MutableStateFlow(prefs.getString(KEY_ACCESS, null) != null)
    override val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    override fun accessToken(): String? = prefs.getString(KEY_ACCESS, null)

    override fun refreshToken(): String? = prefs.getString(KEY_REFRESH, null)

    override fun tenantId(): String? = prefs.getString(KEY_TENANT, null)

    override fun saveSession(accessToken: String, refreshToken: String, tenantId: String) {
        prefs.edit()
            .putString(KEY_ACCESS, accessToken)
            .putString(KEY_REFRESH, refreshToken)
            .putString(KEY_TENANT, tenantId)
            .apply()
        _isLoggedIn.value = true
    }

    override fun clear() {
        prefs.edit().clear().apply()
        _isLoggedIn.value = false
    }

    private companion object {
        const val PREFS_NAME = "carlos_secure_session"
        const val KEY_ACCESS = "access_token"
        const val KEY_REFRESH = "refresh_token"
        const val KEY_TENANT = "tenant_id"
    }
}
