package com.carloserp.android.data.remote.di

import com.carloserp.android.core.network.TokenProvider
import com.carloserp.android.data.remote.token.InMemoryTokenProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Binds network-layer interface implementations (task 49.2).
 *
 * [TokenProvider] is currently backed by the in-memory implementation; task
 * 50.1 repoints this binding to the encrypted, persistent one with no change to
 * consumers (the [com.carloserp.android.data.remote.interceptor.AuthInterceptor]).
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkBindsModule {

    @Binds
    @Singleton
    abstract fun bindTokenProvider(impl: InMemoryTokenProvider): TokenProvider
}
