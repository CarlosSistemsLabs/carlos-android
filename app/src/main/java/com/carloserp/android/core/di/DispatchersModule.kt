package com.carloserp.android.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Provides the app's [CoroutineDispatcher]s via Hilt (task 49.3).
 *
 * Consumers inject the one they need with the matching qualifier, e.g.:
 * ```kotlin
 * class SomeRepository @Inject constructor(
 *     @IoDispatcher private val io: CoroutineDispatcher,
 * )
 * ```
 * This is the DI foundation the data/domain layers build on; per-feature
 * repository and use-case bindings (typically `@Binds` in feature `@Module`s)
 * are added with their feature in tasks 50.x.
 */
@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}
