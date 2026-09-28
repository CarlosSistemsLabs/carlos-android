package com.carloserp.android.domain.repository

import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.domain.model.AuthSession
import kotlinx.coroutines.flow.Flow

/**
 * Auth repository port (task 50.1).
 *
 * Owns authentication as a domain concern: it calls the backend, persists the
 * session in the encrypted store on success, and exposes whether a session is
 * active. The presentation layer depends on this abstraction (via use cases),
 * never on Retrofit or the token store directly.
 */
interface AuthRepository {

    /**
     * Streams `true` while a session is persisted. Emits immediately on
     * collection so the host can pick between the login and home screens.
     */
    fun observeAuthState(): Flow<Boolean>

    /**
     * Authenticates against the backend and, on success, persists the token
     * pair. Returns the [AuthSession] or a typed [ApiResult.Failure].
     */
    suspend fun login(tenantId: String, email: String, password: String): ApiResult<AuthSession>

    /**
     * Revokes the refresh token server-side (best-effort) and clears the local
     * session so the user is signed out even if the network call fails.
     */
    suspend fun logout()
}
