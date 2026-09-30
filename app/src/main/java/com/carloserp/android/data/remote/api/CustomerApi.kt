package com.carloserp.android.data.remote.api

import com.carloserp.android.data.remote.dto.CustomerDto
import com.carloserp.android.data.remote.dto.PagedDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit definition of the customers endpoints (task 50.4). Read-only for the
 * mobile client; tenant scoping comes from the Bearer token.
 */
interface CustomerApi {

    @GET("customers")
    suspend fun list(
        @Query("page") page: Int? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("isActive") isActive: Boolean? = null,
    ): PagedDto<CustomerDto>

    @GET("customers/{id}")
    suspend fun get(@Path("id") id: String): CustomerDto
}
