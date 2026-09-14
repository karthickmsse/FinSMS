package com.kardev.finsms.feature.analytics

import java.util.Calendar

sealed class DateRange {
    data class Month(val year: Int, val month: Int) : DateRange()
    data class Custom(val start: Long, val end: Long) : DateRange()

    fun toMillis(): Pair<Long, Long> = when (this) {
        is Month -> DateRangeUtil.monthRange(year, month)
        is Custom -> start to end
    }
}

object DateRangeUtil {
    fun currentMonthRange(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0)
        val start = cal.timeInMillis
        cal.add(Calendar.MONTH, 1)
        val end = cal.timeInMillis - 1
        return start to end
    }

    fun monthRange(year: Int, month: Int): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(year, month, 1, 0, 0, 0)
        val start = cal.timeInMillis
        cal.add(Calendar.MONTH, 1)
        val end = cal.timeInMillis - 1
        return start to end
    }

    fun lastNMonthsRange(n: Int): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        val end = cal.timeInMillis
        cal.add(Calendar.MONTH, -n)
        val start = cal.timeInMillis
        return start to end
    }
}
