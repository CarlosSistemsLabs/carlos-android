package com.carloserp.android.domain.model

/**
 * Aggregated metrics shown on the dashboard (task 50.4), assembled from the
 * sales and stock reports. [salesTotal] is a decimal string (no currency, as the
 * reports don't carry one).
 */
data class DashboardMetrics(
    val salesCount: Int,
    val salesTotal: String,
    val totalProducts: Int,
    val lowStockCount: Int,
)
