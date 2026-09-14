package com.kardev.finsms.feature.smsingest

import com.kardev.finsms.core.common.ParsedTransaction
import com.kardev.finsms.core.database.dao.TransactionDao
import javax.inject.Inject

class DedupeService @Inject constructor(
    private val transactionDao: TransactionDao
) {
    suspend fun isDuplicate(parsed: ParsedTransaction, instrumentId: Long): Boolean {
        parsed.referenceNumber?.let { ref ->
            if (transactionDao.existsByReference(ref)) return true
        }
        val windowStart = parsed.transactionDate - 120_000
        val windowEnd = parsed.transactionDate + 120_000
        return transactionDao.existsSimilar(parsed.amount, instrumentId, windowStart, windowEnd)
    }
}
