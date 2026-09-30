package com.carloserp.android.data.repository

import com.carloserp.android.core.di.IoDispatcher
import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.core.network.map
import com.carloserp.android.core.network.safeApiCall
import com.carloserp.android.data.remote.api.SaleApi
import com.carloserp.android.data.remote.mapper.toDomain
import com.carloserp.android.data.remote.mapper.toDto
import com.carloserp.android.domain.model.NewSale
import com.carloserp.android.domain.model.Sale
import com.carloserp.android.domain.repository.SaleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [SaleRepository] backed by the sales API (task 50.4). Wraps calls in
 * [safeApiCall] and maps DTOs to/from domain models on the IO dispatcher.
 */
@Singleton
class SaleRepositoryImpl @Inject constructor(
    private val saleApi: SaleApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : SaleRepository {

    override suspend fun getSales(): ApiResult<List<Sale>> =
        withContext(ioDispatcher) {
            safeApiCall { saleApi.list(page = 1, pageSize = PAGE_SIZE) }
                .map { paged -> paged.items.map { it.toDomain() } }
        }

    override suspend fun createSale(sale: NewSale): ApiResult<Sale> =
        withContext(ioDispatcher) {
            safeApiCall { saleApi.create(sale.toDto()) }.map { it.toDomain() }
        }

    private companion object {
        const val PAGE_SIZE = 50
    }
}
