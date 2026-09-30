package com.carloserp.android.data.repository

import com.carloserp.android.core.di.IoDispatcher
import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.core.network.map
import com.carloserp.android.core.network.safeApiCall
import com.carloserp.android.data.local.dao.ProductDao
import com.carloserp.android.data.local.mapper.toDomain
import com.carloserp.android.data.local.mapper.toEntity
import com.carloserp.android.data.remote.api.ProductApi
import com.carloserp.android.data.remote.mapper.toDomain as dtoToDomain
import com.carloserp.android.domain.model.Product
import com.carloserp.android.domain.repository.ProductRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Offline-first [ProductRepository] backed by Room + the products API (task 50.4).
 *
 * The Room cache is the single source of truth: [observeProducts] maps the
 * DAO's [Flow] of entities to domain models, so the UI updates automatically.
 * [refreshProducts] pulls the first page from the backend and replaces the
 * cache; [getProduct] reads a single product directly from the API. All I/O
 * runs on the injected dispatcher.
 */
@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val productApi: ProductApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ProductRepository {

    override fun observeProducts(): Flow<List<Product>> =
        productDao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun refreshProducts(): ApiResult<Unit> =
        withContext(ioDispatcher) {
            when (val result = safeApiCall { productApi.list(page = 1, pageSize = PAGE_SIZE, isActive = true) }) {
                is ApiResult.Success -> {
                    val products = result.data.items.map { it.dtoToDomain().toEntity() }
                    productDao.clear()
                    productDao.upsertAll(products)
                    ApiResult.Success(Unit)
                }

                is ApiResult.Failure -> result
            }
        }

    override suspend fun getProduct(id: String): ApiResult<Product> =
        withContext(ioDispatcher) {
            safeApiCall { productApi.get(id) }.map { it.dtoToDomain() }
        }

    override suspend fun clear() =
        withContext(ioDispatcher) {
            productDao.clear()
        }

    private companion object {
        const val PAGE_SIZE = 100
    }
}
