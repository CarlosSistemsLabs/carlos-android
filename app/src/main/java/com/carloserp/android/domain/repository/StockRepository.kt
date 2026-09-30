package com.carloserp.android.domain.repository

import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.domain.model.StockLevel

/**
 * Stock repository port (task 50.4). [getStockLevels] returns current on-hand
 * levels (each already flagged for low stock); [getLowStock] returns only the
 * levels at or below their minimum.
 */
interface StockRepository {
    suspend fun getStockLevels(): ApiResult<List<StockLevel>>

    suspend fun getLowStock(): ApiResult<List<StockLevel>>
}
