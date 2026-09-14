package com.kardev.finsms.feature.instruments

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun InstrumentsScreen(viewModel: InstrumentsViewModel = hiltViewModel()) {
    val instruments by viewModel.instruments.collectAsStateWithLifecycle()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(instruments) { instrument ->
            ListItem(
                headlineContent = { Text(instrument.displayName) },
                supportingContent = { Text("${instrument.bankName} \u00B7 ${instrument.type}") }
            )
        }
        if (instruments.isEmpty()) {
            item { Text("No cards or accounts detected yet") }
        }
    }
}
