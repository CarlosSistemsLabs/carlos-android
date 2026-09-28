package com.carloserp.android.core.network

/**
 * Port that supplies the current access token to the network layer (task 49.2).
 *
 * The [AuthInterceptor] reads the token from this provider on every request so
 * the network layer stays decoupled from *how* the token is stored. A temporary
 * in-memory implementation backs it now; the encrypted, persistent
 * implementation lands with the auth flow (task 50.1).
 */
interface TokenProvider {
    /** The current bearer access token, or `null` when the user is not signed in. */
    fun accessToken(): String?
}
