package com.carloserp.android.data.repository.di

import com.carloserp.android.data.repository.AuthRepositoryImpl
import com.carloserp.android.data.repository.CustomerRepositoryImpl
import com.carloserp.android.data.repository.DashboardRepositoryImpl
import com.carloserp.android.data.repository.ProductRepositoryImpl
import com.carloserp.android.data.repository.SaleRepositoryImpl
import com.carloserp.android.data.repository.StockRepositoryImpl
import com.carloserp.android.domain.repository.AuthRepository
import com.carloserp.android.domain.repository.CustomerRepository
import com.carloserp.android.domain.repository.DashboardRepository
import com.carloserp.android.domain.repository.ProductRepository
import com.carloserp.android.domain.repository.SaleRepository
import com.carloserp.android.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Binds domain repository ports to their data-layer implementations
 * (tasks 49.4 / 50.1 / 50.4). Each feature adds its `@Binds` here as its
 * repository lands.
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

    @Binds
    @Singleton
    abstract fun bindCustomerRepository(impl: CustomerRepositoryImpl): CustomerRepository

    @Binds
    @Singleton
    abstract fun bindSaleRepository(impl: SaleRepositoryImpl): SaleRepository

    @Binds
    @Singleton
    abstract fun bindStockRepository(impl: StockRepositoryImpl): StockRepository

    @Binds
    @Singleton
    abstract fun bindDashboardRepository(impl: DashboardRepositoryImpl): DashboardRepository
}
