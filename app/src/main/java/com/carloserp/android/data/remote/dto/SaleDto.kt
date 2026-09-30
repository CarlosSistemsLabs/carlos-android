package com.carloserp.android.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Sale DTOs for the `/sales` endpoints (task 50.4).
 *
 * [SaleDto] is the response of list/get (money as decimal strings, `saleDate`
 * an ISO string, `items` present on detail). [CreateSaleDto] is the create body:
 * line items carry ONLY productId+quantity — the server computes prices/totals
 * and takes the author from the JWT — and `status` is limited to draft|completed.
 */
@Serializable
data class SaleDto(
    val id: String,
    val customerId: String,
    val branchId: String? = null,
    val userId: String? = null,
    val saleNumber: String,
    val saleDate: String,
    val status: String,
    val currency: String = "ARS",
    val subtotal: String = "0",
    val taxAmount: String = "0",
    val total: String = "0",
    val notes: String? = null,
    val items: List<SaleLineDto> = emptyList(),
)

@Serializable
data class SaleLineDto(
    val id: String,
    val productId: String,
    val quantity: Int,
    val unitPrice: String = "0",
    val taxRate: Double = 0.0,
    val subtotal: String = "0",
    val taxAmount: String = "0",
    val total: String = "0",
)

@Serializable
data class CreateSaleDto(
    val customerId: String,
    val items: List<CreateSaleItemDto>,
    val branchId: String? = null,
    val notes: String? = null,
    val status: String? = null,
)

@Serializable
data class CreateSaleItemDto(
    val productId: String,
    val quantity: Int,
)
