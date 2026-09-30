package com.carloserp.android.data.remote.api

import com.carloserp.android.data.remote.dto.PagedDto
import com.carloserp.android.data.remote.dto.StockLevelDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit definition of the stock endpoints (task 50.4). Read-only for mobile;
 * tenant scoping comes from the Bearer token.
 */
interface StockApi {

    @GET("stock")
    suspend fun list(
        @Query("page") page: Int? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("branchId") branchId: String? = null,
    ): PagedDto<StockLevelDto>

    @GET("stock/alerts")
    suspend fun alerts(
        @Query("page") page: Int? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("branchId") branchId: String? = null,
    ): PagedDto<StockLevelDto>
}
