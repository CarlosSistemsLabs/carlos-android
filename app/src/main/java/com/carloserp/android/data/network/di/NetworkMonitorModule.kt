package com.carloserp.android.data.network.di

import com.carloserp.android.core.network.NetworkMonitor
import com.carloserp.android.data.network.ConnectivityNetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Binds the [NetworkMonitor] port to its [ConnectivityManager]-backed
 * implementation (task 50.5).
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkMonitorModule {

    @Binds
    @Singleton
    abstract fun bindNetworkMonitor(impl: ConnectivityNetworkMonitor): NetworkMonitor
}
