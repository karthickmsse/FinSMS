package com.kardev.finsms.feature.parser

import com.kardev.finsms.core.common.Direction

/**
 * Regex templates built from publicly documented, anonymized SMS formats.
 * These are a STARTING POINT ONLY -- validate and tune against your own
 * real bank/UPI SMS before relying on them. Order matters: bank-specific
 * templates must be listed before the generic fallback.
 */
object BankTemplates {

    // ---------------- DEBIT ----------------

    val SBI_UPI_DEBIT = SmsTemplate(
        id = "sbi_upi_debit",
        bankSenderPattern = Regex("(?i).*SBI.*"),
        bodyPattern = Regex(
            """Rs\.?\s?([\d,]+\.?\d*)\s*debited@SBI\s*UPI\s*frm\s*A/c\s*X?(\d+)\s*on\s*(\d{2}\w{3}\d{2,4})\s*RefNo\s*(\w+)""",
            RegexOption.IGNORE_CASE
        ),
        direction = Direction.DEBIT,
        bankName = "SBI"
    )

    val SBI_TRANSFER_DEBIT = SmsTemplate(
        id = "sbi_transfer_debit",
        bankSenderPattern = Regex("(?i).*SBI.*"),
        bodyPattern = Regex(
            """A/c\s*X?(\d+)-?debited\s*by\s*Rs\.?\s?([\d,]+\.?\d*)\s*on\s*(\d{2}\w{3}\d{2,4})""",
            RegexOption.IGNORE_CASE
        ),
        direction = Direction.DEBIT,
        bankName = "SBI"
    )

    val HDFC_DEBIT = SmsTemplate(
        id = "hdfc_debit",
        bankSenderPattern = Regex("(?i).*HDFC.*"),
        bodyPattern = Regex(
            """Rs\.?\s?([\d,]+\.?\d*)\s*debited\s*from\s*a/c\s*\*+(\d+)\s*to\s*([A-Za-z0-9 .&]+?)\.?\s*Avl\s*bal\s*Rs\.?\s?([\d,]+\.?\d*)""",
            RegexOption.IGNORE_CASE
        ),
        direction = Direction.DEBIT,
        bankName = "HDFC"
    )

    val ICICI_UPI_DEBIT = SmsTemplate(
        id = "icici_upi_debit",
        bankSenderPattern = Regex("(?i).*ICICI.*"),
        bodyPattern = Regex(
            """Acct\s*XX(\d+)\s*debited\s*for\s*Rs\.?\s?([\d,]+\.?\d*)\s*on\s*([\d\w-]+);\s*([A-Za-z0-9 .&]+?)\s*credited.*UPI:(\d+)""",
            RegexOption.IGNORE_CASE
        ),
        direction = Direction.DEBIT,
        bankName = "ICICI"
    )

    val PNB_UPI_DEBIT = SmsTemplate(
        id = "pnb_upi_debit",
        bankSenderPattern = Regex("(?i).*PNB.*"),
        bodyPattern = Regex(
            """A/c\s*X?(\d+)\s*debited\s*INR\s*([\d,]+\.?\d*)\s*Dt\s*([\d-]+)\s*[\d:]+\s*to\s*([A-Za-z0-9 .&]+?)\s*thru\s*UPI:(\d+)\.?Bal\s*INR\s*([\d,]+\.?\d*)""",
            RegexOption.IGNORE_CASE
        ),
        direction = Direction.DEBIT,
        bankName = "PNB"
    )

    // ---------------- CREDIT ----------------

    val SBI_CREDIT = SmsTemplate(
        id = "sbi_credit",
        bankSenderPattern = Regex("(?i).*SBI.*"),
        bodyPattern = Regex(
            """A/c\s*X?(\d+)\s*credited\s*by\s*Rs\.?\s?([\d,]+\.?\d*)\s*on\s*(\d{2}\w{3}\d{2,4})(?:\s*transfer\s*from\s*([A-Za-z0-9 .&]+?))?\s*Ref\s*No\s*(\w+)""",
            RegexOption.IGNORE_CASE
        ),
        direction = Direction.CREDIT,
        bankName = "SBI"
    )

    val HDFC_CREDIT = SmsTemplate(
        id = "hdfc_credit",
        bankSenderPattern = Regex("(?i).*HDFC.*"),
        bodyPattern = Regex(
            """Rs\.?\s?([\d,]+\.?\d*)\s*credited\s*to\s*a/c\s*\*+(\d+)\s*on\s*([\d\w-]+)\s*by\s*([A-Za-z ]+?)\.?\s*Avl\s*bal\s*Rs\.?\s?([\d,]+\.?\d*)""",
            RegexOption.IGNORE_CASE
        ),
        direction = Direction.CREDIT,
        bankName = "HDFC"
    )

    val ICICI_CREDIT = SmsTemplate(
        id = "icici_credit",
        bankSenderPattern = Regex("(?i).*ICICI.*"),
        bodyPattern = Regex(
            """Acct\s*XX(\d+)\s*is\s*credited\s*with\s*Rs\.?\s?([\d,]+\.?\d*)\s*on\s*([\d\w-]+)\s*from\s*([A-Za-z0-9 .&]+?)\.?\s*UPI:(\d+)""",
            RegexOption.IGNORE_CASE
        ),
        direction = Direction.CREDIT,
        bankName = "ICICI"
    )

    val PNB_UPI_CREDIT = SmsTemplate(
        id = "pnb_upi_credit",
        bankSenderPattern = Regex("(?i).*PNB.*"),
        bodyPattern = Regex(
            """A/c\s*X?(\d+)\s*credited\s*for\s*INR\s*([\d,]+\.?\d*)\s*on\s*([\d-]+)\s*[\d:]+\s*by\s*([A-Za-z0-9 .&]+?)\s*thru\s*UPI\.?Avl?Bal\s*INR\s*([\d,]+\.?\d*)""",
            RegexOption.IGNORE_CASE
        ),
        direction = Direction.CREDIT,
        bankName = "PNB"
    )

    val GENERIC_SALARY_CREDIT = SmsTemplate(
        id = "generic_salary_credit",
        bankSenderPattern = Regex("(?i).*"),
        bodyPattern = Regex(
            """credited\s*(?:by|with)?\s*(?:Rs\.?|INR)\s*([\d,]+\.?\d*)\s*on\s*([\d-]+)\s*by\s*([A-Za-z0-9 .&]+?)\.?\s*Bal:?\s*(?:Rs\.?|INR)\s*([\d,]+\.?\d*)""",
            RegexOption.IGNORE_CASE
        ),
        direction = Direction.CREDIT,
        bankName = "UNKNOWN"
    )

    val ALL = listOf(
        SBI_UPI_DEBIT, SBI_TRANSFER_DEBIT, SBI_CREDIT,
        HDFC_DEBIT, HDFC_CREDIT,
        ICICI_UPI_DEBIT, ICICI_CREDIT,
        PNB_UPI_DEBIT, PNB_UPI_CREDIT,
        GENERIC_SALARY_CREDIT
    )
}
