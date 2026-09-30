package com.carloserp.android.presentation.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.domain.model.Product
import com.carloserp.android.domain.repository.ProductRepository
import com.carloserp.android.presentation.common.UiState
import com.carloserp.android.presentation.common.toUserMessage
import com.carloserp.android.presentation.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Product detail ViewModel (task 50.4).
 *
 * Reads the product id from the navigation arguments ([SavedStateHandle]) and
 * loads the product from the backend, exposing it as [UiState]. [load] backs the
 * retry action.
 */
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ProductRepository,
) : ViewModel() {

    private val productId: String = checkNotNull(savedStateHandle[Destination.ProductDetail.ARG]) {
        "Missing ${Destination.ProductDetail.ARG} navigation argument"
    }

    private val _uiState = MutableStateFlow<UiState<Product>>(UiState.Loading)
    val uiState: StateFlow<UiState<Product>> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = when (val result = repository.getProduct(productId)) {
                is ApiResult.Success -> UiState.Success(result.data)
                is ApiResult.Failure -> UiState.Error(result.error.toUserMessage())
            }
        }
    }
}
