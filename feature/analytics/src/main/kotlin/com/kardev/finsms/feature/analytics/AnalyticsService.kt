package com.kardev.finsms.feature.analytics

import com.kardev.finsms.core.database.dao.AnalyticsDao
import com.kardev.finsms.core.database.dao.CategorySpend
import com.kardev.finsms.core.database.dao.InstrumentSpend
import com.kardev.finsms.core.database.dao.MerchantSpend
import com.kardev.finsms.core.database.dao.MonthlyTrend
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class MonthSummary(val spend: Double, val income: Double, val net: Double)

class AnalyticsService @Inject constructor(
    private val analyticsDao: AnalyticsDao
) {
    fun getCurrentMonthSummary(): Flow<MonthSummary> {
        val (start, end) = DateRangeUtil.currentMonthRange()
        return combine(
            analyticsDao.getTotalSpend(start, end),
            analyticsDao.getTotalIncome(start, end)
        ) { spend, income ->
            val s = spend ?: 0.0
            val i = income ?: 0.0
            MonthSummary(spend = s, income = i, net = i - s)
        }
    }

    fun getCategoryBreakdown(range: DateRange): Flow<List<CategorySpend>> {
        val (start, end) = range.toMillis()
        return analyticsDao.getCategoryBreakdown(start, end)
    }

    fun getTrend(months: Int = 6): Flow<List<MonthlyTrend>> {
        val (start, end) = DateRangeUtil.lastNMonthsRange(months)
        return analyticsDao.getMonthlyTrend(start, end)
    }

    fun getInstrumentBreakdown(range: DateRange): Flow<List<InstrumentSpend>> {
        val (start, end) = range.toMillis()
        return analyticsDao.getSpendByInstrument(start, end)
    }

    fun getTopMerchants(range: DateRange, limit: Int = 10): Flow<List<MerchantSpend>> {
        val (start, end) = range.toMillis()
        return analyticsDao.getTopMerchants(start, end, limit)
    }
}
