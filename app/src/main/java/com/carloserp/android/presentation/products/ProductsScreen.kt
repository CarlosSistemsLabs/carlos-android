package com.carloserp.android.presentation.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carloserp.android.core.format.formatMoney
import com.carloserp.android.domain.model.Product
import com.carloserp.android.presentation.common.UiState
import com.carloserp.android.presentation.components.EmptyView
import com.carloserp.android.presentation.components.UiStateContent
import com.carloserp.android.presentation.theme.CarlosErpTheme
import com.carloserp.android.presentation.theme.CarlosTheme

/**
 * Products list screen (task 50.4).
 *
 * Renders the offline-first [ProductsViewModel] state through the shared
 * [UiStateContent] (loading/error handled for free, with retry wired to
 * [ProductsViewModel.refresh]). Tapping a row calls [onProductClick] with the
 * product id so the host navigates to the detail screen.
 */
@Composable
fun ProductsScreen(
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    UiStateContent(state = uiState, modifier = modifier, onRetry = viewModel::refresh) { products ->
        if (products.isEmpty()) {
            EmptyView(message = "Todavía no hay productos cargados.")
        } else {
            ProductList(products = products, onProductClick = onProductClick)
        }
    }
}

@Composable
private fun ProductList(
    products: List<Product>,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(items = products, key = { it.id }) { product ->
            ProductRow(product = product, onClick = { onProductClick(product.id) })
            HorizontalDivider()
        }
    }
}

@Composable
private fun ProductRow(product: Product, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = CarlosTheme.spacing.md, vertical = CarlosTheme.spacing.sm),
    ) {
        Text(text = product.name, style = MaterialTheme.typography.bodyLarge)
        Text(
            text = "SKU ${product.sku} · ${formatMoney(product.price, product.currency)}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductsScreenPreview() {
    CarlosErpTheme {
        UiStateContent(
            state = UiState.Success(
                listOf(
                    Product(
                        id = "1", categoryId = "c", sku = "SKU-1", name = "Café molido 500g",
                        description = null, price = "4500.00", cost = null, currency = "ARS",
                        taxRate = 21.0, priceWithTax = "5445.00", unit = "unit", minStock = 5,
                        isActive = true, imageUrl = null,
                    ),
                ),
            ),
            onRetry = {},
        ) { products ->
            ProductList(products = products, onProductClick = {})
        }
    }
}
