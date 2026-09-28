package com.carloserp.android.domain.usecase

import com.carloserp.android.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Signs the current user out (task 50.1): delegates to [AuthRepository.logout],
 * which revokes the refresh token best-effort and always clears local state.
 */
class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke() = authRepository.logout()
}
