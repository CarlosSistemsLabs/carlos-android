package com.carloserp.android.data.repository

import com.carloserp.android.core.di.IoDispatcher
import com.carloserp.android.data.local.dao.ProductDao
import com.carloserp.android.data.local.mapper.toDomain
import com.carloserp.android.data.local.mapper.toEntity
import com.carloserp.android.domain.model.Product
import com.carloserp.android.domain.repository.ProductRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Offline-first [ProductRepository] backed by Room (task 49.4).
 *
 * The Room cache is the single source of truth: [observeProducts] maps the
 * DAO's [Flow] of entities to domain models, so the UI updates automatically
 * when the cache changes. Write operations run on the injected IO dispatcher.
 * A remote fetch that populates the cache (via [cacheProducts]) is wired in the
 * products feature (task 50.4).
 */
@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ProductRepository {

    override fun observeProducts(): Flow<List<Product>> =
        productDao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun cacheProducts(products: List<Product>) =
        withContext(ioDispatcher) {
            productDao.upsertAll(products.map { it.toEntity() })
        }

    override suspend fun clear() =
        withContext(ioDispatcher) {
            productDao.clear()
        }
}
