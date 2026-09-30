package com.carloserp.android.presentation.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.domain.model.StockLevel
import com.carloserp.android.domain.repository.StockRepository
import com.carloserp.android.presentation.common.UiState
import com.carloserp.android.presentation.common.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Stock ViewModel (task 50.4). Loads current stock levels into [UiState]; low
 * levels are flagged per item so the screen can highlight them. [load] backs the
 * initial fetch and retry.
 */
@HiltViewModel
class StockViewModel @Inject constructor(
    private val repository: StockRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<StockLevel>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<StockLevel>>> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = when (val result = repository.getStockLevels()) {
                is ApiResult.Success -> UiState.Success(result.data)
                is ApiResult.Failure -> UiState.Error(result.error.toUserMessage())
            }
        }
    }
}
