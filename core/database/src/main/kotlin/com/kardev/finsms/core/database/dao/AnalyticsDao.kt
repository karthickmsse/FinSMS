package com.kardev.finsms.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

data class CategorySpend(val categoryName: String, val icon: String?, val total: Double, val txnCount: Int)
data class InstrumentSpend(val instrumentName: String, val instrumentType: String, val total: Double, val txnCount: Int)
data class MonthlyTrend(val month: String, val totalSpend: Double, val totalIncome: Double)
data class MerchantSpend(val merchant: String, val total: Double, val txnCount: Int)

@Dao
interface AnalyticsDao {

    @Query("""
        SELECT SUM(amount) FROM transactions
        WHERE direction = 'DEBIT' AND transactionDate BETWEEN :startMillis AND :endMillis
    """)
    fun getTotalSpend(startMillis: Long, endMillis: Long): Flow<Double?>

    @Query("""
        SELECT SUM(amount) FROM transactions
        WHERE direction = 'CREDIT' AND transactionDate BETWEEN :startMillis AND :endMillis
    """)
    fun getTotalIncome(startMillis: Long, endMillis: Long): Flow<Double?>

    @Query("""
        SELECT c.name AS categoryName, c.icon AS icon, SUM(t.amount) AS total, COUNT(t.txnId) AS txnCount
        FROM transactions t
        INNER JOIN categories c ON t.categoryId = c.categoryId
        WHERE t.direction = 'DEBIT' AND t.transactionDate BETWEEN :startMillis AND :endMillis
        GROUP BY t.categoryId
        ORDER BY total DESC
    """)
    fun getCategoryBreakdown(startMillis: Long, endMillis: Long): Flow<List<CategorySpend>>

    @Query("""
        SELECT i.displayName AS instrumentName, i.type AS instrumentType,
               SUM(t.amount) AS total, COUNT(t.txnId) AS txnCount
        FROM transactions t
        INNER JOIN instruments i ON t.instrumentId = i.instrumentId
        WHERE t.direction = 'DEBIT' AND t.transactionDate BETWEEN :startMillis AND :endMillis
        GROUP BY t.instrumentId
        ORDER BY total DESC
    """)
    fun getSpendByInstrument(startMillis: Long, endMillis: Long): Flow<List<InstrumentSpend>>

    @Query("""
        SELECT strftime('%Y-%m', transactionDate / 1000, 'unixepoch') AS month,
               SUM(CASE WHEN direction = 'DEBIT' THEN amount ELSE 0 END) AS totalSpend,
               SUM(CASE WHEN direction = 'CREDIT' THEN amount ELSE 0 END) AS totalIncome
        FROM transactions
        WHERE transactionDate BETWEEN :startMillis AND :endMillis
        GROUP BY month
        ORDER BY month ASC
    """)
    fun getMonthlyTrend(startMillis: Long, endMillis: Long): Flow<List<MonthlyTrend>>

    @Query("""
        SELECT merchant, SUM(amount) AS total, COUNT(txnId) AS txnCount
        FROM transactions
        WHERE direction = 'DEBIT' AND merchant IS NOT NULL
        AND transactionDate BETWEEN :startMillis AND :endMillis
        GROUP BY merchant
        ORDER BY total DESC
        LIMIT :limit
    """)
    fun getTopMerchants(startMillis: Long, endMillis: Long, limit: Int = 10): Flow<List<MerchantSpend>>
}
