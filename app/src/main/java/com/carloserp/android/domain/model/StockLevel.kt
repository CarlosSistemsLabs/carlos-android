package com.carloserp.android.domain.model

/**
 * Stock level domain model (task 50.4): the on-hand [quantity] for a product at
 * a branch, with [lowStock] flagging when it is at or below [minStock].
 */
data class StockLevel(
    val productId: String,
    val branchId: String?,
    val productName: String,
    val quantity: Int,
    val minStock: Int,
    val lowStock: Boolean,
)
