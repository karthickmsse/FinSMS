package com.kardev.finsms.feature.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kardev.finsms.core.database.dao.CategorySpend
import com.kardev.finsms.core.database.dao.InstrumentSpend
import com.kardev.finsms.core.database.dao.MerchantSpend
import com.kardev.finsms.core.database.dao.MonthlyTrend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val analyticsService: AnalyticsService
) : ViewModel() {

    private val currentMonthRange: DateRange
        get() = DateRangeUtil.currentMonthRange().let { DateRange.Custom(it.first, it.second) }

    val monthSummary = analyticsService.getCurrentMonthSummary()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MonthSummary(0.0, 0.0, 0.0))

    val categoryBreakdown = analyticsService.getCategoryBreakdown(currentMonthRange)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList<CategorySpend>())

    val instrumentBreakdown = analyticsService.getInstrumentBreakdown(currentMonthRange)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList<InstrumentSpend>())

    val topMerchants = analyticsService.getTopMerchants(currentMonthRange)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList<MerchantSpend>())

    val trend = analyticsService.getTrend(6)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList<MonthlyTrend>())
}
