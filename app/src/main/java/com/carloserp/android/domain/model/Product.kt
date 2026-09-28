package com.carloserp.android.domain.model

/**
 * Product domain model (task 49.4).
 *
 * Framework-free representation used by the UI/domain layers. Money is kept as
 * integer cents ([priceCents]) to avoid floating-point rounding, mirroring the
 * backend's Money value object. This is the minimal shape needed to prepare the
 * offline cache; the full product feature (task 50.4) extends it as needed.
 */
data class Product(
    val id: String,
    val name: String,
    val sku: String,
    val priceCents: Long,
    /** Epoch millis of the last known update (used for cache freshness). */
    val updatedAt: Long,
)
