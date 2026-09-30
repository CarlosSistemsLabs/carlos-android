package com.carloserp.android.presentation.common

/**
 * Generic screen state for data-loading features (task 50.4).
 *
 * Feature ViewModels expose `StateFlow<UiState<T>>` so screens branch on the
 * three states with the shared components (see `UiStateContent`) instead of
 * juggling separate loading/error/data flags.
 */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>

    data class Success<T>(val data: T) : UiState<T>

    data class Error(val message: String) : UiState<Nothing>
}
