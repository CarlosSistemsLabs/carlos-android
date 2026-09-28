package com.carloserp.android.domain.repository

import com.carloserp.android.domain.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * Product repository port (task 49.4) — offline-first.
 *
 * The local Room cache is the **single source of truth**: the UI observes
 * [observeProducts] (a cold [Flow] that emits on every DB change), while a sync
 * step (added with the products feature, task 50.4) fetches from the backend and
 * writes into the cache via [cacheProducts]. Keeping the port in the domain
 * layer lets the presentation layer depend on this abstraction, not on Room.
 */
interface ProductRepository {
    /** Streams the cached products, re-emitting whenever the cache changes. */
    fun observeProducts(): Flow<List<Product>>

    /** Inserts/updates the given products in the local cache (single source of truth). */
    suspend fun cacheProducts(products: List<Product>)

    /** Clears the local product cache (e.g. on sign-out or tenant switch). */
    suspend fun clear()
}
