package com.carloserp.android.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.domain.model.Product
import com.carloserp.android.domain.repository.ProductRepository
import com.carloserp.android.presentation.common.UiState
import com.carloserp.android.presentation.common.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Products list ViewModel (task 50.4) — offline-first.
 *
 * Exposes the cached products (Room, the single source of truth) as [UiState],
 * and triggers a backend [refresh] on init and on demand. While the cache is
 * empty it shows loading or, if the refresh failed, the error; once there is
 * cached data it always shows the list (a refresh failure then surfaces via
 * [refreshError] without hiding content).
 */
@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ProductRepository,
) : ViewModel() {

    private val loading = MutableStateFlow(true)
    private val error = MutableStateFlow<String?>(null)

    /** Non-blocking refresh error shown as a banner when the cache is non-empty. */
    val refreshError: StateFlow<String?> = error

    private val _isRefreshing = MutableStateFlow(false)

    /** Drives the pull-to-refresh indicator (separate from the empty-state loading). */
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    val uiState: StateFlow<UiState<List<Product>>> =
        combine(repository.observeProducts(), loading, error) { products, isLoading, errorMessage ->
            when {
                products.isNotEmpty() -> UiState.Success(products)
                isLoading -> UiState.Loading
                errorMessage != null -> UiState.Error(errorMessage)
                else -> UiState.Success(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = UiState.Loading,
        )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            loading.value = true
            _isRefreshing.value = true
            error.value = null
            when (val result = repository.refreshProducts()) {
                is ApiResult.Success -> Unit
                is ApiResult.Failure -> error.value = result.error.toUserMessage()
            }
            loading.value = false
            _isRefreshing.value = false
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
