package com.kardev.finsms.feature.smsingest

import com.kardev.finsms.core.common.Direction
import com.kardev.finsms.core.database.dao.CategoryDao
import com.kardev.finsms.core.database.dao.MerchantCategoryMapDao
import com.kardev.finsms.core.database.entity.MerchantCategoryMapEntity
import javax.inject.Inject

class CategorizationEngine @Inject constructor(
    private val merchantMapDao: MerchantCategoryMapDao,
    private val categoryDao: CategoryDao
) {
    suspend fun categorize(merchant: String?, direction: Direction): Long? {
        if (merchant.isNullOrBlank()) {
            return if (direction == Direction.CREDIT) {
                categoryDao.findByName("Salary/Income")?.categoryId
            } else null
        }

        val normalized = normalize(merchant)

        merchantMapDao.findByKeyword(normalized)?.let { return it.categoryId }

        val ruleMatch = MerchantKeywordRules.RULES.entries.firstOrNull { (keyword, _) ->
            normalized.contains(keyword)
        }
        if (ruleMatch != null) {
            val categoryId = categoryDao.findByName(ruleMatch.value)?.categoryId
            if (categoryId != null) {
                merchantMapDao.insert(
                    MerchantCategoryMapEntity(
                        merchantKeyword = normalized,
                        categoryId = categoryId,
                        confidence = 0.7f
                    )
                )
            }
            return categoryId
        }
        return null
    }

    suspend fun learnFromUserCorrection(merchant: String, categoryId: Long) {
        val normalized = normalize(merchant)
        merchantMapDao.insert(
            MerchantCategoryMapEntity(
                merchantKeyword = normalized,
                categoryId = categoryId,
                confidence = 1.0f
            )
        )
    }

    private fun normalize(merchant: String): String =
        merchant.lowercase().trim().replace(Regex("[^a-z0-9 ]"), "")
}
