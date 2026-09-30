package com.carloserp.android.data.remote.mapper

import com.carloserp.android.data.remote.dto.CreateSaleDto
import com.carloserp.android.data.remote.dto.CreateSaleItemDto
import com.carloserp.android.data.remote.dto.SaleDto
import com.carloserp.android.data.remote.dto.SaleLineDto
import com.carloserp.android.domain.model.NewSale
import com.carloserp.android.domain.model.Sale
import com.carloserp.android.domain.model.SaleLine

/** Maps sale DTOs/domain models across the network boundary (task 50.4). */
fun SaleDto.toDomain(): Sale =
    Sale(
        id = id,
        customerId = customerId,
        saleNumber = saleNumber,
        saleDate = saleDate,
        status = status,
        currency = currency,
        subtotal = subtotal,
        taxAmount = taxAmount,
        total = total,
        notes = notes,
        items = items.map { it.toDomain() },
    )

fun SaleLineDto.toDomain(): SaleLine =
    SaleLine(
        id = id,
        productId = productId,
        quantity = quantity,
        unitPrice = unitPrice,
        total = total,
    )

fun NewSale.toDto(): CreateSaleDto =
    CreateSaleDto(
        customerId = customerId,
        items = items.map { CreateSaleItemDto(productId = it.productId, quantity = it.quantity) },
        notes = notes,
        status = status,
    )
