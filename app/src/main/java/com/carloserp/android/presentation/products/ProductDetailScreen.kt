package com.carloserp.android.presentation.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carloserp.android.R
import com.carloserp.android.core.analytics.AnalyticsScreens
import com.carloserp.android.core.format.formatMoney
import com.carloserp.android.domain.model.Product
import com.carloserp.android.presentation.analytics.TrackScreenView
import com.carloserp.android.presentation.components.SectionCard
import com.carloserp.android.presentation.components.UiStateContent
import com.carloserp.android.presentation.theme.CarlosTheme

/**
 * Product detail screen (task 50.4).
 *
 * A full screen with its own top bar (back arrow) since it lives outside the
 * bottom-nav shell. Loads via [ProductDetailViewModel] and renders through the
 * shared [UiStateContent] (loading/error/retry handled).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductDetailViewModel = hiltViewModel(),
) {
    TrackScreenView(AnalyticsScreens.PRODUCT_DETAIL)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.product_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        UiStateContent(
            state = uiState,
            modifier = Modifier.padding(innerPadding),
            onRetry = viewModel::load,
        ) { product ->
            ProductDetailContent(product = product)
        }
    }
}

@Composable
private fun ProductDetailContent(product: Product, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(CarlosTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(CarlosTheme.spacing.md),
    ) {
        Text(text = product.name, style = MaterialTheme.typography.headlineSmall)
        Text(
            text = stringResource(R.string.product_sku, product.sku),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        SectionCard {
            DetailRow(stringResource(R.string.product_price), formatMoney(product.price, product.currency))
            DetailRow(
                stringResource(R.string.product_price_with_tax),
                formatMoney(product.priceWithTax, product.currency),
            )
            product.cost?.let {
                DetailRow(stringResource(R.string.product_cost), formatMoney(it, product.currency))
            }
            DetailRow(
                stringResource(R.string.product_tax),
                stringResource(R.string.product_tax_value, product.taxRate.toString()),
            )
            DetailRow(stringResource(R.string.product_unit), product.unit)
            DetailRow(stringResource(R.string.product_min_stock), product.minStock.toString())
            DetailRow(
                stringResource(R.string.product_status),
                stringResource(if (product.isActive) R.string.status_active else R.string.status_inactive),
            )
        }

        product.description?.takeIf { it.isNotBlank() }?.let { description ->
            SectionCard {
                Text(stringResource(R.string.product_description), style = MaterialTheme.typography.titleSmall)
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = CarlosTheme.spacing.xs),
                )
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = CarlosTheme.spacing.xs)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}
