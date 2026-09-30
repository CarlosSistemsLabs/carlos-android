package com.carloserp.android.data.local.mapper

import com.carloserp.android.data.local.entity.ProductEntity
import com.carloserp.android.domain.model.Product

/**
 * Mappers between the Room [ProductEntity] and the domain [Product]
 * (tasks 49.4 / 50.4). One-to-one; kept explicit so the two shapes can diverge
 * later without coupling the layers.
 */
fun ProductEntity.toDomain(): Product =
    Product(
        id = id,
        categoryId = categoryId,
        sku = sku,
        name = name,
        description = description,
        price = price,
        cost = cost,
        currency = currency,
        taxRate = taxRate,
        priceWithTax = priceWithTax,
        unit = unit,
        minStock = minStock,
        isActive = isActive,
        imageUrl = imageUrl,
    )

fun Product.toEntity(): ProductEntity =
    ProductEntity(
        id = id,
        categoryId = categoryId,
        sku = sku,
        name = name,
        description = description,
        price = price,
        cost = cost,
        currency = currency,
        taxRate = taxRate,
        priceWithTax = priceWithTax,
        unit = unit,
        minStock = minStock,
        isActive = isActive,
        imageUrl = imageUrl,
    )
