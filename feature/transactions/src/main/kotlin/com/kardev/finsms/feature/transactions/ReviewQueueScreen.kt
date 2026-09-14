package com.kardev.finsms.feature.transactions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kardev.finsms.core.database.dao.TransactionWithDetails
import com.kardev.finsms.core.database.entity.CategoryEntity
import com.kardev.finsms.core.database.entity.RawSmsEntity

@Composable
fun ReviewQueueScreen(viewModel: ReviewQueueViewModel = hiltViewModel()) {
    val unrecognizedSms by viewModel.unrecognizedSms.collectAsStateWithLifecycle()
    val uncategorizedTxns by viewModel.uncategorizedTransactions.collectAsStateWithLifecycle()
    val categories by viewModel.allCategories.collectAsStateWithLifecycle()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (uncategorizedTxns.isNotEmpty()) {
            item { Text("Needs a Category (${uncategorizedTxns.size})", style = MaterialTheme.typography.titleMedium) }
            items(uncategorizedTxns) { txn ->
                UncategorizedTxnCard(txn, categories, onCategorize = { categoryId ->
                    viewModel.categorize(txn.txnId, categoryId)
                })
            }
        }

        if (unrecognizedSms.isNotEmpty()) {
            item { Text("Unrecognized SMS (${unrecognizedSms.size})", style = MaterialTheme.typography.titleMedium) }
            items(unrecognizedSms) { sms ->
                UnrecognizedSmsCard(sms, onDismiss = { viewModel.markIgnored(sms.smsId) })
            }
        }

        if (uncategorizedTxns.isEmpty() && unrecognizedSms.isEmpty()) {
            item {
                Text(
                    "All caught up!",
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun UncategorizedTxnCard(
    txn: TransactionWithDetails,
    categories: List<CategoryEntity>,
    onCategorize: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(txn.merchant ?: "Unknown merchant", modifier = Modifier.weight(1f))
                Text("\u20B9${txn.amount}")
            }
            TextButton(onClick = { expanded = true }) { Text("Assign category") }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            onCategorize(category.categoryId)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun UnrecognizedSmsCard(sms: RawSmsEntity, onDismiss: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text(sms.sender, style = MaterialTheme.typography.labelMedium)
            Text(sms.body, style = MaterialTheme.typography.bodySmall, maxLines = 3, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(4.dp))
            TextButton(onClick = onDismiss) { Text("Not a transaction") }
        }
    }
}
