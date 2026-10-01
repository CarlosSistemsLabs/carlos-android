package com.carloserp.android.data.analytics.di

import com.carloserp.android.core.analytics.AnalyticsService
import com.carloserp.android.data.analytics.FirebaseAnalyticsService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Binds the [AnalyticsService] port to its Firebase implementation (task 51.2). */
@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    @Singleton
    abstract fun bindAnalyticsService(impl: FirebaseAnalyticsService): AnalyticsService
}
