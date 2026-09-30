package com.carloserp.android.domain.model

/**
 * Product domain model (tasks 49.4 / 50.4).
 *
 * Framework-free representation used by the UI/domain layers, matching the
 * backend product contract. Money fields ([price], [cost], [priceWithTax]) are
 * kept as decimal strings exactly as the API sends them (no floating point);
 * format them for display with `formatMoney`.
 */
data class Product(
    val id: String,
    val categoryId: String,
    val sku: String,
    val name: String,
    val description: String?,
    val price: String,
    val cost: String?,
    val currency: String,
    val taxRate: Double,
    val priceWithTax: String,
    val unit: String,
    val minStock: Int,
    val isActive: Boolean,
    val imageUrl: String?,
)
