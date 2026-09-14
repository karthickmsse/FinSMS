package com.kardev.finsms.core.database

import com.kardev.finsms.core.database.dao.CategoryDao
import com.kardev.finsms.core.database.entity.CategoryEntity

object DefaultCategories {
    val SEED_CATEGORIES = listOf(
        "Food & Dining", "Groceries", "Transport", "Shopping",
        "Bills & Utilities", "Entertainment", "Health & Wellness",
        "Travel", "Rent", "Investments", "Transfers", "Salary/Income",
        "Subscriptions", "Fuel", "Education", "Uncategorized"
    )

    suspend fun seedIfEmpty(categoryDao: CategoryDao) {
        if (categoryDao.count() > 0) return
        categoryDao.insertAll(SEED_CATEGORIES.map { CategoryEntity(name = it, isDefault = true) })
    }
}
