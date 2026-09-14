package com.kardev.finsms.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kardev.finsms.core.common.Direction

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(entity = InstrumentEntity::class, parentColumns = ["instrumentId"], childColumns = ["instrumentId"]),
        ForeignKey(entity = CategoryEntity::class, parentColumns = ["categoryId"], childColumns = ["categoryId"]),
        ForeignKey(entity = RawSmsEntity::class, parentColumns = ["smsId"], childColumns = ["sourceSmsId"])
    ],
    indices = [Index("instrumentId"), Index("categoryId"), Index("transactionDate")]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val txnId: Long = 0,
    val sourceSmsId: String? = null,
    val amount: Double,
    val direction: Direction,
    val instrumentId: Long,
    val categoryId: Long? = null,
    val merchant: String? = null,
    val transactionDate: Long,
    val referenceNumber: String? = null,
    val isManualOverride: Boolean = false,
    val isManualEntry: Boolean = false,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
