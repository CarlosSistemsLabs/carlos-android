package com.carloserp.android.data.remote.mapper

import com.carloserp.android.data.remote.dto.ProductDto
import com.carloserp.android.domain.model.Product

/**
 * Maps the network [ProductDto] to the domain [Product] (task 50.4), dropping
 * transport-only fields (`tenantId`). The reverse mapping isn't needed yet
 * (product create/update from mobile is out of scope for 50.4).
 */
fun ProductDto.toDomain(): Product =
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
