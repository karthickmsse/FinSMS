package com.kardev.finsms.feature.analytics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kardev.finsms.core.database.dao.CategorySpend
import java.util.Locale

@Composable
fun HomeScreen(viewModel: AnalyticsViewModel = hiltViewModel()) {
    val summary by viewModel.monthSummary.collectAsStateWithLifecycle()
    val categoryBreakdown by viewModel.categoryBreakdown.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { MonthSummaryCard(summary) }
        item { TopCategoriesCard(categoryBreakdown) }
    }
}

@Composable
fun MonthSummaryCard(summary: MonthSummary) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(20.dp)) {
            Text("This Month", style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                formatRupees(summary.spend),
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                LabeledStat("Income", summary.income, Color(0xFF2E7D32))
                LabeledStat("Net", summary.net, if (summary.net >= 0) Color(0xFF2E7D32) else Color(0xFFC62828))
            }
        }
    }
}

@Composable
fun LabeledStat(label: String, value: Double, color: Color) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall)
        Text(formatRupees(value), color = color, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun TopCategoriesCard(categories: List<CategorySpend>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("Top Categories", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))
            if (categories.isEmpty()) {
                Text("No spending yet this month", style = MaterialTheme.typography.bodyMedium)
            } else {
                categories.take(5).forEach { cat ->
                    CategoryRow(cat)
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun CategoryRow(category: CategorySpend) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(category.categoryName, style = MaterialTheme.typography.bodyMedium)
        Text(formatRupees(category.total), style = MaterialTheme.typography.bodyMedium)
    }
}

fun formatRupees(amount: Double): String = "\u20B9${String.format(Locale.getDefault(), "%,.2f", amount)}"
