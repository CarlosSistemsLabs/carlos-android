package com.carloserp.android.core.analytics

/**
 * App analytics port (task 51.2).
 *
 * A framework-free surface the UI/features depend on, so screens log events
 * without knowing about Firebase. Backed by the Firebase implementation, which
 * degrades to a no-op when Firebase isn't configured.
 */
interface AnalyticsService {
    /** Logs a screen view with a stable [screenName]. */
    fun logScreenView(screenName: String)

    /** Logs a named event with optional string [params]. */
    fun logEvent(name: String, params: Map<String, String> = emptyMap())

    /** Associates subsequent events with the signed-in user/tenant (null clears). */
    fun setUser(userId: String?, tenantId: String?)
}
