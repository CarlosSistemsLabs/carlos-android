package com.carloserp.android.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.carloserp.android.presentation.common.UiState

/**
 * Renders a [UiState] with the shared state views (task 50.4).
 *
 * Maps `Loading`/`Error` to [LoadingView]/[ErrorView] (wiring [onRetry]) and
 * delegates `Success` to [content], so feature screens describe only their happy
 * path. This is the consistent loading/error presentation that task 50.5 extends.
 */
@Composable
fun <T> UiStateContent(
    state: UiState<T>,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
    content: @Composable (T) -> Unit,
) {
    when (state) {
        is UiState.Loading -> LoadingView(modifier = modifier)
        is UiState.Error -> ErrorView(message = state.message, modifier = modifier, onRetry = onRetry)
        is UiState.Success -> content(state.data)
    }
}
