package com.carloserp.android.data.repository

import com.carloserp.android.core.di.IoDispatcher
import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.core.network.safeApiCall
import com.carloserp.android.data.remote.api.ReportApi
import com.carloserp.android.domain.model.DashboardMetrics
import com.carloserp.android.domain.repository.DashboardRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [DashboardRepository] that composes the dashboard metrics from the sales and
 * stock reports (task 50.4). Both calls must succeed; the first failure is
 * returned so the screen can show a single error with retry. Runs on the IO
 * dispatcher.
 */
@Singleton
class DashboardRepositoryImpl @Inject constructor(
    private val reportApi: ReportApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : DashboardRepository {

    override suspend fun getMetrics(): ApiResult<DashboardMetrics> =
        withContext(ioDispatcher) {
            val salesResult = safeApiCall { reportApi.salesReport() }
            if (salesResult is ApiResult.Failure) return@withContext salesResult

            val stockResult = safeApiCall { reportApi.stockReport() }
            if (stockResult is ApiResult.Failure) return@withContext stockResult

            val sales = (salesResult as ApiResult.Success).data
            val stock = (stockResult as ApiResult.Success).data
            ApiResult.Success(
                DashboardMetrics(
                    salesCount = sales.totals.count,
                    salesTotal = sales.totals.total,
                    totalProducts = stock.totalItems,
                    lowStockCount = stock.lowStockCount,
                ),
            )
        }
}
