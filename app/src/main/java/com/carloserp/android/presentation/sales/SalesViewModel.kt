package com.carloserp.android.presentation.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.domain.model.Sale
import com.carloserp.android.domain.repository.SaleRepository
import com.carloserp.android.presentation.common.UiState
import com.carloserp.android.presentation.common.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Sales list ViewModel (task 50.4). Loads recent sales into [UiState]; [load]
 * backs the initial fetch, the retry action, and a refresh after creating a sale.
 */
@HiltViewModel
class SalesViewModel @Inject constructor(
    private val repository: SaleRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Sale>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Sale>>> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = when (val result = repository.getSales()) {
                is ApiResult.Success -> UiState.Success(result.data)
                is ApiResult.Failure -> UiState.Error(result.error.toUserMessage())
            }
        }
    }
}
