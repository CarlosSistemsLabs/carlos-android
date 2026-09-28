package com.carloserp.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room row for the cached product (task 49.4).
 *
 * Persistence mirror of [com.carloserp.android.domain.model.Product]. Kept
 * separate from the domain model so storage concerns (annotations, column
 * types) never leak into the domain layer.
 */
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val sku: String,
    val priceCents: Long,
    val updatedAt: Long,
)
