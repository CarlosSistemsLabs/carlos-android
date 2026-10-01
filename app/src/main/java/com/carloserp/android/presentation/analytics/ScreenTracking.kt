package com.carloserp.android.presentation.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.carloserp.android.core.analytics.AnalyticsService
import com.carloserp.android.core.monitoring.PerformanceTracer
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

/**
 * Hilt entry point for pulling app-scoped observability services into
 * composables that don't own a ViewModel (task 51.2 / 51.3).
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface ObservabilityEntryPoint {
    fun analyticsService(): AnalyticsService
    fun performanceTracer(): PerformanceTracer
}

/**
 * Logs a screen view and measures its initial render time (tasks 51.2 / 51.3).
 *
 * On first composition for [screenName] it logs an analytics `screen_view` and
 * starts a `screen_load_<name>` performance trace that stops once the first
 * composition settles (the `LaunchedEffect` body runs after composition), giving
 * an approximate screen-load time. Both degrade to no-ops without Firebase.
 */
@Composable
fun TrackScreenView(screenName: String) {
    val context = LocalContext.current
    LaunchedEffect(screenName) {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            ObservabilityEntryPoint::class.java,
        )
        val trace = entryPoint.performanceTracer().startTrace("screen_load_$screenName")
        entryPoint.analyticsService().logScreenView(screenName)
        trace.stop()
    }
}
