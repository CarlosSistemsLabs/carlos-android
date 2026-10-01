package com.carloserp.android.data.monitoring

import android.content.Context
import com.carloserp.android.core.firebase.isFirebaseAvailable
import com.carloserp.android.core.monitoring.NetworkTrace
import com.carloserp.android.core.monitoring.NoOpNetworkTrace
import com.carloserp.android.core.monitoring.NoOpPerfTrace
import com.carloserp.android.core.monitoring.PerfTrace
import com.carloserp.android.core.monitoring.PerformanceTracer
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.HttpMetric
import com.google.firebase.perf.metrics.Trace
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [PerformanceTracer] backed by Firebase Performance Monitoring (task 51.3).
 *
 * Starts real [Trace]/[HttpMetric] objects when Firebase is configured; returns
 * the shared no-op handles otherwise, so screens and the network interceptor
 * trace unconditionally with zero cost when Firebase is absent.
 */
@Singleton
class FirebasePerformanceTracer @Inject constructor(
    @ApplicationContext context: Context,
) : PerformanceTracer {

    private val performance: FirebasePerformance? =
        if (isFirebaseAvailable(context)) FirebasePerformance.getInstance() else null

    override fun startTrace(name: String): PerfTrace {
        val trace = performance?.newTrace(name)?.apply { start() } ?: return NoOpPerfTrace
        return FirebasePerfTrace(trace)
    }

    override fun startNetworkTrace(url: String, httpMethod: String): NetworkTrace {
        val metric = performance?.newHttpMetric(url, httpMethod.toFirebaseHttpMethod())?.apply { start() }
            ?: return NoOpNetworkTrace
        return FirebaseNetworkTrace(metric)
    }

    private fun String.toFirebaseHttpMethod(): String = when (uppercase()) {
        "GET" -> FirebasePerformance.HttpMethod.GET
        "POST" -> FirebasePerformance.HttpMethod.POST
        "PUT" -> FirebasePerformance.HttpMethod.PUT
        "DELETE" -> FirebasePerformance.HttpMethod.DELETE
        "PATCH" -> FirebasePerformance.HttpMethod.PATCH
        "HEAD" -> FirebasePerformance.HttpMethod.HEAD
        "OPTIONS" -> FirebasePerformance.HttpMethod.OPTIONS
        else -> FirebasePerformance.HttpMethod.GET
    }
}

private class FirebasePerfTrace(private val trace: Trace) : PerfTrace {
    override fun putMetric(name: String, value: Long) = trace.putMetric(name, value)
    override fun stop() = trace.stop()
}

private class FirebaseNetworkTrace(private val metric: HttpMetric) : NetworkTrace {
    override fun setRequestPayloadSize(bytes: Long) = metric.setRequestPayloadSize(bytes)
    override fun setResponseCode(code: Int) = metric.setHttpResponseCode(code)
    override fun setResponsePayloadSize(bytes: Long) = metric.setResponsePayloadSize(bytes)
    override fun stop() = metric.stop()
}
