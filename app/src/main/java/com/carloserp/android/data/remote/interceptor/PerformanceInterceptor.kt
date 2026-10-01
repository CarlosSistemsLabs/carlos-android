package com.carloserp.android.data.remote.interceptor

import com.carloserp.android.core.monitoring.PerformanceTracer
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * OkHttp interceptor that times each request via the [PerformanceTracer] (task 51.3).
 *
 * Wraps every call in a network trace, recording the HTTP method/URL, request &
 * response payload sizes, and the response code. The trace is always stopped
 * (even on transport failure). No-ops transparently when Firebase Performance is
 * unavailable.
 */
class PerformanceInterceptor @Inject constructor(
    private val performanceTracer: PerformanceTracer,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val trace = performanceTracer.startNetworkTrace(request.url.toString(), request.method)
        request.body?.contentLength()?.takeIf { it >= 0 }?.let(trace::setRequestPayloadSize)

        try {
            val response = chain.proceed(request)
            trace.setResponseCode(response.code)
            response.body?.contentLength()?.takeIf { it >= 0 }?.let(trace::setResponsePayloadSize)
            return response
        } finally {
            trace.stop()
        }
    }
}
