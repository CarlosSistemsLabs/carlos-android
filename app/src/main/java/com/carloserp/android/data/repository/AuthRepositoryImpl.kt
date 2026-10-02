package com.carloserp.android.data.repository

import com.carloserp.android.core.auth.SessionStore
import com.carloserp.android.core.di.IoDispatcher
import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.core.network.safeApiCall
import com.carloserp.android.data.remote.api.AuthApi
import com.carloserp.android.data.remote.dto.LoginRequestDto
import com.carloserp.android.data.remote.dto.LogoutRequestDto
import com.carloserp.android.data.remote.mapper.toDomain
import com.carloserp.android.domain.model.AuthSession
import com.carloserp.android.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [AuthRepository] backed by the backend API + encrypted session store (task 50.1).
 *
 * On a successful [login] it persists the token pair through [SessionStore],
 * which both makes the token available to the
 * [com.carloserp.android.data.remote.interceptor.AuthInterceptor] and flips the
 * observed auth state. [logout] revokes the refresh token best-effort and always
 * clears local state so the user is signed out even offline. Network work runs
 * on the injected IO dispatcher.
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val sessionStore: SessionStore,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : AuthRepository {

    override fun observeAuthState(): Flow<Boolean> = sessionStore.isLoggedIn

    override suspend fun login(
        tenantId: String,
        email: String,
        password: String,
    ): ApiResult<AuthSession> = withContext(ioDispatcher) {
        val result = safeApiCall {
            authApi.login(LoginRequestDto(tenantId = tenantId, email = email, password = password))
        }
        when (result) {
            is ApiResult.Success -> {
                val session = result.data.toDomain()
                sessionStore.saveSession(
                    accessToken = session.accessToken,
                    refreshToken = session.refreshToken,
                    tenantId = session.user.tenantId,
                    email = session.user.email,
                )
                ApiResult.Success(session)
            }

            is ApiResult.Failure -> result
        }
    }

    override suspend fun logout() {
        withContext(ioDispatcher) {
            val refreshToken = sessionStore.refreshToken()
            if (refreshToken != null) {
                safeApiCall {
                    authApi.logout(
                        LogoutRequestDto(refreshToken = refreshToken, tenantId = sessionStore.tenantId()),
                    )
                }
            }
            sessionStore.clear()
        }
    }
}
