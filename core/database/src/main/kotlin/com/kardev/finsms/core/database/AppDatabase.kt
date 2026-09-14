package com.kardev.finsms.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kardev.finsms.core.database.dao.AnalyticsDao
import com.kardev.finsms.core.database.dao.CategoryDao
import com.kardev.finsms.core.database.dao.InstrumentDao
import com.kardev.finsms.core.database.dao.MerchantCategoryMapDao
import com.kardev.finsms.core.database.dao.RawSmsDao
import com.kardev.finsms.core.database.dao.TransactionDao
import com.kardev.finsms.core.database.entity.CategoryEntity
import com.kardev.finsms.core.database.entity.InstrumentEntity
import com.kardev.finsms.core.database.entity.MerchantCategoryMapEntity
import com.kardev.finsms.core.database.entity.RawSmsEntity
import com.kardev.finsms.core.database.entity.TransactionEntity

@Database(
    entities = [
        RawSmsEntity::class,
        InstrumentEntity::class,
        CategoryEntity::class,
        TransactionEntity::class,
        MerchantCategoryMapEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rawSmsDao(): RawSmsDao
    abstract fun instrumentDao(): InstrumentDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun merchantCategoryMapDao(): MerchantCategoryMapDao
    abstract fun analyticsDao(): AnalyticsDao

    companion object {
        const val DATABASE_NAME = "finsms.db"
    }
}
