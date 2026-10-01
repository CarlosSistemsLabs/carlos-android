package com.carloserp.android.presentation.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carloserp.android.core.analytics.AnalyticsEvents
import com.carloserp.android.core.analytics.AnalyticsService
import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.domain.model.Customer
import com.carloserp.android.domain.model.NewSale
import com.carloserp.android.domain.model.NewSaleItem
import com.carloserp.android.domain.model.Product
import com.carloserp.android.domain.repository.CustomerRepository
import com.carloserp.android.domain.repository.ProductRepository
import com.carloserp.android.domain.repository.SaleRepository
import com.carloserp.android.presentation.common.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * State for the new-sale form (task 50.4). [canSubmit] gates the confirm button:
 * a customer must be chosen and at least one line must have a positive quantity.
 */
data class SaleCreateUiState(
    val loading: Boolean = true,
    val loadError: String? = null,
    val customers: List<Customer> = emptyList(),
    val products: List<Product> = emptyList(),
    val selectedCustomerId: String? = null,
    val quantities: Map<String, Int> = emptyMap(),
    val submitting: Boolean = false,
    val submitError: String? = null,
    val created: Boolean = false,
) {
    val canSubmit: Boolean
        get() = !submitting && selectedCustomerId != null && quantities.values.any { it > 0 }
}

/**
 * New-sale form ViewModel (task 50.4).
 *
 * Loads the selectable customers and products up front, tracks the chosen
 * customer and per-product quantities, and posts the sale. Line items carry only
 * productId+quantity (the backend computes totals). On success it flips
 * [SaleCreateUiState.created] so the screen navigates back.
 */
@HiltViewModel
class SaleCreateViewModel @Inject constructor(
    private val saleRepository: SaleRepository,
    private val customerRepository: CustomerRepository,
    private val productRepository: ProductRepository,
    private val analytics: AnalyticsService,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SaleCreateUiState())
    val uiState: StateFlow<SaleCreateUiState> = _uiState.asStateFlow()

    init {
        loadOptions()
    }

    fun loadOptions() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, loadError = null) }
            when (val customers = customerRepository.getCustomers()) {
                is ApiResult.Success -> {
                    productRepository.refreshProducts()
                    val products = productRepository.observeProducts().first()
                    _uiState.update {
                        it.copy(loading = false, customers = customers.data, products = products)
                    }
                }

                is ApiResult.Failure ->
                    _uiState.update {
                        it.copy(loading = false, loadError = customers.error.toUserMessage())
                    }
            }
        }
    }

    fun onSelectCustomer(customerId: String) =
        _uiState.update { it.copy(selectedCustomerId = customerId) }

    fun onQuantityChange(productId: String, quantity: Int) {
        val safeQuantity = quantity.coerceAtLeast(0)
        _uiState.update { state ->
            val updated = state.quantities.toMutableMap()
            if (safeQuantity == 0) updated.remove(productId) else updated[productId] = safeQuantity
            state.copy(quantities = updated)
        }
    }

    fun submit() {
        val state = _uiState.value
        val customerId = state.selectedCustomerId
        if (!state.canSubmit || customerId == null) return

        val items = state.quantities
            .filterValues { it > 0 }
            .map { (productId, quantity) -> NewSaleItem(productId = productId, quantity = quantity) }

        viewModelScope.launch {
            _uiState.update { it.copy(submitting = true, submitError = null) }
            val newSale = NewSale(customerId = customerId, items = items, status = "completed")
            when (val result = saleRepository.createSale(newSale)) {
                is ApiResult.Success -> {
                    analytics.logEvent(AnalyticsEvents.SALE_CREATED)
                    _uiState.update { it.copy(submitting = false, created = true) }
                }
                is ApiResult.Failure ->
                    _uiState.update {
                        it.copy(submitting = false, submitError = result.error.toUserMessage())
                    }
            }
        }
    }
}
