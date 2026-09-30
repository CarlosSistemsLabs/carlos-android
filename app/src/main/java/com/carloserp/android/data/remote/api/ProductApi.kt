package com.carloserp.android.data.remote.api

import com.carloserp.android.data.remote.dto.PagedDto
import com.carloserp.android.data.remote.dto.ProductDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit definition of the products endpoints (task 50.4).
 *
 * Paths are relative to the `/api/v1/` base URL. Only the read endpoints the
 * mobile client needs are declared; the tenant is applied server-side from the
 * Bearer token, so no tenant param is sent.
 */
interface ProductApi {

    @GET("products")
    suspend fun list(
        @Query("page") page: Int? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("isActive") isActive: Boolean? = null,
    ): PagedDto<ProductDto>

    @GET("products/{id}")
    suspend fun get(@Path("id") id: String): ProductDto
}
