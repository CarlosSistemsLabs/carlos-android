package com.carloserp.android.presentation.customers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.domain.model.Customer
import com.carloserp.android.domain.repository.CustomerRepository
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
 * Customer detail ViewModel (task 50.4). Reads the customer id from the nav
 * arguments and loads the record, exposed as [UiState]; [load] backs retry.
 */
@HiltViewModel
class CustomerDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: CustomerRepository,
) : ViewModel() {

    private val customerId: String = checkNotNull(savedStateHandle[Destination.CustomerDetail.ARG]) {
        "Missing ${Destination.CustomerDetail.ARG} navigation argument"
    }

    private val _uiState = MutableStateFlow<UiState<Customer>>(UiState.Loading)
    val uiState: StateFlow<UiState<Customer>> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = when (val result = repository.getCustomer(customerId)) {
                is ApiResult.Success -> UiState.Success(result.data)
                is ApiResult.Failure -> UiState.Error(result.error.toUserMessage())
            }
        }
    }
}
