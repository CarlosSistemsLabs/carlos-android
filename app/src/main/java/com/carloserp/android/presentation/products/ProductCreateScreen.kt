package com.carloserp.android.presentation.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carloserp.android.R
import com.carloserp.android.core.ui.asString
import com.carloserp.android.presentation.components.CarlosTextField
import com.carloserp.android.presentation.components.PrimaryButton
import com.carloserp.android.presentation.theme.CarlosTheme

/**
 * New-product form (create products from the app). Header owned by the shell;
 * on success calls [onCreated]. The category can be picked from the existing
 * ones or created inline.
 */
@Composable
fun ProductCreateScreen(
    onCreated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductCreateViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.created) {
        LaunchedEffect(Unit) { onCreated() }
    }

    val spacing = CarlosTheme.spacing
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.md),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        CategorySelector(
            categories = state.categories.map { it.id to it.name },
            selectedCategoryId = state.selectedCategoryId,
            creatingNew = state.creatingNewCategory,
            newCategoryName = state.newCategoryName,
            enabled = !state.submitting,
            onSelect = viewModel::onSelectCategory,
            onNewToggle = viewModel::onNewCategoryToggle,
            onNewNameChange = viewModel::onNewCategoryNameChange,
        )

        CarlosTextField(
            value = state.sku,
            onValueChange = viewModel::onSkuChange,
            label = "SKU",
            enabled = !state.submitting,
        )
        CarlosTextField(
            value = state.name,
            onValueChange = viewModel::onNameChange,
            label = stringResource(R.string.field_name),
            enabled = !state.submitting,
        )
        CarlosTextField(
            value = state.price,
            onValueChange = viewModel::onPriceChange,
            label = stringResource(R.string.product_price),
            enabled = !state.submitting,
            keyboardType = KeyboardType.Decimal,
        )
        CarlosTextField(
            value = state.cost,
            onValueChange = viewModel::onCostChange,
            label = stringResource(R.string.product_cost),
            enabled = !state.submitting,
            keyboardType = KeyboardType.Decimal,
        )
        CarlosTextField(
            value = state.taxRate,
            onValueChange = viewModel::onTaxRateChange,
            label = stringResource(R.string.product_tax),
            enabled = !state.submitting,
            keyboardType = KeyboardType.Decimal,
        )
        CarlosTextField(
            value = state.minStock,
            onValueChange = viewModel::onMinStockChange,
            label = stringResource(R.string.product_min_stock),
            enabled = !state.submitting,
            keyboardType = KeyboardType.Number,
        )

        state.error?.let { error ->
            Text(
                text = error.asString(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        PrimaryButton(
            text = stringResource(R.string.action_save),
            onClick = viewModel::submit,
            enabled = state.canSubmit,
            loading = state.submitting,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategorySelector(
    categories: List<Pair<String, String>>,
    selectedCategoryId: String?,
    creatingNew: Boolean,
    newCategoryName: String,
    enabled: Boolean,
    onSelect: (String) -> Unit,
    onNewToggle: () -> Unit,
    onNewNameChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedName = categories.firstOrNull { it.first == selectedCategoryId }?.second ?: ""
    val fieldValue = when {
        creatingNew -> stringResource(R.string.product_category_new)
        else -> selectedName
    }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { if (enabled) expanded = it }) {
        OutlinedTextField(
            value = fieldValue,
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            label = { Text(stringResource(R.string.product_category)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            categories.forEach { (id, name) ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = {
                        onSelect(id)
                        expanded = false
                    },
                )
            }
            DropdownMenuItem(
                text = { Text(stringResource(R.string.product_category_new)) },
                onClick = {
                    onNewToggle()
                    expanded = false
                },
            )
        }
    }

    if (creatingNew) {
        CarlosTextField(
            value = newCategoryName,
            onValueChange = onNewNameChange,
            label = stringResource(R.string.product_category_new_hint),
            enabled = enabled,
        )
    }
}
