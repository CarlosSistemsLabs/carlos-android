package com.carloserp.android.core.network

/**
 * Typed network failure (task 49.2).
 *
 * A closed set of the failures the app can recover from / present to the user,
 * so callers `when`-branch exhaustively instead of inspecting raw exceptions.
 * Mapped from transport exceptions by [safeApiCall].
 */
sealed interface NetworkError {
    /** No connectivity / socket/timeout error (no HTTP response received). */
    data object Connection : NetworkError

    /** A non-2xx HTTP response. [code] is the status; [body] the raw error body. */
    data class Http(val code: Int, val body: String?) : NetworkError

    /** The response body could not be decoded into the expected shape. */
    data object Serialization : NetworkError

    /** Anything else. [message] is best-effort for logging. */
    data class Unknown(val message: String?) : NetworkError
}

/** `true` for HTTP 401 — used by callers to trigger re-authentication. */
val NetworkError.isUnauthorized: Boolean
    get() = this is NetworkError.Http && code == 401
