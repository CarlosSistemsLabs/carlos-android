package com.carloserp.android.presentation.stock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carloserp.android.domain.model.StockLevel
import com.carloserp.android.presentation.components.EmptyView
import com.carloserp.android.presentation.components.UiStateContent
import com.carloserp.android.presentation.theme.CarlosTheme

/**
 * Stock screen (task 50.4). Lists on-hand quantities via the shared
 * [UiStateContent]; rows at or below their minimum show a "Bajo stock" chip in
 * the error color so low levels stand out.
 */
@Composable
fun StockScreen(
    modifier: Modifier = Modifier,
    viewModel: StockViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    UiStateContent(state = uiState, modifier = modifier, onRetry = viewModel::load) { levels ->
        if (levels.isEmpty()) {
            EmptyView(message = "No hay información de stock todavía.")
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items = levels, key = { it.productId + (it.branchId ?: "") }) { level ->
                    StockRow(level = level)
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun StockRow(level: StockLevel, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = CarlosTheme.spacing.md, vertical = CarlosTheme.spacing.sm),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = level.productName, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "Mínimo ${level.minStock}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (level.lowStock) {
            AssistChip(
                onClick = {},
                enabled = false,
                label = { Text("Bajo · ${level.quantity}") },
                colors = AssistChipDefaults.assistChipColors(
                    disabledLabelColor = MaterialTheme.colorScheme.error,
                ),
            )
        } else {
            Text(text = level.quantity.toString(), style = MaterialTheme.typography.titleMedium)
        }
    }
}
