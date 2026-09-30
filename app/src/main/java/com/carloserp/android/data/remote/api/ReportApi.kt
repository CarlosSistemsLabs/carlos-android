package com.carloserp.android.data.remote.api

import com.carloserp.android.data.remote.dto.SalesReportDto
import com.carloserp.android.data.remote.dto.StockReportDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit definition of the reports endpoints used by the dashboard (task 50.4).
 * `from`/`to` are optional ISO date-times; omitting them uses the backend
 * default range. Tenant scoping comes from the Bearer token.
 */
interface ReportApi {

    @GET("reports/sales")
    suspend fun salesReport(
        @Query("from") from: String? = null,
        @Query("to") to: String? = null,
    ): SalesReportDto

    @GET("reports/stock")
    suspend fun stockReport(
        @Query("branchId") branchId: String? = null,
    ): StockReportDto
}
