package com.carloserp.android.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Product as returned by `GET /products` and `GET /products/:id` (task 50.4).
 *
 * Field names/types mirror the backend exactly: money is decimal strings,
 * `taxRate` a number, and nullable fields are nullable here. `tenantId` is
 * accepted but ignored (the client is already tenant-scoped by its token).
 */
@Serializable
data class ProductDto(
    val id: String,
    val categoryId: String,
    val sku: String,
    val name: String,
    val description: String? = null,
    val price: String,
    val cost: String? = null,
    val currency: String = "ARS",
    val taxRate: Double = 0.0,
    val priceWithTax: String = "0",
    val unit: String = "unit",
    val minStock: Int = 0,
    val isActive: Boolean = true,
    val imageUrl: String? = null,
)

/** Body of `POST /products` (create products from the app). */
@Serializable
data class CreateProductDto(
    val categoryId: String,
    val sku: String,
    val name: String,
    val price: String,
    val cost: String? = null,
    val taxRate: Double? = null,
    val unit: String? = null,
    val minStock: Int? = null,
    val currency: String? = null,
    val isActive: Boolean = true,
)
