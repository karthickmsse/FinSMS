package com.kardev.finsms.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val categoryId: Long = 0,
    val name: String,
    val icon: String? = null,
    val isDefault: Boolean = false,
    val parentCategoryId: Long? = null
)
