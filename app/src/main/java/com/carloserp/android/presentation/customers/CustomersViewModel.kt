package com.carloserp.android.presentation.customers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.domain.model.Customer
import com.carloserp.android.domain.repository.CustomerRepository
import com.carloserp.android.presentation.common.UiState
import com.carloserp.android.presentation.common.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Customers list ViewModel (task 50.4). Loads the customer list into [UiState];
 * [load] backs the initial fetch and the retry action.
 */
@HiltViewModel
class CustomersViewModel @Inject constructor(
    private val repository: CustomerRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Customer>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Customer>>> = _uiState.asStateFlow()

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
        _uiState.value = when (val result = repository.getCustomers()) {
            is ApiResult.Success -> UiState.Success(result.data)
            is ApiResult.Failure -> UiState.Error(result.error.toUserMessage())
        }
    }
}
