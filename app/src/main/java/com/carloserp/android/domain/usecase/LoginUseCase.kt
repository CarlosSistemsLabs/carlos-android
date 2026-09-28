package com.carloserp.android.domain.usecase

import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.domain.model.AuthSession
import com.carloserp.android.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Signs a user in (task 50.1).
 *
 * A thin, injectable operation the [com.carloserp.android.presentation.auth.LoginViewModel]
 * invokes; it delegates to [AuthRepository.login] and exists so the ViewModel
 * depends on a single-purpose domain type rather than the whole repository.
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        tenantId: String,
        email: String,
        password: String,
    ): ApiResult<AuthSession> = authRepository.login(tenantId, email, password)
}
