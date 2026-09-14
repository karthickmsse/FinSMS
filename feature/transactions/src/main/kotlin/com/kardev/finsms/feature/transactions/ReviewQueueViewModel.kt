package com.kardev.finsms.feature.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kardev.finsms.core.database.dao.CategoryDao
import com.kardev.finsms.core.database.dao.RawSmsDao
import com.kardev.finsms.core.database.dao.TransactionDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewQueueViewModel @Inject constructor(
    private val rawSmsDao: RawSmsDao,
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
) : ViewModel() {

    val unrecognizedSms = rawSmsDao.getUnrecognized()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uncategorizedTransactions = transactionDao.getUncategorizedTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCategories = categoryDao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun categorize(txnId: Long, categoryId: Long) {
        viewModelScope.launch {
            transactionDao.updateCategory(txnId, categoryId)
        }
    }

    fun markIgnored(smsId: String) {
        viewModelScope.launch {
            rawSmsDao.markIgnored(smsId)
        }
    }
}
