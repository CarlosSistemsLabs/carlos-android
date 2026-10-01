package com.carloserp.android.presentation.sales

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carloserp.android.core.format.formatMoney
import com.carloserp.android.core.analytics.AnalyticsScreens
import com.carloserp.android.domain.model.Sale
import com.carloserp.android.presentation.analytics.TrackScreenView
import com.carloserp.android.presentation.components.EmptyView
import com.carloserp.android.presentation.components.RefreshableUiState
import com.carloserp.android.presentation.theme.CarlosTheme

/**
 * Sales list screen (task 50.4). Shows recent sales through the shared
 * [UiStateContent] with a FAB that calls [onCreateClick] to open the new-sale
 * form. Each row shows the sale number, date, total and status.
 */
@Composable
fun SalesScreen(
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SalesViewModel = hiltViewModel(),
) {
    TrackScreenView(AnalyticsScreens.SALES)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    Box(modifier = modifier.fillMaxSize()) {
        RefreshableUiState(state = uiState, isRefreshing = isRefreshing, onRefresh = viewModel::refresh) { sales ->
            if (sales.isEmpty()) {
                EmptyView(message = "Todavía no registraste ventas.")
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(items = sales, key = { it.id }) { sale ->
                        SaleRow(sale = sale)
                        HorizontalDivider()
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = onCreateClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(CarlosTheme.spacing.md),
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Nueva venta")
        }
    }
}

@Composable
private fun SaleRow(sale: Sale, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = CarlosTheme.spacing.md, vertical = CarlosTheme.spacing.sm),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = sale.saleNumber, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "${sale.saleDate.substringBefore('T')} · ${statusLabel(sale.status)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = formatMoney(sale.total, sale.currency),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.End,
        )
    }
}

private fun statusLabel(status: String): String = when (status) {
    "draft" -> "Borrador"
    "completed" -> "Completada"
    "cancelled" -> "Cancelada"
    else -> status
}
