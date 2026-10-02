package com.carloserp.android.presentation.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carloserp.android.R
import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.core.ui.UiText
import com.carloserp.android.domain.model.AddressSuggestion
import com.carloserp.android.domain.model.GeocodedAddress
import com.carloserp.android.domain.repository.AddressRepository
import com.carloserp.android.presentation.common.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * State of the address-validation screen.
 *
 * The user types into [query]; once it has [MIN_QUERY_LENGTH] characters the
 * (debounced) autocomplete fills [suggestions]. Picking one geocodes it into
 * [geocoded] (coordinates for the map pin). [confirmable] is the address that
 * can be returned to the caller once a location is resolved.
 */
data class AddressValidationUiState(
    val query: String = "",
    val suggestions: List<AddressSuggestion> = emptyList(),
    val loadingSuggestions: Boolean = false,
    val selected: AddressSuggestion? = null,
    val locating: Boolean = false,
    val geocoded: GeocodedAddress? = null,
    val error: UiText? = null,
) {
    /** The resolved address to hand back; `null` until a location is geocoded. */
    val confirmable: GeocodedAddress? get() = geocoded
}

/**
 * Drives the address-validation flow: debounced autocomplete (≥3 chars) and the
 * geocode of the chosen suggestion. Both calls go through [AddressRepository]
 * and surface typed errors as localizable [UiText].
 */
@HiltViewModel
class AddressValidationViewModel @Inject constructor(
    private val repository: AddressRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddressValidationUiState())
    val uiState: StateFlow<AddressValidationUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(value: String) {
        _uiState.update {
            it.copy(query = value, selected = null, geocoded = null, error = null)
        }
        searchJob?.cancel()
        if (value.trim().length < MIN_QUERY_LENGTH) {
            _uiState.update { it.copy(suggestions = emptyList(), loadingSuggestions = false) }
            return
        }
        searchJob = viewModelScope.launch {
            delay(DEBOUNCE_MS)
            _uiState.update { it.copy(loadingSuggestions = true) }
            when (val result = repository.autocomplete(value.trim())) {
                is ApiResult.Success ->
                    _uiState.update { it.copy(loadingSuggestions = false, suggestions = result.data) }

                is ApiResult.Failure ->
                    _uiState.update {
                        it.copy(
                            loadingSuggestions = false,
                            suggestions = emptyList(),
                            error = result.error.toUserMessage(),
                        )
                    }
            }
        }
    }

    fun onSelectSuggestion(suggestion: AddressSuggestion) {
        searchJob?.cancel()
        _uiState.update {
            it.copy(
                selected = suggestion,
                query = suggestion.label,
                suggestions = emptyList(),
                locating = true,
                geocoded = null,
                error = null,
            )
        }
        viewModelScope.launch {
            when (val result = repository.locate(suggestion.id)) {
                is ApiResult.Success ->
                    _uiState.update {
                        it.copy(
                            locating = false,
                            geocoded = result.data,
                            error = if (result.data == null) {
                                UiText.res(R.string.address_not_located)
                            } else {
                                null
                            },
                        )
                    }

                is ApiResult.Failure ->
                    _uiState.update {
                        it.copy(locating = false, error = result.error.toUserMessage())
                    }
            }
        }
    }

    private companion object {
        const val MIN_QUERY_LENGTH = 3
        const val DEBOUNCE_MS = 350L
    }
}
