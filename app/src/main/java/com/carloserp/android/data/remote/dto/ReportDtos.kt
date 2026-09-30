package com.carloserp.android.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Report aggregates used to build the mobile dashboard (task 50.4).
 *
 * The backend has no dashboard endpoint, so the dashboard is assembled from
 * `GET /reports/sales` ([SalesReportDto]) and `GET /reports/stock`
 * ([StockReportDto]). Only the fields the dashboard shows are declared; the rest
 * (e.g. the `daily`/`items` arrays) are ignored by the tolerant JSON parser.
 */
@Serializable
data class SalesReportDto(
    val totals: SalesTotalsDto = SalesTotalsDto(),
)

@Serializable
data class SalesTotalsDto(
    val count: Int = 0,
    val subtotal: String = "0",
    val tax: String = "0",
    val total: String = "0",
)

@Serializable
data class StockReportDto(
    val totalItems: Int = 0,
    val lowStockCount: Int = 0,
)
