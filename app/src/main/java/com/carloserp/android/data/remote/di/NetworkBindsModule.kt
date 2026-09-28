package com.carloserp.android.data.remote.di

import com.carloserp.android.core.auth.SessionStore
import com.carloserp.android.core.network.TokenProvider
import com.carloserp.android.data.remote.token.EncryptedTokenStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Binds network/auth-layer interface implementations (tasks 49.2 / 50.1).
 *
 * The single [EncryptedTokenStore] singleton backs both the wide [SessionStore]
 * (used by the auth flow to persist/clear tokens) and the narrow [TokenProvider]
 * (read by the [com.carloserp.android.data.remote.interceptor.AuthInterceptor]),
 * replacing the in-memory provider from task 49.2. Both binds resolve to the
 * same instance, so a login persisted through [SessionStore] is immediately
 * visible to the interceptor.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkBindsModule {

    @Binds
    @Singleton
    abstract fun bindSessionStore(impl: EncryptedTokenStore): SessionStore

    @Binds
    @Singleton
    abstract fun bindTokenProvider(impl: EncryptedTokenStore): TokenProvider
}
