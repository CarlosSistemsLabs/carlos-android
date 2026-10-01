package com.carloserp.android.core.crash

/**
 * Crash-reporting port (task 51.3).
 *
 * Lets the app attach user context and record non-fatal errors/log breadcrumbs
 * without depending on Crashlytics directly. Backed by the Firebase
 * implementation, which no-ops when Firebase isn't configured.
 */
interface CrashReporter {
    /** Attaches the signed-in user/tenant to subsequent crash reports (null clears). */
    fun setUser(userId: String?, tenantId: String?)

    /** Adds a breadcrumb log line to the next crash report. */
    fun log(message: String)

    /** Records a non-fatal exception. */
    fun recordException(throwable: Throwable)
}
