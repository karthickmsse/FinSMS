package com.kardev.finsms.feature.parser

import com.kardev.finsms.core.common.InstrumentType
import com.kardev.finsms.core.common.ParsedTransaction
import javax.inject.Inject

/**
 * Matches raw SMS text against known bank templates and extracts a
 * normalized ParsedTransaction. Returns null when nothing matches --
 * caller should queue the SMS for manual review in that case.
 */
class SmsParserEngine @Inject constructor() {

    private val templates: List<SmsTemplate> = BankTemplates.ALL

    fun parse(sender: String, body: String, receivedAt: Long): ParsedTransaction? {
        val candidates = templates.filter { it.bankSenderPattern.matches(sender) }
        for (template in candidates) {
            val match = template.bodyPattern.find(body) ?: continue
            return try {
                mapMatchToTransaction(template, match, receivedAt)
            } catch (e: Exception) {
                null
            }
        }
        return null
    }

    private fun cleanAmount(raw: String): Double =
        raw.replace(",", "").toDouble()

    private fun mapMatchToTransaction(
        template: SmsTemplate,
        match: MatchResult,
        receivedAt: Long
    ): ParsedTransaction {
        val groups = match.groupValues

        // Each template has a different group layout depending on its
        // regex. This is intentionally explicit per template rather than
        // "clever" -- makes it obvious what to fix when real SMS drift
        // from these generic formats.
        return when (template.id) {
            "sbi_upi_debit" -> ParsedTransaction(
                templateId = template.id,
                amount = cleanAmount(groups[1]),
                direction = template.direction,
                instrumentType = InstrumentType.UPI,
                instrumentIdentifier = groups[2],
                bankName = template.bankName,
                merchant = null,
                transactionDate = receivedAt,
                referenceNumber = groups[4]
            )

            "sbi_transfer_debit" -> ParsedTransaction(
                templateId = template.id,
                amount = cleanAmount(groups[2]),
                direction = template.direction,
                instrumentType = InstrumentType.BANK_ACCOUNT,
                instrumentIdentifier = groups[1],
                bankName = template.bankName,
                merchant = null,
                transactionDate = receivedAt,
                referenceNumber = null
            )

            "sbi_credit" -> ParsedTransaction(
                templateId = template.id,
                amount = cleanAmount(groups[2]),
                direction = template.direction,
                instrumentType = InstrumentType.BANK_ACCOUNT,
                instrumentIdentifier = groups[1],
                bankName = template.bankName,
                merchant = groups.getOrNull(4)?.takeIf { it.isNotBlank() },
                transactionDate = receivedAt,
                referenceNumber = groups.getOrNull(5)
            )

            "hdfc_debit" -> ParsedTransaction(
                templateId = template.id,
                amount = cleanAmount(groups[1]),
                direction = template.direction,
                instrumentType = InstrumentType.BANK_ACCOUNT,
                instrumentIdentifier = groups[2],
                bankName = template.bankName,
                merchant = groups[3].trim(),
                transactionDate = receivedAt,
                referenceNumber = null
            )

            "hdfc_credit" -> ParsedTransaction(
                templateId = template.id,
                amount = cleanAmount(groups[1]),
                direction = template.direction,
                instrumentType = InstrumentType.BANK_ACCOUNT,
                instrumentIdentifier = groups[2],
                bankName = template.bankName,
                merchant = groups[4].trim(),
                transactionDate = receivedAt,
                referenceNumber = null
            )

            "icici_upi_debit" -> ParsedTransaction(
                templateId = template.id,
                amount = cleanAmount(groups[2]),
                direction = template.direction,
                instrumentType = InstrumentType.UPI,
                instrumentIdentifier = groups[1],
                bankName = template.bankName,
                merchant = groups[4].trim(),
                transactionDate = receivedAt,
                referenceNumber = groups[5]
            )

            "icici_credit" -> ParsedTransaction(
                templateId = template.id,
                amount = cleanAmount(groups[2]),
                direction = template.direction,
                instrumentType = InstrumentType.UPI,
                instrumentIdentifier = groups[1],
                bankName = template.bankName,
                merchant = groups[4].trim(),
                transactionDate = receivedAt,
                referenceNumber = groups[5]
            )

            "pnb_upi_debit" -> ParsedTransaction(
                templateId = template.id,
                amount = cleanAmount(groups[2]),
                direction = template.direction,
                instrumentType = InstrumentType.UPI,
                instrumentIdentifier = groups[1],
                bankName = template.bankName,
                merchant = groups[4].trim(),
                transactionDate = receivedAt,
                referenceNumber = groups[5]
            )

            "pnb_upi_credit" -> ParsedTransaction(
                templateId = template.id,
                amount = cleanAmount(groups[2]),
                direction = template.direction,
                instrumentType = InstrumentType.UPI,
                instrumentIdentifier = groups[1],
                bankName = template.bankName,
                merchant = groups[4].trim(),
                transactionDate = receivedAt,
                referenceNumber = null
            )

            "generic_salary_credit" -> ParsedTransaction(
                templateId = template.id,
                amount = cleanAmount(groups[1]),
                direction = template.direction,
                instrumentType = InstrumentType.UNKNOWN,
                instrumentIdentifier = "unknown",
                bankName = template.bankName,
                merchant = groups[3].trim(),
                transactionDate = receivedAt,
                referenceNumber = null
            )

            else -> throw IllegalStateException("Unmapped template: ${template.id}")
        }
    }
}
