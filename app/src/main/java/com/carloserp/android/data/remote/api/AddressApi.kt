package com.carloserp.android.data.remote.api

import com.carloserp.android.data.remote.dto.AddressSuggestionDto
import com.carloserp.android.data.remote.dto.GeographicAddressDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit definition of the address-validation endpoints.
 *
 * - `GET /addresses/autocomplete?q=` returns candidate suggestions (empty until
 *   the query has at least 3 characters, enforced server-side).
 * - `GET /addresses/{id}/location` geocodes a chosen suggestion (array of
 *   addresses, usually one). Tenant/auth come from the Bearer token.
 */
interface AddressApi {

    @GET("addresses/autocomplete")
    suspend fun autocomplete(@Query("q") query: String): List<AddressSuggestionDto>

    @GET("addresses/{id}/location")
    suspend fun locate(@Path("id") id: String): List<GeographicAddressDto>
}
