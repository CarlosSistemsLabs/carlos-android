package com.carloserp.android.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.carloserp.android.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for the product cache (task 49.4).
 *
 * [observeAll] returns a [Flow] so the UI reacts to cache changes automatically
 * (offline-first single source of truth). Writes use `@Upsert` so a sync can
 * insert-or-update in one call.
 */
@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY name")
    fun observeAll(): Flow<List<ProductEntity>>

    @Upsert
    suspend fun upsertAll(products: List<ProductEntity>)

    @Query("DELETE FROM products")
    suspend fun clear()
}
