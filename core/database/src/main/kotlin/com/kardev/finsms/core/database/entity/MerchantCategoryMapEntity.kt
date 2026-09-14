package com.kardev.finsms.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "merchant_category_map")
data class MerchantCategoryMapEntity(
    @PrimaryKey val merchantKeyword: String,
    val categoryId: Long,
    val confidence: Float = 1.0f,
    val updatedAt: Long = System.currentTimeMillis()
)
