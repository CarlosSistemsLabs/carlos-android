package com.carloserp.android.presentation.address

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carloserp.android.R
import com.carloserp.android.core.ui.asString
import com.carloserp.android.domain.model.AddressSuggestion
import com.carloserp.android.domain.model.GeocodedAddress
import com.carloserp.android.presentation.components.CarlosTextField
import com.carloserp.android.presentation.components.PrimaryButton
import com.carloserp.android.presentation.theme.CarlosTheme
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

/**
 * Address-validation screen reached from the new-customer form. The user types
 * an address (autocomplete after 3 chars), picks a suggestion, sees the pin on a
 * Google Map, and confirms — [onConfirm] returns the formatted address to the
 * caller. Header owned by the shell.
 */
@Composable
fun AddressValidationScreen(
    onConfirm: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddressValidationViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val spacing = CarlosTheme.spacing

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.md),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        CarlosTextField(
            value = state.query,
            onValueChange = viewModel::onQueryChange,
            label = stringResource(R.string.address_search_label),
            errorMessage = null,
        )
        Text(
            text = stringResource(R.string.address_search_hint),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        if (state.loadingSuggestions) {
            CircularProgressIndicator(modifier = Modifier.height(SPINNER_SIZE))
        }

        SuggestionList(
            suggestions = state.suggestions,
            onSelect = viewModel::onSelectSuggestion,
        )

        if (state.locating) {
            CircularProgressIndicator(modifier = Modifier.height(SPINNER_SIZE))
        }

        state.geocoded?.let { geocoded ->
            AddressMap(geocoded = geocoded)
            Text(
                text = stringResource(R.string.address_selected_label),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = geocoded.formatted,
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        state.error?.let { error ->
            Text(
                text = error.asString(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        val confirmable = state.confirmable
        PrimaryButton(
            text = stringResource(R.string.address_confirm),
            onClick = { confirmable?.let { onConfirm(it.formatted) } },
            enabled = confirmable != null && !state.locating,
        )
    }
}

@Composable
private fun SuggestionList(
    suggestions: List<AddressSuggestion>,
    onSelect: (AddressSuggestion) -> Unit,
) {
    if (suggestions.isEmpty()) return
    Card(modifier = Modifier.fillMaxWidth()) {
        suggestions.forEachIndexed { index, suggestion ->
            if (index > 0) HorizontalDivider()
            Text(
                text = suggestion.label,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(suggestion) }
                    .padding(horizontal = SUGGESTION_PADDING, vertical = SUGGESTION_PADDING),
            )
        }
    }
}

@Composable
private fun AddressMap(geocoded: GeocodedAddress) {
    val position = LatLng(geocoded.latitude, geocoded.longitude)
    // Re-create the camera + marker state when the coordinates change so the map
    // recenters on each newly geocoded address.
    key(geocoded.latitude, geocoded.longitude) {
        val cameraPositionState = rememberCameraPositionState {
            this.position = CameraPosition.fromLatLngZoom(position, MAP_ZOOM)
        }
        val markerState = rememberMarkerState(position = position)
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(MAP_HEIGHT),
            cameraPositionState = cameraPositionState,
        ) {
            Marker(
                state = markerState,
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
                title = geocoded.formatted,
            )
        }
    }
}

private val SPINNER_SIZE = 28.dp
private val SUGGESTION_PADDING = 12.dp
private val MAP_HEIGHT = 280.dp
private const val MAP_ZOOM = 16f
