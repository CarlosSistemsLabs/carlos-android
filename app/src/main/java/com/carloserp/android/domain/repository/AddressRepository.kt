package com.carloserp.android.domain.repository

import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.domain.model.AddressSuggestion
import com.carloserp.android.domain.model.GeocodedAddress

/**
 * Address-validation port. Two steps power the mobile flow:
 * [autocomplete] turns typed text into candidate suggestions, and [locate]
 * geocodes a chosen suggestion into coordinates for the map pin.
 */
interface AddressRepository {
    suspend fun autocomplete(query: String): ApiResult<List<AddressSuggestion>>

    /** Geocodes [id]; the value is `null` when the provider returns no location. */
    suspend fun locate(id: String): ApiResult<GeocodedAddress?>
}
