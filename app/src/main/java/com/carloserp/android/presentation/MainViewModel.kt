package com.carloserp.android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carloserp.android.core.analytics.AnalyticsEvents
import com.carloserp.android.core.analytics.AnalyticsService
import com.carloserp.android.core.crash.CrashReporter
import com.carloserp.android.core.network.NetworkMonitor
import com.carloserp.android.domain.usecase.LogoutUseCase
import com.carloserp.android.domain.usecase.ObserveAuthStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Immutable UI state for the host screen (tasks 49.3 / 50.1 / 50.5).
 *
 * [isResolvingSession] is `true` until the first auth-state emission arrives, so
 * the host can show a splash instead of briefly flashing the login screen.
 * [isOffline] drives the connectivity banner.
 */
data class MainUiState(
    val isLoggedIn: Boolean = false,
    val isResolvingSession: Boolean = true,
    val isOffline: Boolean = false,
)

/**
 * Host-screen ViewModel (tasks 49.3 / 50.1 / 50.5).
 *
 * Combines the app-level auth state ([ObserveAuthStateUseCase]) with device
 * connectivity ([NetworkMonitor]) into [uiState], so [MainActivity] can pick the
 * screen and show the offline banner. Also offers [logout].
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    observeAuthState: ObserveAuthStateUseCase,
    networkMonitor: NetworkMonitor,
    private val logoutUseCase: LogoutUseCase,
    private val analytics: AnalyticsService,
    private val crashReporter: CrashReporter,
) : ViewModel() {

    val uiState: StateFlow<MainUiState> =
        combine(observeAuthState(), networkMonitor.isOnline) { loggedIn, online ->
            MainUiState(
                isLoggedIn = loggedIn,
                isResolvingSession = false,
                isOffline = !online,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = MainUiState(),
        )

    fun logout() {
        viewModelScope.launch {
            analytics.logEvent(AnalyticsEvents.LOGOUT)
            logoutUseCase()
            analytics.setUser(null, null)
            crashReporter.setUser(null, null)
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
