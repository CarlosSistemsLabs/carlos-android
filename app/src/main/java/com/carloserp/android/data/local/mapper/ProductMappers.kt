package com.carloserp.android.data.local.mapper

import com.carloserp.android.data.local.entity.ProductEntity
import com.carloserp.android.domain.model.Product

/**
 * Mappers between the Room [ProductEntity] and the domain [Product] (task 49.4).
 * One-to-one today; kept explicit so the two shapes can diverge later without
 * coupling the layers.
 */
fun ProductEntity.toDomain(): Product =
    Product(
        id = id,
        name = name,
        sku = sku,
        priceCents = priceCents,
        updatedAt = updatedAt,
    )

fun Product.toEntity(): ProductEntity =
    ProductEntity(
        id = id,
        name = name,
        sku = sku,
        priceCents = priceCents,
        updatedAt = updatedAt,
    )
