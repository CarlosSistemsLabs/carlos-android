package com.carloserp.android.core.monitoring

/**
 * Performance-tracing port (task 51.3).
 *
 * Produces lightweight handles for custom traces (e.g. screen load) and network
 * requests. Backed by Firebase Performance; when Firebase isn't configured the
 * returned handles are no-ops, so callers never branch on availability.
 */
interface PerformanceTracer {
    /** Starts a custom trace; call [PerfTrace.stop] when the measured work ends. */
    fun startTrace(name: String): PerfTrace

    /** Starts an HTTP request trace; call [NetworkTrace.stop] once the response is handled. */
    fun startNetworkTrace(url: String, httpMethod: String): NetworkTrace
}

/** A running custom trace. */
interface PerfTrace {
    fun putMetric(name: String, value: Long)
    fun stop()
}

/** A running HTTP request trace. */
interface NetworkTrace {
    fun setRequestPayloadSize(bytes: Long)
    fun setResponseCode(code: Int)
    fun setResponsePayloadSize(bytes: Long)
    fun stop()
}

/** No-op handles used when performance monitoring is unavailable. */
object NoOpPerfTrace : PerfTrace {
    override fun putMetric(name: String, value: Long) = Unit
    override fun stop() = Unit
}

object NoOpNetworkTrace : NetworkTrace {
    override fun setRequestPayloadSize(bytes: Long) = Unit
    override fun setResponseCode(code: Int) = Unit
    override fun setResponsePayloadSize(bytes: Long) = Unit
    override fun stop() = Unit
}
