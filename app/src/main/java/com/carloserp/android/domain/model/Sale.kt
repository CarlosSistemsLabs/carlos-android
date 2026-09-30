package com.carloserp.android.domain.model

/**
 * Sale domain models (task 50.4).
 *
 * [Sale] is the header (+ optional [items] on detail); money stays as decimal
 * strings and [saleDate] as the raw ISO string. [status] is the backend enum
 * value (draft|completed|cancelled). [NewSale] is the input for creating a sale:
 * only customer + product/quantity lines, matching the backend create contract.
 */
data class Sale(
    val id: String,
    val customerId: String,
    val saleNumber: String,
    val saleDate: String,
    val status: String,
    val currency: String,
    val subtotal: String,
    val taxAmount: String,
    val total: String,
    val notes: String?,
    val items: List<SaleLine>,
)

data class SaleLine(
    val id: String,
    val productId: String,
    val quantity: Int,
    val unitPrice: String,
    val total: String,
)

data class NewSale(
    val customerId: String,
    val items: List<NewSaleItem>,
    val notes: String? = null,
    /** draft or completed; null lets the backend default apply. */
    val status: String? = null,
)

data class NewSaleItem(
    val productId: String,
    val quantity: Int,
)
