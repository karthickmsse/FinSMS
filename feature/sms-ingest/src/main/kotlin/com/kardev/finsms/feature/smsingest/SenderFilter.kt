package com.kardev.finsms.feature.smsingest

object SenderFilter {
    private val bankSenderPatterns = listOf(
        Regex("(?i).*HDFC.*"), Regex("(?i).*SBI.*"), Regex("(?i).*ICICI.*"),
        Regex("(?i).*AXIS.*"), Regex("(?i).*PNB.*"), Regex("(?i).*KOTAK.*"),
        Regex("(?i).*UPI.*"), Regex("(?i).*BOB.*"), Regex("(?i).*CANARA.*")
    )

    fun isBankOrUpiSender(sender: String): Boolean =
        bankSenderPatterns.any { it.containsMatchIn(sender) }
}
