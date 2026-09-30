package com.carloserp.android.data.remote.api

import com.carloserp.android.data.remote.dto.CreateSaleDto
import com.carloserp.android.data.remote.dto.PagedDto
import com.carloserp.android.data.remote.dto.SaleDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit definition of the sales endpoints (task 50.4). Tenant + author come
 * from the Bearer token; the client never sends them.
 */
interface SaleApi {

    @GET("sales")
    suspend fun list(
        @Query("page") page: Int? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("status") status: String? = null,
    ): PagedDto<SaleDto>

    @GET("sales/{id}")
    suspend fun get(@Path("id") id: String): SaleDto

    @POST("sales")
    suspend fun create(@Body body: CreateSaleDto): SaleDto
}
