package com.carloserp.android.core.network

/**
 * Result of a network call (task 49.2): either a decoded [Success] value or a
 * typed [Failure]. Keeps the transport's success/error union explicit so the
 * data/domain layers never see raw exceptions.
 */
sealed interface ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>

    data class Failure(val error: NetworkError) : ApiResult<Nothing>
}

/** Maps the success value, preserving a [Failure] unchanged. */
inline fun <T, R> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> =
    when (this) {
        is ApiResult.Success -> ApiResult.Success(transform(data))
        is ApiResult.Failure -> this
    }

/** Returns the value on success, or `null` on failure. */
fun <T> ApiResult<T>.getOrNull(): T? = (this as? ApiResult.Success)?.data
