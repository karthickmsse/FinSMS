package com.kardev.finsms.core.common

/**
 * Output of the parser engine, before it is persisted to Room.
 * Kept free of any Android/Room dependency so :feature:parser stays a pure Kotlin module.
 */
data class ParsedTransaction(
    val templateId: String,
    val amount: Double,
    val direction: Direction,
    val instrumentType: InstrumentType,
    val instrumentIdentifier: String,
    val bankName: String,
    val merchant: String?,
    val transactionDate: Long,
    val referenceNumber: String?
)
