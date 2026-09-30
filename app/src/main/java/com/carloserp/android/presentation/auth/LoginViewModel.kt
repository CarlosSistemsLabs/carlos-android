package com.carloserp.android.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.core.network.isUnauthorized
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
    val errorMessage: String? = null,
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
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
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

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            val result = loginUseCase(
                tenantId = state.workspaceId.trim(),
                email = state.email.trim(),
                password = state.password,
            )
            when (result) {
                is ApiResult.Success ->
                    _uiState.update { it.copy(isSubmitting = false) }

                is ApiResult.Failure -> {
                    // On the login screen a 401 means bad credentials, not an expired session.
                    val message = if (result.error.isUnauthorized) {
                        "Credenciales inválidas. Verificá el workspace, email y contraseña."
                    } else {
                        result.error.toUserMessage()
                    }
                    _uiState.update { it.copy(isSubmitting = false, errorMessage = message) }
                }
            }
        }
    }
}
