package com.carloserp.android.data.repository

import com.carloserp.android.core.di.IoDispatcher
import com.carloserp.android.core.network.ApiResult
import com.carloserp.android.core.network.map
import com.carloserp.android.core.network.safeApiCall
import com.carloserp.android.data.remote.api.AddressApi
import com.carloserp.android.data.remote.mapper.toDomain
import com.carloserp.android.domain.model.AddressSuggestion
import com.carloserp.android.domain.model.GeocodedAddress
import com.carloserp.android.domain.repository.AddressRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [AddressRepository] backed by the addresses API. Wraps calls in [safeApiCall]
 * and maps DTOs to domain models on the IO dispatcher. [locate] collapses the
 * returned array to its first usable coordinate.
 */
@Singleton
class AddressRepositoryImpl @Inject constructor(
    private val addressApi: AddressApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : AddressRepository {

    override suspend fun autocomplete(query: String): ApiResult<List<AddressSuggestion>> =
        withContext(ioDispatcher) {
            safeApiCall { addressApi.autocomplete(query) }
                .map { suggestions -> suggestions.map { it.toDomain() } }
        }

    override suspend fun locate(id: String): ApiResult<GeocodedAddress?> =
        withContext(ioDispatcher) {
            safeApiCall { addressApi.locate(id) }
                .map { addresses -> addresses.firstNotNullOfOrNull { it.toDomain() } }
        }
}
