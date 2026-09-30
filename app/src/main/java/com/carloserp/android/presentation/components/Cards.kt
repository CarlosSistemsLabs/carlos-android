package com.carloserp.android.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carloserp.android.presentation.theme.CarlosErpTheme
import com.carloserp.android.presentation.theme.CarlosTheme

/**
 * Reusable card containers (task 50.2).
 *
 * [SectionCard] is a padded surface for grouping arbitrary content; [MetricCard]
 * is the labelled value tile used on the dashboard (task 50.4). Both take their
 * corner radius from `MaterialTheme.shapes` and padding from the spacing tokens.
 */
@Composable
fun SectionCard(
    modifier: Modifier = Modifier,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit,
) {
    Card(modifier = modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium) {
        Column(
            modifier = Modifier.padding(CarlosTheme.spacing.md),
            content = content,
        )
    }
}

@Composable
fun MetricCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier, shape = MaterialTheme.shapes.medium) {
        Column(
            modifier = Modifier.padding(CarlosTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(CarlosTheme.spacing.xs),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardsPreview() {
    CarlosErpTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            MetricCard(label = "Ventas del día", value = "$ 128.450")
            Spacer(Modifier.height(12.dp))
            SectionCard {
                Text("Resumen", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
