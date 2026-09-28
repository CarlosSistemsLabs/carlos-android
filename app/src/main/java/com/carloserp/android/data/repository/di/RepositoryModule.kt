package com.carloserp.android.data.repository.di

import com.carloserp.android.data.repository.AuthRepositoryImpl
import com.carloserp.android.data.repository.ProductRepositoryImpl
import com.carloserp.android.domain.repository.AuthRepository
import com.carloserp.android.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Binds domain repository ports to their data-layer implementations (task 49.4).
 *
 * This is the repository-layer DI module (the pattern referenced in task 49.3);
 * each feature adds its `@Binds` here (or in a feature module) as its repository
 * lands. The auth repository was added with the auth flow (task 50.1).
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}
