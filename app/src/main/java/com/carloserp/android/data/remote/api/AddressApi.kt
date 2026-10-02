package com.carloserp.android.data.remote.api

import com.carloserp.android.data.remote.dto.AddressSuggestionDto
import com.carloserp.android.data.remote.dto.GeographicAddressDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit definition of the address-validation endpoints.
 *
 * - `GET /addresses/autocomplete?q=` returns candidate suggestions (empty until
 *   the query has at least 3 characters, enforced server-side).
 * - `GET /addresses/location?id=` geocodes a chosen suggestion (array of
 *   addresses, usually one). The place id is a query param because Google's
 *   ids are opaque, variable-length tokens. Tenant/auth come from the token.
 */
interface AddressApi {

    @GET("addresses/autocomplete")
    suspend fun autocomplete(@Query("q") query: String): List<AddressSuggestionDto>

    @GET("addresses/location")
    suspend fun locate(@Query("id") id: String): List<GeographicAddressDto>
}
