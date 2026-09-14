package com.kardev.finsms.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kardev.finsms.core.database.entity.MerchantCategoryMapEntity

@Dao
interface MerchantCategoryMapDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: MerchantCategoryMapEntity)

    @Query("SELECT * FROM merchant_category_map WHERE merchantKeyword = :keyword LIMIT 1")
    suspend fun findByKeyword(keyword: String): MerchantCategoryMapEntity?
}
