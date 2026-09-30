package com.carloserp.android.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.carloserp.android.presentation.common.UiState

/**
 * [UiStateContent] wrapped in a pull-to-refresh container (task 50.5).
 *
 * Gives every list/detail screen the same swipe-to-refresh gesture plus the
 * shared loading/error/retry handling. [onRefresh] backs both the pull gesture
 * and the error-view retry, so there's a single refresh entry point.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> RefreshableUiState(
    state: UiState<T>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
    ) {
        UiStateContent(state = state, onRetry = onRefresh, content = content)
    }
}
