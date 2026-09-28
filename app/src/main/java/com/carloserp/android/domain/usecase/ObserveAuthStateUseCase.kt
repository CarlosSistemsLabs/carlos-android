package com.carloserp.android.domain.usecase

import com.carloserp.android.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Observes whether a session is active (task 50.1).
 *
 * The host ViewModel collects this to switch between the login and home
 * screens; it re-emits whenever the session is saved or cleared.
 */
class ObserveAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Flow<Boolean> = authRepository.observeAuthState()
}
