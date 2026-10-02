package com.carloserp.android.domain.model

/**
 * Customer domain model (task 50.4) — framework-free view of the account's
 * customers, mapped from the network DTO.
 */
data class Customer(
    val id: String,
    val name: String,
    val email: String?,
    val phone: String?,
    val taxId: String?,
    val address: String?,
    val notes: String?,
    val isActive: Boolean,
)

/** Input for creating a customer from the app (only [name] is required). */
data class NewCustomer(
    val name: String,
    val email: String? = null,
    val phone: String? = null,
    val taxId: String? = null,
    val address: String? = null,
    val notes: String? = null,
)
