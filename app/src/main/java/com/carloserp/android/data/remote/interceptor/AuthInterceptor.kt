package com.carloserp.android.data.remote.interceptor

import com.carloserp.android.core.network.TokenProvider
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * OkHttp interceptor that attaches the JWT bearer token (task 49.2).
 *
 * Reads the current token from [TokenProvider] on every request and adds
 * `Authorization: Bearer <token>` when one is present and the caller has not
 * already set the header. Requests made while signed out simply go through
 * without the header (public endpoints like login/register).
 */
class AuthInterceptor @Inject constructor(
    private val tokenProvider: TokenProvider,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = tokenProvider.accessToken()

        val authorized = if (token != null && request.header(HEADER) == null) {
            request.newBuilder().addHeader(HEADER, "$PREFIX$token").build()
        } else {
            request
        }
        return chain.proceed(authorized)
    }

    private companion object {
        const val HEADER = "Authorization"
        const val PREFIX = "Bearer "
    }
}
