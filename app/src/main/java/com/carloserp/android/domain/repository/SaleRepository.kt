package com.carloserp.android.domain.repository

import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.domain.model.NewSale
import com.carloserp.android.domain.model.Sale

/**
 * Sale repository port (task 50.4).
 *
 * [getSales] lists recent sales; [createSale] posts a new sale (server computes
 * totals) and returns the created record.
 */
interface SaleRepository {
    suspend fun getSales(): ApiResult<List<Sale>>

    suspend fun createSale(sale: NewSale): ApiResult<Sale>
}
