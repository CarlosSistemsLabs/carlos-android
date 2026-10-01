package com.carloserp.android.data.observability.di

import com.carloserp.android.core.crash.CrashReporter
import com.carloserp.android.core.monitoring.PerformanceTracer
import com.carloserp.android.data.crash.FirebaseCrashReporter
import com.carloserp.android.data.monitoring.FirebasePerformanceTracer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Binds the crash-reporting and performance-tracing ports to their Firebase
 * implementations (task 51.3).
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ObservabilityModule {

    @Binds
    @Singleton
    abstract fun bindCrashReporter(impl: FirebaseCrashReporter): CrashReporter

    @Binds
    @Singleton
    abstract fun bindPerformanceTracer(impl: FirebasePerformanceTracer): PerformanceTracer
}
