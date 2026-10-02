package com.carloserp.android.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Autocomplete suggestion as returned by `GET /addresses/autocomplete`. */
@Serializable
data class AddressSuggestionDto(
    val id: String,
    val country: String = "",
    val locality: String = "",
    val stateOrProvince: String = "",
    val streetName: String = "",
    val streetType: String = "",
)

/** A WGS84 point; `x` is longitude, `y` is latitude (strings from the provider). */
@Serializable
data class GeographicPointDto(
    val x: String = "",
    val y: String = "",
)

/** Geocoded location envelope for a chosen address. */
@Serializable
data class GeographicLocationDto(
    val geometry: List<GeographicPointDto> = emptyList(),
    val geometryType: String = "",
    val name: String = "",
    val spatialRef: String = "",
)

/**
 * Geocoded address returned (as an array) by `GET /addresses/{id}/location`.
 * Only the fields the app needs are modelled; unknown keys are ignored.
 */
@Serializable
data class GeographicAddressDto(
    @SerialName("@type") val type: String = "",
    val geographicLocation: GeographicLocationDto = GeographicLocationDto(),
    val streetName: String = "",
    val streetNr: Int? = null,
    val streetType: String = "",
    val locality: String = "",
    val city: String = "",
    val stateOrProvince: String = "",
    val country: String = "",
    val postcode: String = "",
    val isNormalized: Boolean = false,
)
