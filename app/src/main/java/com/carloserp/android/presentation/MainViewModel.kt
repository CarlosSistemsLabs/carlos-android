package com.carloserp.android.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/** Immutable UI state for the host screen. */
data class MainUiState(
    val title: String = "Carlos ERP",
    val subtitle: String = "Android client",
)

/**
 * Host-screen ViewModel (task 49.3).
 *
 * Annotated with [HiltViewModel] so it is provided by Hilt and obtained in
 * Compose via `hiltViewModel()` — proving the end-to-end DI chain
 * (`@HiltAndroidApp` → `@AndroidEntryPoint` activity → `@HiltViewModel`). It
 * exposes UI state as an immutable [StateFlow]; feature ViewModels will inject
 * their use cases here following the same pattern.
 */
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
}
