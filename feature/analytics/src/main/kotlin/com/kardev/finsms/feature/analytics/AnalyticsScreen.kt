package com.kardev.finsms.feature.analytics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kardev.finsms.core.database.dao.InstrumentSpend
import com.kardev.finsms.core.database.dao.MerchantSpend

@Composable
fun AnalyticsScreen(viewModel: AnalyticsViewModel = hiltViewModel()) {
    val categoryBreakdown by viewModel.categoryBreakdown.collectAsStateWithLifecycle()
    val instrumentBreakdown by viewModel.instrumentBreakdown.collectAsStateWithLifecycle()
    val topMerchants by viewModel.topMerchants.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { SectionHeader("Spend by Category") }
        items(categoryBreakdown) { CategoryRow(it) }

        item { SectionHeader("Spend by Card/Instrument") }
        items(instrumentBreakdown) { InstrumentRow(it) }

        item { SectionHeader("Top Merchants") }
        items(topMerchants) { MerchantRow(it) }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
}

@Composable
fun InstrumentRow(instrument: InstrumentSpend) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(instrument.instrumentName, style = MaterialTheme.typography.bodyMedium)
        Text(formatRupees(instrument.total), style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun MerchantRow(merchant: MerchantSpend) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(merchant.merchant, style = MaterialTheme.typography.bodyMedium)
        Text(formatRupees(merchant.total), style = MaterialTheme.typography.bodyMedium)
    }
}
