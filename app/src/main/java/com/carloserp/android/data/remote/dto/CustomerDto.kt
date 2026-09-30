package com.carloserp.android.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Customer as returned by `GET /customers` and `GET /customers/:id` (task 50.4).
 * Field names/types mirror the backend; `tenantId` is accepted but ignored.
 */
@Serializable
data class CustomerDto(
    val id: String,
    val name: String,
    val email: String? = null,
    val phone: String? = null,
    val taxId: String? = null,
    val address: String? = null,
    val notes: String? = null,
    val isActive: Boolean = true,
)
