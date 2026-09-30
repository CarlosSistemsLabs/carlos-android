package com.carloserp.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room row for the cached product (tasks 49.4 / 50.4).
 *
 * Persistence mirror of [com.carloserp.android.domain.model.Product]. Kept
 * separate from the domain model so storage concerns (annotations, column
 * types) never leak into the domain layer. Money stays as decimal strings.
 */
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
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
