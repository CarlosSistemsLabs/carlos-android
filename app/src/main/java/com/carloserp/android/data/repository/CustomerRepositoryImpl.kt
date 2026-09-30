package com.carloserp.android.data.repository

import com.carloserp.android.core.di.IoDispatcher
import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.core.network.map
import com.carloserp.android.core.network.safeApiCall
import com.carloserp.android.data.remote.api.CustomerApi
import com.carloserp.android.data.remote.mapper.toDomain
import com.carloserp.android.domain.model.Customer
import com.carloserp.android.domain.repository.CustomerRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [CustomerRepository] backed by the customers API (task 50.4). Wraps calls in
 * [safeApiCall] and maps DTOs to domain models on the IO dispatcher.
 */
@Singleton
class CustomerRepositoryImpl @Inject constructor(
    private val customerApi: CustomerApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : CustomerRepository {

    override suspend fun getCustomers(): ApiResult<List<Customer>> =
        withContext(ioDispatcher) {
            safeApiCall { customerApi.list(page = 1, pageSize = PAGE_SIZE, isActive = true) }
                .map { paged -> paged.items.map { it.toDomain() } }
        }

    override suspend fun getCustomer(id: String): ApiResult<Customer> =
        withContext(ioDispatcher) {
            safeApiCall { customerApi.get(id) }.map { it.toDomain() }
        }

    private companion object {
        const val PAGE_SIZE = 100
    }
}
