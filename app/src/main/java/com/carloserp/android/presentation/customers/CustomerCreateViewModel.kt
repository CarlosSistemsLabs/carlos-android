package com.carloserp.android.presentation.customers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.core.ui.UiText
import com.carloserp.android.domain.model.NewCustomer
import com.carloserp.android.domain.repository.CustomerRepository
import com.carloserp.android.presentation.common.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Form state for creating a customer. [canSubmit] requires a non-blank name. */
data class CustomerCreateUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val taxId: String = "",
    val address: String = "",
    val notes: String = "",
    val submitting: Boolean = false,
    val error: UiText? = null,
    val created: Boolean = false,
) {
    val canSubmit: Boolean get() = !submitting && name.isNotBlank()
}

/** New-customer form ViewModel; posts the customer and flips [created] on success. */
@HiltViewModel
class CustomerCreateViewModel @Inject constructor(
    private val repository: CustomerRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerCreateUiState())
    val uiState: StateFlow<CustomerCreateUiState> = _uiState.asStateFlow()

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value, error = null) }
    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value) }
    fun onPhoneChange(value: String) = _uiState.update { it.copy(phone = value) }
    fun onTaxIdChange(value: String) = _uiState.update { it.copy(taxId = value) }
    fun onAddressChange(value: String) = _uiState.update { it.copy(address = value) }
    fun onNotesChange(value: String) = _uiState.update { it.copy(notes = value) }

    fun submit() {
        val state = _uiState.value
        if (!state.canSubmit) return
        viewModelScope.launch {
            _uiState.update { it.copy(submitting = true, error = null) }
            val newCustomer = NewCustomer(
                name = state.name.trim(),
                email = state.email.trim().ifBlank { null },
                phone = state.phone.trim().ifBlank { null },
                taxId = state.taxId.trim().ifBlank { null },
                address = state.address.trim().ifBlank { null },
                notes = state.notes.trim().ifBlank { null },
            )
            when (val result = repository.createCustomer(newCustomer)) {
                is ApiResult.Success -> _uiState.update { it.copy(submitting = false, created = true) }
                is ApiResult.Failure ->
                    _uiState.update { it.copy(submitting = false, error = result.error.toUserMessage()) }
            }
        }
    }
}
