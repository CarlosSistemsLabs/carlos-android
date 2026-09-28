package com.carloserp.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.carloserp.android.data.local.dao.ProductDao
import com.carloserp.android.data.local.entity.ProductEntity

/**
 * Room database (task 49.4).
 *
 * Starts with the product cache; feature tasks add their entities/DAOs and bump
 * [DATABASE_VERSION] with a migration. `exportSchema` is disabled for now (no
 * schema history is tracked yet) — enable it and add a schema directory before
 * shipping migrations.
 */
@Database(
    entities = [ProductEntity::class],
    version = CarlosDatabase.DATABASE_VERSION,
    exportSchema = false,
)
abstract class CarlosDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "carlos.db"
    }
}
