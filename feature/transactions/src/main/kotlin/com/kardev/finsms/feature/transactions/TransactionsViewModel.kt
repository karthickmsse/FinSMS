package com.kardev.finsms.feature.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kardev.finsms.core.common.Direction
import com.kardev.finsms.core.database.dao.TransactionDao
import com.kardev.finsms.core.database.dao.TransactionWithDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

enum class TxnFilter(val label: String) {
    ALL("All"), DEBIT("Debits"), CREDIT("Credits"), UNCATEGORIZED("Uncategorized")
}

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionDao: TransactionDao
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedFilter = MutableStateFlow(TxnFilter.ALL)
    val selectedFilter: StateFlow<TxnFilter> = _selectedFilter

    val filteredTransactions: StateFlow<List<TransactionWithDetails>> = combine(
        transactionDao.getAllWithDetails(),
        _searchQuery,
        _selectedFilter
    ) { transactions, query, filter ->
        transactions
            .filter { txn ->
                when (filter) {
                    TxnFilter.ALL -> true
                    TxnFilter.DEBIT -> txn.direction == Direction.DEBIT
                    TxnFilter.CREDIT -> txn.direction == Direction.CREDIT
                    TxnFilter.UNCATEGORIZED -> txn.categoryId == null
                }
            }
            .filter { txn ->
                query.isBlank() ||
                    (txn.merchant?.contains(query, ignoreCase = true) == true) ||
                    txn.amount.toString().contains(query)
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchChanged(query: String) { _searchQuery.value = query }
    fun onFilterSelected(filter: TxnFilter) { _selectedFilter.value = filter }
}
