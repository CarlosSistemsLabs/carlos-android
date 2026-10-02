package com.carloserp.android.domain.model

/** Product category (create products from the app). */
data class Category(
    val id: String,
    val name: String,
)

/**
 * Input for creating a product from the app. Money ([price], [cost]) are decimal
 * strings; [taxRate] a percentage; [minStock] units.
 */
data class NewProduct(
    val categoryId: String,
    val sku: String,
    val name: String,
    val price: String,
    val cost: String? = null,
    val taxRate: Double = 0.0,
    val unit: String = "unidad",
    val minStock: Int = 0,
    val currency: String = "ARS",
)
