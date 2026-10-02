package com.carloserp.android.data.remote.mapper

import com.carloserp.android.data.remote.dto.AddressSuggestionDto
import com.carloserp.android.data.remote.dto.GeographicAddressDto
import com.carloserp.android.domain.model.AddressSuggestion
import com.carloserp.android.domain.model.GeocodedAddress

/** Maps an autocomplete DTO to the domain [AddressSuggestion]. */
fun AddressSuggestionDto.toDomain(): AddressSuggestion =
    AddressSuggestion(
        id = id,
        country = country,
        locality = locality,
        stateOrProvince = stateOrProvince,
        streetName = streetName,
        streetType = streetType,
    )

/**
 * Maps a geocoded DTO to the domain [GeocodedAddress], reading the first
 * geometry point (x = longitude, y = latitude). Returns `null` when there is no
 * usable coordinate so the caller can treat it as "not located".
 */
fun GeographicAddressDto.toDomain(): GeocodedAddress? {
    val point = geographicLocation.geometry.firstOrNull() ?: return null
    val longitude = point.x.toDoubleOrNull() ?: return null
    val latitude = point.y.toDoubleOrNull() ?: return null
    return GeocodedAddress(
        streetName = streetName,
        streetNr = streetNr,
        locality = locality,
        city = city,
        stateOrProvince = stateOrProvince,
        country = country,
        postcode = postcode,
        latitude = latitude,
        longitude = longitude,
    )
}
