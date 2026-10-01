package com.carloserp.android.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carloserp.android.R
import com.carloserp.android.core.analytics.AnalyticsScreens
import com.carloserp.android.core.format.formatAmount
import com.carloserp.android.domain.model.DashboardMetrics
import com.carloserp.android.presentation.analytics.TrackScreenView
import com.carloserp.android.presentation.common.UiState
import com.carloserp.android.presentation.components.MetricCard
import com.carloserp.android.presentation.components.RefreshableUiState
import com.carloserp.android.presentation.components.UiStateContent
import com.carloserp.android.presentation.theme.CarlosErpTheme
import com.carloserp.android.presentation.theme.CarlosTheme

/**
 * Dashboard screen (task 50.4). Shows the aggregated metrics as [MetricCard]s
 * through the shared [UiStateContent] (loading/error/retry handled).
 */
@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    TrackScreenView(AnalyticsScreens.DASHBOARD)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    RefreshableUiState(
        state = uiState,
        isRefreshing = isRefreshing,
        onRefresh = viewModel::refresh,
        modifier = modifier,
    ) { metrics ->
        DashboardContent(metrics = metrics)
    }
}

@Composable
private fun DashboardContent(metrics: DashboardMetrics, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(CarlosTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(CarlosTheme.spacing.md),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(CarlosTheme.spacing.md)) {
            MetricCard(
                label = stringResource(R.string.dashboard_sales),
                value = metrics.salesCount.toString(),
                modifier = Modifier.weight(1f),
            )
            MetricCard(
                label = stringResource(R.string.dashboard_revenue),
                value = formatAmount(metrics.salesTotal),
                modifier = Modifier.weight(1f),
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(CarlosTheme.spacing.md)) {
            MetricCard(
                label = stringResource(R.string.dashboard_products),
                value = metrics.totalProducts.toString(),
                modifier = Modifier.weight(1f),
            )
            MetricCard(
                label = stringResource(R.string.dashboard_low_stock),
                value = metrics.lowStockCount.toString(),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    CarlosErpTheme {
        UiStateContent(
            state = UiState.Success(
                DashboardMetrics(
                    salesCount = 42,
                    salesTotal = "128450.00",
                    totalProducts = 87,
                    lowStockCount = 5,
                ),
            ),
        ) { metrics ->
            DashboardContent(metrics = metrics, modifier = Modifier.fillMaxWidth())
        }
    }
}
