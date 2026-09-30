package com.carloserp.android.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.domain.model.DashboardMetrics
import com.carloserp.android.domain.repository.DashboardRepository
import com.carloserp.android.presentation.common.UiState
import com.carloserp.android.presentation.common.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Dashboard ViewModel (task 50.4). Loads the aggregated [DashboardMetrics] into
 * [UiState]; [load] backs the initial fetch and retry.
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DashboardRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<DashboardMetrics>>(UiState.Loading)
    val uiState: StateFlow<UiState<DashboardMetrics>> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = when (val result = repository.getMetrics()) {
                is ApiResult.Success -> UiState.Success(result.data)
                is ApiResult.Failure -> UiState.Error(result.error.toUserMessage())
            }
        }
    }
}
