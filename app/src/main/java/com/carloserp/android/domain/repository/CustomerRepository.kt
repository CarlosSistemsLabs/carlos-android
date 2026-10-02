package com.carloserp.android.domain.repository

import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.domain.model.Customer
import com.carloserp.android.domain.model.NewCustomer

/**
 * Customer repository port (task 50.4).
 *
 * Read-only for the mobile client: [getCustomers] fetches the first page and
 * [getCustomer] a single record. No local cache yet (unlike products); the list
 * is fetched on demand.
 */
interface CustomerRepository {
    suspend fun getCustomers(): ApiResult<List<Customer>>

    suspend fun getCustomer(id: String): ApiResult<Customer>

    suspend fun createCustomer(customer: NewCustomer): ApiResult<Customer>
}
