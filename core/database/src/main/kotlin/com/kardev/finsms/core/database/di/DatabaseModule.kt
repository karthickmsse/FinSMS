package com.kardev.finsms.core.database.di

import android.content.Context
import androidx.room.Room
import com.kardev.finsms.core.database.AppDatabase
import com.kardev.finsms.core.database.dao.AnalyticsDao
import com.kardev.finsms.core.database.dao.CategoryDao
import com.kardev.finsms.core.database.dao.InstrumentDao
import com.kardev.finsms.core.database.dao.MerchantCategoryMapDao
import com.kardev.finsms.core.database.dao.RawSmsDao
import com.kardev.finsms.core.database.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration() // fine pre-1.0; replace with real migrations later
            .build()

    @Provides
    fun provideRawSmsDao(db: AppDatabase): RawSmsDao = db.rawSmsDao()

    @Provides
    fun provideInstrumentDao(db: AppDatabase): InstrumentDao = db.instrumentDao()

    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()

    @Provides
    fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao()

    @Provides
    fun provideMerchantCategoryMapDao(db: AppDatabase): MerchantCategoryMapDao = db.merchantCategoryMapDao()

    @Provides
    fun provideAnalyticsDao(db: AppDatabase): AnalyticsDao = db.analyticsDao()
}
