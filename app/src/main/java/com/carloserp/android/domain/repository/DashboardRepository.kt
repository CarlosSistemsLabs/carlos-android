package com.carloserp.android.domain.repository

import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.domain.model.DashboardMetrics

/**
 * Dashboard repository port (task 50.4). [getMetrics] assembles the dashboard
 * figures from the backend's report endpoints in one call.
 */
interface DashboardRepository {
    suspend fun getMetrics(): ApiResult<DashboardMetrics>
}
