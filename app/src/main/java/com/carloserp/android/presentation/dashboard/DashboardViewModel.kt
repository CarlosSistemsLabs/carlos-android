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

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        load()
    }

    /** Initial load: shows the full-screen loading state. */
    fun load() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            fetch()
        }
    }

    /** Pull-to-refresh: keeps current content and shows the refresh indicator. */
    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            fetch()
            _isRefreshing.value = false
        }
    }

    private suspend fun fetch() {
        _uiState.value = when (val result = repository.getMetrics()) {
            is ApiResult.Success -> UiState.Success(result.data)
            is ApiResult.Failure -> UiState.Error(result.error.toUserMessage())
        }
    }
}
