package com.carloserp.android.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Stock level as returned by `GET /stock` and `GET /stock/alerts` (task 50.4).
 * `lowStock` is `true` when [quantity] is at or below [minStock].
 */
@Serializable
data class StockLevelDto(
    val productId: String,
    val branchId: String? = null,
    val quantity: Int,
    val productName: String,
    val minStock: Int = 0,
    val lowStock: Boolean = false,
)
