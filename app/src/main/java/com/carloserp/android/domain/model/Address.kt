package com.carloserp.android.domain.model

/**
 * A single address autocomplete suggestion (address-validation flow). Returned
 * by `GET /addresses/autocomplete`; the user picks one to geocode.
 */
data class AddressSuggestion(
    val id: String,
    val country: String,
    val locality: String,
    val stateOrProvince: String,
    val streetName: String,
    val streetType: String,
) {
    /** Single-line label shown in the suggestions dropdown. */
    val label: String
        get() = listOf(streetName, locality, stateOrProvince)
            .filter { it.isNotBlank() }
            .joinToString(", ")
}

/**
 * A geocoded address with the coordinates for the map pin. Mapped from the
 * `GET /addresses/{id}/location` response (x = longitude, y = latitude).
 */
data class GeocodedAddress(
    val streetName: String,
    val streetNr: Int?,
    val locality: String,
    val city: String,
    val stateOrProvince: String,
    val country: String,
    val postcode: String,
    val latitude: Double,
    val longitude: Double,
) {
    /** Human-readable one-line address used as the customer's stored address. */
    val formatted: String
        get() {
            val street = listOfNotNull(
                streetName.takeIf { it.isNotBlank() },
                streetNr?.toString(),
            ).joinToString(" ")
            return listOf(street, locality, stateOrProvince, postcode, country)
                .filter { it.isNotBlank() }
                .joinToString(", ")
        }
}
