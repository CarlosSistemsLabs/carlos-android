package com.carloserp.android.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carloserp.android.R
import com.carloserp.android.core.analytics.AnalyticsEvents
import com.carloserp.android.core.analytics.AnalyticsService
import com.carloserp.android.core.crash.CrashReporter
import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.core.network.isUnauthorized
import com.carloserp.android.core.ui.UiText
import com.carloserp.android.data.biometric.BiometricCredentialStore
import com.carloserp.android.domain.usecase.LoginUseCase
import com.carloserp.android.presentation.common.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Immutable UI state for the login screen (task 50.1).
 *
 * [canSubmit] gates the button so it is disabled while a request is in flight or
 * a required field is blank.
 */
data class LoginUiState(
    val workspaceId: String = "",
    val email: String = "",
    val password: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: UiText? = null,
    val biometricEnabled: Boolean = false,
) {
    val canSubmit: Boolean
        get() = !isSubmitting &&
            workspaceId.isNotBlank() &&
            email.isNotBlank() &&
            password.isNotBlank()
}

/**
 * Drives the login screen (task 50.1).
 *
 * Holds the form state, validates it, and calls [LoginUseCase]. On success the
 * session is persisted by the repository, which flips the app-level auth state
 * observed by [com.carloserp.android.presentation.MainViewModel]; this ViewModel
 * just clears its submitting flag. Failures are mapped to a user-facing message.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val analytics: AnalyticsService,
    private val crashReporter: CrashReporter,
    biometricCredentialStore: BiometricCredentialStore,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        LoginUiState(
            email = biometricCredentialStore.email().orEmpty(),
            biometricEnabled = biometricCredentialStore.isEnabled(),
        ),
    )
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onWorkspaceChange(value: String) =
        _uiState.update { it.copy(workspaceId = value, errorMessage = null) }

    fun onEmailChange(value: String) =
        _uiState.update { it.copy(email = value, errorMessage = null) }

    fun onPasswordChange(value: String) =
        _uiState.update { it.copy(password = value, errorMessage = null) }

    fun onSubmit() {
        val state = _uiState.value
        if (!state.canSubmit) return
        performLogin(state.workspaceId.trim(), state.email.trim(), state.password)
    }

    /** Signs in with credentials recovered from the biometric store. */
    fun loginWithCredentials(tenantId: String, email: String, password: String) {
        if (_uiState.value.isSubmitting) return
        performLogin(tenantId.trim(), email.trim(), password)
    }

    /** Surfaces an error raised by the biometric flow (e.g. invalidated key). */
    fun onError(message: UiText) {
        _uiState.update { it.copy(errorMessage = message) }
    }

    /** Reflects that biometric sign-in is no longer available (store was cleared). */
    fun onBiometricDisabled() {
        _uiState.update { it.copy(biometricEnabled = false) }
    }

    private fun performLogin(tenantId: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            when (val result = loginUseCase(tenantId = tenantId, email = email, password = password)) {
                is ApiResult.Success -> {
                    val user = result.data.user
                    analytics.setUser(user.id, user.tenantId)
                    analytics.logEvent(AnalyticsEvents.LOGIN)
                    crashReporter.setUser(user.id, user.tenantId)
                    _uiState.update { it.copy(isSubmitting = false) }
                }

                is ApiResult.Failure -> {
                    // On the login screen a 401 means bad credentials, not an expired session.
                    val message = if (result.error.isUnauthorized) {
                        UiText.res(R.string.login_error_invalid_credentials)
                    } else {
                        result.error.toUserMessage()
                    }
                    _uiState.update { it.copy(isSubmitting = false, errorMessage = message) }
                }
            }
        }
    }
}
