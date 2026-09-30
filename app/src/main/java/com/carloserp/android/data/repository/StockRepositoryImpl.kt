package com.carloserp.android.data.repository

import com.carloserp.android.core.di.IoDispatcher
import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.core.network.map
import com.carloserp.android.core.network.safeApiCall
import com.carloserp.android.data.remote.api.StockApi
import com.carloserp.android.data.remote.mapper.toDomain
import com.carloserp.android.domain.model.StockLevel
import com.carloserp.android.domain.repository.StockRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [StockRepository] backed by the stock API (task 50.4). Wraps calls in
 * [safeApiCall] and maps DTOs to domain models on the IO dispatcher.
 */
@Singleton
class StockRepositoryImpl @Inject constructor(
    private val stockApi: StockApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : StockRepository {

    override suspend fun getStockLevels(): ApiResult<List<StockLevel>> =
        withContext(ioDispatcher) {
            safeApiCall { stockApi.list(page = 1, pageSize = PAGE_SIZE) }
                .map { paged -> paged.items.map { it.toDomain() } }
        }

    override suspend fun getLowStock(): ApiResult<List<StockLevel>> =
        withContext(ioDispatcher) {
            safeApiCall { stockApi.alerts(page = 1, pageSize = PAGE_SIZE) }
                .map { paged -> paged.items.map { it.toDomain() } }
        }

    private companion object {
        const val PAGE_SIZE = 100
    }
}
