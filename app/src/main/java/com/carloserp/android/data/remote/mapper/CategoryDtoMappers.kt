package com.carloserp.android.data.remote.mapper

import com.carloserp.android.data.remote.dto.CategoryDto
import com.carloserp.android.data.remote.dto.CreateProductDto
import com.carloserp.android.domain.model.Category
import com.carloserp.android.domain.model.NewProduct

/** Maps the network [CategoryDto] to the domain [Category]. */
fun CategoryDto.toDomain(): Category = Category(id = id, name = name)

/** Maps a [NewProduct] input to the create-product request body. */
fun NewProduct.toDto(): CreateProductDto =
    CreateProductDto(
        categoryId = categoryId,
        sku = sku,
        name = name,
        price = price,
        cost = cost,
        taxRate = taxRate,
        unit = unit,
        minStock = minStock,
        currency = currency,
    )
