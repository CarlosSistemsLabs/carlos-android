package com.carloserp.android.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.core.ui.UiText
import com.carloserp.android.domain.model.Category
import com.carloserp.android.domain.model.NewProduct
import com.carloserp.android.domain.repository.ProductRepository
import com.carloserp.android.presentation.common.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Form state for creating a product.
 *
 * The category can be an existing one ([selectedCategoryId]) or a new one typed
 * in ([newCategoryName]); [canSubmit] requires SKU, name, a valid price and a
 * category choice.
 */
data class ProductCreateUiState(
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: String? = null,
    val creatingNewCategory: Boolean = false,
    val newCategoryName: String = "",
    val sku: String = "",
    val name: String = "",
    val price: String = "",
    val cost: String = "",
    val taxRate: String = "21",
    val minStock: String = "0",
    val submitting: Boolean = false,
    val error: UiText? = null,
    val created: Boolean = false,
) {
    val hasCategory: Boolean
        get() = if (creatingNewCategory) newCategoryName.isNotBlank() else selectedCategoryId != null

    val canSubmit: Boolean
        get() = !submitting &&
            sku.isNotBlank() &&
            name.isNotBlank() &&
            price.toDoubleOrNull() != null &&
            hasCategory
}

/** New-product form ViewModel: loads categories, optionally creates one, posts the product. */
@HiltViewModel
class ProductCreateViewModel @Inject constructor(
    private val repository: ProductRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductCreateUiState())
    val uiState: StateFlow<ProductCreateUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            when (val result = repository.getCategories()) {
                is ApiResult.Success -> _uiState.update { it.copy(categories = result.data) }
                is ApiResult.Failure -> Unit // non-blocking; user can still create a new category
            }
        }
    }

    fun onSelectCategory(id: String) =
        _uiState.update { it.copy(selectedCategoryId = id, creatingNewCategory = false, error = null) }

    fun onNewCategoryToggle() =
        _uiState.update { it.copy(creatingNewCategory = true, selectedCategoryId = null, error = null) }

    fun onNewCategoryNameChange(value: String) =
        _uiState.update { it.copy(newCategoryName = value, error = null) }

    fun onSkuChange(value: String) = _uiState.update { it.copy(sku = value, error = null) }
    fun onNameChange(value: String) = _uiState.update { it.copy(name = value, error = null) }
    fun onPriceChange(value: String) = _uiState.update { it.copy(price = value, error = null) }
    fun onCostChange(value: String) = _uiState.update { it.copy(cost = value) }
    fun onTaxRateChange(value: String) = _uiState.update { it.copy(taxRate = value) }
    fun onMinStockChange(value: String) = _uiState.update { it.copy(minStock = value) }

    fun submit() {
        val state = _uiState.value
        if (!state.canSubmit) return
        viewModelScope.launch {
            _uiState.update { it.copy(submitting = true, error = null) }

            val categoryId = resolveCategoryId(state)
            if (categoryId == null) {
                _uiState.update { it.copy(submitting = false, error = UiText.res(com.carloserp.android.R.string.form_error_generic)) }
                return@launch
            }

            val newProduct = NewProduct(
                categoryId = categoryId,
                sku = state.sku.trim(),
                name = state.name.trim(),
                price = state.price.trim(),
                cost = state.cost.trim().ifBlank { null },
                taxRate = state.taxRate.toDoubleOrNull() ?: 0.0,
                minStock = state.minStock.toIntOrNull() ?: 0,
            )
            when (val result = repository.createProduct(newProduct)) {
                is ApiResult.Success -> _uiState.update { it.copy(submitting = false, created = true) }
                is ApiResult.Failure ->
                    _uiState.update { it.copy(submitting = false, error = result.error.toUserMessage()) }
            }
        }
    }

    /** Resolves the category id, creating the category first when a new one was typed. */
    private suspend fun resolveCategoryId(state: ProductCreateUiState): String? {
        if (!state.creatingNewCategory) return state.selectedCategoryId
        return when (val result = repository.createCategory(state.newCategoryName.trim())) {
            is ApiResult.Success -> result.data.id
            is ApiResult.Failure -> {
                _uiState.update { it.copy(error = result.error.toUserMessage()) }
                null
            }
        }
    }
}
