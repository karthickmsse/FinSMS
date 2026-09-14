package com.kardev.finsms.feature.smsingest

import com.kardev.finsms.core.common.Direction
import com.kardev.finsms.core.common.InstrumentType
import com.kardev.finsms.core.common.ParsedTransaction
import com.kardev.finsms.core.database.dao.TransactionDao
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class DedupeServiceTest {

    private val transactionDao: TransactionDao = mock()
    private val dedupeService = DedupeService(transactionDao)

    @Test
    fun testIsDuplicateByReference() = runBlocking {
        val parsed = createTransaction(ref = "REF123")
        whenever(transactionDao.existsByReference("REF123")).thenReturn(true)

        assertTrue(dedupeService.isDuplicate(parsed, 1L))
    }

    @Test
    fun testIsNotDuplicate() = runBlocking {
        val parsed = createTransaction(ref = "REF_NEW")
        whenever(transactionDao.existsByReference("REF_NEW")).thenReturn(false)
        whenever(transactionDao.existsSimilar(any(), any(), any(), any())).thenReturn(false)

        assertFalse(dedupeService.isDuplicate(parsed, 1L))
    }

    private fun createTransaction(ref: String?) = ParsedTransaction(
        templateId = "test",
        amount = 100.0,
        direction = Direction.DEBIT,
        instrumentType = InstrumentType.UPI,
        instrumentIdentifier = "X1234",
        bankName = "TEST",
        merchant = null,
        transactionDate = System.currentTimeMillis(),
        referenceNumber = ref
    )
}
