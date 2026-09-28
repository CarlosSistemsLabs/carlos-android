package com.carloserp.android.core.auth

import com.carloserp.android.core.network.TokenProvider
import kotlinx.coroutines.flow.StateFlow

/**
 * Persistent store for the authenticated session (task 50.1).
 *
 * Extends [TokenProvider] so the network layer keeps reading the access token
 * through the narrow port it already depends on, while the auth flow uses this
 * wider surface to persist/clear the full token pair + tenant and observe
 * whether a session exists. Backed by the encrypted, disk-backed
 * [com.carloserp.android.data.remote.token.EncryptedTokenStore].
 */
interface SessionStore : TokenProvider {

    /** The stored refresh token, or `null` when signed out. */
    fun refreshToken(): String?

    /** The tenant id bound to the current session, or `null` when signed out. */
    fun tenantId(): String?

    /**
     * Hot stream that is `true` while a session is persisted. Emits the current
     * value on subscription so the UI can decide its start destination.
     */
    val isLoggedIn: StateFlow<Boolean>

    /** Persists a full session, flipping [isLoggedIn] to `true`. */
    fun saveSession(accessToken: String, refreshToken: String, tenantId: String)

    /** Erases the persisted session, flipping [isLoggedIn] to `false`. */
    fun clear()
}
