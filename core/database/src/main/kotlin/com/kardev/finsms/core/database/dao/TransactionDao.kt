package com.kardev.finsms.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kardev.finsms.core.database.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    suspend fun insert(transaction: TransactionEntity): Long

    @Query("SELECT EXISTS(SELECT 1 FROM transactions WHERE referenceNumber = :ref)")
    suspend fun existsByReference(ref: String): Boolean

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM transactions
            WHERE amount = :amount AND instrumentId = :instrumentId
            AND transactionDate BETWEEN :windowStart AND :windowEnd
        )
    """)
    suspend fun existsSimilar(amount: Double, instrumentId: Long, windowStart: Long, windowEnd: Long): Boolean

    @Query("""
        SELECT t.*, c.name AS categoryName, i.displayName AS instrumentName
        FROM transactions t
        LEFT JOIN categories c ON t.categoryId = c.categoryId
        LEFT JOIN instruments i ON t.instrumentId = i.instrumentId
        ORDER BY t.transactionDate DESC
    """)
    fun getAllWithDetails(): Flow<List<TransactionWithDetails>>

    @Query("SELECT * FROM transactions WHERE categoryId IS NULL ORDER BY transactionDate DESC")
    fun getUncategorizedTransactions(): Flow<List<TransactionEntity>>

    @Query("UPDATE transactions SET categoryId = :categoryId, isManualOverride = 1 WHERE txnId = :txnId")
    suspend fun updateCategory(txnId: Long, categoryId: Long)
}

/**
 * Flattened projection used by the Transactions list UI -- avoids a
 * separate relation query per row.
 */
data class TransactionWithDetails(
    val txnId: Long,
    val sourceSmsId: String?,
    val amount: Double,
    val direction: com.kardev.finsms.core.common.Direction,
    val instrumentId: Long,
    val categoryId: Long?,
    val merchant: String?,
    val transactionDate: Long,
    val referenceNumber: String?,
    val isManualOverride: Boolean,
    val isManualEntry: Boolean,
    val notes: String?,
    val createdAt: Long,
    val categoryName: String?,
    val instrumentName: String?
)
