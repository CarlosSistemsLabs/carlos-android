package com.carloserp.android.presentation.common

import com.carloserp.android.core.ui.UiText

/**
 * Generic screen state for data-loading features (tasks 50.4 / 52.1).
 *
 * Feature ViewModels expose `StateFlow<UiState<T>>` so screens branch on the
 * three states with the shared components (see `UiStateContent`). The error
 * carries a localizable [UiText] rather than a resolved string.
 */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>

    data class Success<T>(val data: T) : UiState<T>

    data class Error(val message: UiText) : UiState<Nothing>
}
