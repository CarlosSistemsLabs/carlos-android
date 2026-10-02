package com.carloserp.android.domain.repository

import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.domain.model.Category
import com.carloserp.android.domain.model.NewProduct
import com.carloserp.android.domain.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * Product repository port (tasks 49.4 / 50.4) — offline-first.
 *
 * The local Room cache is the **single source of truth**: the UI observes
 * [observeProducts] (a cold [Flow] that emits on every DB change), while
 * [refreshProducts] fetches from the backend and replaces the cache. Detail
 * lookups go straight to the API via [getProduct]. Keeping the port in the
 * domain layer lets the presentation layer depend on this abstraction, not Room.
 */
interface ProductRepository {
    /** Streams the cached products, re-emitting whenever the cache changes. */
    fun observeProducts(): Flow<List<Product>>

    /** Fetches the latest products from the backend and replaces the local cache. */
    suspend fun refreshProducts(): ApiResult<Unit>

    /** Fetches a single product by id from the backend. */
    suspend fun getProduct(id: String): ApiResult<Product>

    /** Lists the tenant's product categories (for the create-product form). */
    suspend fun getCategories(): ApiResult<List<Category>>

    /** Creates a new category and returns it. */
    suspend fun createCategory(name: String): ApiResult<Category>

    /** Creates a new product and refreshes the local cache. */
    suspend fun createProduct(product: NewProduct): ApiResult<Product>

    /** Clears the local product cache (e.g. on sign-out or tenant switch). */
    suspend fun clear()
}
