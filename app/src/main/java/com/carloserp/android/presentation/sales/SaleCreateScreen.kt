package com.carloserp.android.presentation.sales

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carloserp.android.core.format.formatMoney
import com.carloserp.android.domain.model.Customer
import com.carloserp.android.domain.model.Product
import com.carloserp.android.presentation.components.PrimaryButton
import com.carloserp.android.presentation.components.UiStateContent
import com.carloserp.android.presentation.common.UiState
import com.carloserp.android.presentation.theme.CarlosTheme

/**
 * New-sale form (task 50.4).
 *
 * Full screen with its own back-arrow top bar. Lets the user pick a customer and
 * set per-product quantities, then confirm. On success ([SaleCreateUiState.created])
 * it invokes [onCreated] so the host pops back to the refreshed list.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleCreateScreen(
    onBack: () -> Unit,
    onCreated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SaleCreateViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.created) {
        // Fire the navigation callback once the sale is persisted.
        androidx.compose.runtime.LaunchedEffect(Unit) { onCreated() }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Nueva venta") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
            )
        },
    ) { innerPadding ->
        val listState = when {
            uiState.loading -> UiState.Loading
            uiState.loadError != null -> UiState.Error(uiState.loadError!!)
            else -> UiState.Success(Unit)
        }
        UiStateContent(
            state = listState,
            modifier = Modifier.padding(innerPadding),
            onRetry = viewModel::loadOptions,
        ) {
            SaleForm(
                state = uiState,
                onSelectCustomer = viewModel::onSelectCustomer,
                onQuantityChange = viewModel::onQuantityChange,
                onSubmit = viewModel::submit,
            )
        }
    }
}

@Composable
private fun SaleForm(
    state: SaleCreateUiState,
    onSelectCustomer: (String) -> Unit,
    onQuantityChange: (String, Int) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().padding(CarlosTheme.spacing.md)) {
        CustomerSelector(
            customers = state.customers,
            selectedCustomerId = state.selectedCustomerId,
            onSelectCustomer = onSelectCustomer,
        )

        Text(
            text = "Productos",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(vertical = CarlosTheme.spacing.sm),
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(items = state.products, key = { it.id }) { product ->
                ProductQuantityRow(
                    product = product,
                    quantity = state.quantities[product.id] ?: 0,
                    onQuantityChange = { onQuantityChange(product.id, it) },
                )
                HorizontalDivider()
            }
        }

        if (state.submitError != null) {
            Text(
                text = state.submitError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = CarlosTheme.spacing.sm),
            )
        }

        PrimaryButton(
            text = "Confirmar venta",
            onClick = onSubmit,
            enabled = state.canSubmit,
            loading = state.submitting,
            modifier = Modifier.padding(top = CarlosTheme.spacing.sm),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomerSelector(
    customers: List<Customer>,
    selectedCustomerId: String?,
    onSelectCustomer: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedName = customers.firstOrNull { it.id == selectedCustomerId }?.name ?: ""

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = selectedName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Cliente") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            customers.forEach { customer ->
                DropdownMenuItem(
                    text = { Text(customer.name) },
                    onClick = {
                        onSelectCustomer(customer.id)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun ProductQuantityRow(
    product: Product,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = CarlosTheme.spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(CarlosTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = product.name, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = formatMoney(product.price, product.currency),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        OutlinedTextField(
            value = if (quantity > 0) quantity.toString() else "",
            onValueChange = { onQuantityChange(it.toIntOrNull() ?: 0) },
            label = { Text("Cant.") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.width(96.dp),
        )
    }
}
