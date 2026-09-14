package com.kardev.finsms.feature.smsingest

import com.kardev.finsms.core.database.dao.InstrumentDao
import com.kardev.finsms.core.database.dao.RawSmsDao
import com.kardev.finsms.core.database.dao.TransactionDao
import com.kardev.finsms.core.database.entity.RawSmsEntity
import com.kardev.finsms.core.database.entity.TransactionEntity
import com.kardev.finsms.core.common.ParseStatus
import com.kardev.finsms.feature.parser.SmsParserEngine
import java.security.MessageDigest
import javax.inject.Inject

class SmsRepository @Inject constructor(
    private val rawSmsDao: RawSmsDao,
    private val transactionDao: TransactionDao,
    private val instrumentDao: InstrumentDao,
    private val parserEngine: SmsParserEngine,
    private val dedupeService: DedupeService,
    private val categorizationEngine: CategorizationEngine
) {
    suspend fun ingestSms(sender: String, body: String, timestamp: Long) {
        val smsId = generateSmsId(sender, body, timestamp)

        if (rawSmsDao.exists(smsId)) return

        val parsed = parserEngine.parse(sender, body, timestamp)

        rawSmsDao.insert(
            RawSmsEntity(
                smsId = smsId,
                sender = sender,
                body = body,
                receivedAt = timestamp,
                parseStatus = if (parsed != null) ParseStatus.PARSED else ParseStatus.UNRECOGNIZED,
                matchedTemplateId = parsed?.templateId
            )
        )

        if (parsed == null) return

        val instrumentId = instrumentDao.findOrCreate(
            type = parsed.instrumentType,
            identifier = parsed.instrumentIdentifier,
            bankName = parsed.bankName
        )

        if (dedupeService.isDuplicate(parsed, instrumentId)) return

        val categoryId = categorizationEngine.categorize(parsed.merchant, parsed.direction)

        transactionDao.insert(
            TransactionEntity(
                sourceSmsId = smsId,
                amount = parsed.amount,
                direction = parsed.direction,
                instrumentId = instrumentId,
                categoryId = categoryId,
                merchant = parsed.merchant,
                transactionDate = parsed.transactionDate,
                referenceNumber = parsed.referenceNumber
            )
        )
    }

    /** Deterministic id so the same SMS is never double-processed by receiver + backfill. */
    private fun generateSmsId(sender: String, body: String, timestamp: Long): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest("$sender|$body|$timestamp".toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
}
