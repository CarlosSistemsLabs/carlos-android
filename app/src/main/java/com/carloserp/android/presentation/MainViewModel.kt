package com.carloserp.android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carloserp.android.domain.usecase.LogoutUseCase
import com.carloserp.android.domain.usecase.ObserveAuthStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Immutable UI state for the host screen (tasks 49.3 / 50.1).
 *
 * [isResolvingSession] is `true` until the first auth-state emission arrives, so
 * the host can show a splash instead of briefly flashing the login screen.
 */
data class MainUiState(
    val title: String = "Carlos ERP",
    val subtitle: String = "Android client",
    val isLoggedIn: Boolean = false,
    val isResolvingSession: Boolean = true,
)

/**
 * Host-screen ViewModel (tasks 49.3 / 50.1).
 *
 * Observes the app-level auth state via [ObserveAuthStateUseCase] and exposes it
 * as [uiState] so [MainActivity] picks between the login and home screens, and
 * offers [logout]. Login itself is handled by the login feature; when it
 * persists a session this flow re-emits and the host switches automatically.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    observeAuthState: ObserveAuthStateUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    val uiState: StateFlow<MainUiState> =
        observeAuthState()
            .map { loggedIn -> MainUiState(isLoggedIn = loggedIn, isResolvingSession = false) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                initialValue = MainUiState(),
            )

    fun logout() {
        viewModelScope.launch { logoutUseCase() }
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
