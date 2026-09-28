package com.carloserp.android.core.network

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

/**
 * Runs a suspending network [block] and maps transport exceptions to a typed
 * [ApiResult] (task 49.2). Repositories wrap their Retrofit calls with this so
 * they return `ApiResult` instead of throwing:
 *
 * ```kotlin
 * suspend fun login(req: LoginRequest) = safeApiCall { api.login(req) }
 * ```
 */
@Suppress("TooGenericExceptionCaught")
suspend inline fun <T> safeApiCall(block: () -> T): ApiResult<T> =
    try {
        ApiResult.Success(block())
    } catch (e: HttpException) {
        ApiResult.Failure(NetworkError.Http(e.code(), e.response()?.errorBody()?.string()))
    } catch (e: IOException) {
        ApiResult.Failure(NetworkError.Connection)
    } catch (e: SerializationException) {
        ApiResult.Failure(NetworkError.Serialization)
    } catch (e: Exception) {
        ApiResult.Failure(NetworkError.Unknown(e.message))
    }
