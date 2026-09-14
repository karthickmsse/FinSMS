package com.kardev.finsms.feature.parser

import com.kardev.finsms.core.common.Direction
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class SmsParserEngineTest {

    private val engine = SmsParserEngine()
    private val timestamp = System.currentTimeMillis()

    @Test
    fun testSbiUpiDebit() {
        val sms = "Rs.500.00 debited@SBI UPI frm A/c X1234 on 14Sep26 RefNo 67890"
        val sender = "AD-SBIUPI"
        val result = engine.parse(sender, sms, timestamp)

        assertNotNull(result)
        assertEquals(500.0, result?.amount)
        assertEquals(Direction.DEBIT, result?.direction)
        assertEquals("1234", result?.instrumentIdentifier)
        assertEquals("SBI", result?.bankName)
        assertEquals("67890", result?.referenceNumber)
    }

    @Test
    fun testHdfcDebit() {
        val sms = "Rs.1,200.50 debited from a/c **5678 to AMZN INDIA. Avl bal Rs.10,000.00"
        val sender = "AD-HDFCBK"
        val result = engine.parse(sender, sms, timestamp)

        assertNotNull(result)
        assertEquals(1200.50, result?.amount)
        assertEquals(Direction.DEBIT, result?.direction)
        assertEquals("5678", result?.instrumentIdentifier)
        assertEquals("HDFC", result?.bankName)
        assertEquals("AMZN INDIA", result?.merchant)
    }

    @Test
    fun testIciciCredit() {
        val sms = "Acct XX9012 is credited with Rs.25,000.00 on 14-Sep-26 from EMPLOYER INC. UPI:12345678"
        val sender = "VM-ICICIB"
        val result = engine.parse(sender, sms, timestamp)

        assertNotNull(result)
        assertEquals(25000.0, result?.amount)
        assertEquals(Direction.CREDIT, result?.direction)
        assertEquals("9012", result?.instrumentIdentifier)
        assertEquals("ICICI", result?.bankName)
        assertEquals("EMPLOYER INC", result?.merchant)
        assertEquals("12345678", result?.referenceNumber)
    }

    @Test
    fun testNoMatch() {
        val sms = "Random message that is not a transaction"
        val sender = "FRIEND"
        val result = engine.parse(sender, sms, timestamp)
        assertEquals(null, result)
    }
}
