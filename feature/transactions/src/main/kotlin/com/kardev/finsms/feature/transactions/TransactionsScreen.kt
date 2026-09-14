package com.kardev.finsms.feature.transactions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kardev.finsms.core.common.Direction
import com.kardev.finsms.core.database.dao.TransactionWithDetails
import java.util.Locale

@Composable
fun TransactionsScreen(viewModel: TransactionsViewModel = hiltViewModel()) {
    val transactions by viewModel.filteredTransactions.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = viewModel::onSearchChanged,
            placeholder = { Text("Search merchant, amount...") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )

        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(TxnFilter.entries) { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { viewModel.onFilterSelected(filter) },
                    label = { Text(filter.label) }
                )
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(transactions, key = { it.txnId }) { txn ->
                TransactionRow(txn)
                HorizontalDivider()
            }
            if (transactions.isEmpty()) {
                item {
                    Text(
                        "No transactions found",
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionRow(txn: TransactionWithDetails) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { }.padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(txn.merchant ?: "Unknown", style = MaterialTheme.typography.bodyLarge)
            Text(
                "${txn.categoryName ?: "Uncategorized"} \u00B7 ${txn.instrumentName ?: ""}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            "${if (txn.direction == Direction.DEBIT) "-" else "+"}\u20B9${String.format(Locale.getDefault(), "%,.2f", txn.amount)}",
            color = if (txn.direction == Direction.DEBIT) Color(0xFFC62828) else Color(0xFF2E7D32),
            style = MaterialTheme.typography.titleMedium
        )
    }
}
