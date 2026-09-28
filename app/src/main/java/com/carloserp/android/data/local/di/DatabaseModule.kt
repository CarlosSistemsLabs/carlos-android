package com.carloserp.android.data.local.di

import android.content.Context
import androidx.room.Room
import com.carloserp.android.data.local.CarlosDatabase
import com.carloserp.android.data.local.dao.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing the Room database + DAOs (task 49.4).
 *
 * `fallbackToDestructiveMigration()` is acceptable pre-release (the cache is
 * rebuildable from the backend); real migrations + `exportSchema` are added
 * before shipping. DAOs are provided so repositories inject them directly.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CarlosDatabase =
        Room.databaseBuilder(
            context,
            CarlosDatabase::class.java,
            CarlosDatabase.DATABASE_NAME,
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideProductDao(database: CarlosDatabase): ProductDao = database.productDao()
}
