package com.aman.cricmate.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.aman.cricmate.components.AppHeader
import com.aman.cricmate.model.Constants.testItems
import com.aman.cricmate.components.LineChartView
import com.aman.cricmate.viewModel.TestResultViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    userId: String,
    field: String,
    viewModel: TestResultViewModel = hiltViewModel()
) {
    val results = viewModel.testResults
    val isLoading = viewModel.isLoading
    val error = viewModel.error
    var expanded by remember { mutableStateOf(false) }

    val filterMap = mapOf(
        "Last 7 Days" to "last7days",
        "Last 30 Days" to "last30days",
        "All" to "all"
    )
    val displayOptions = filterMap.keys.toList()

    var selectedFilter by remember { mutableStateOf(viewModel.filter) }
    val selectedDisplay = filterMap.entries.find { it.value == selectedFilter }?.key ?: "All"

    LaunchedEffect(selectedFilter, field) {
        val testItem = testItems[field] ?: ""
        if (testItem.isNotEmpty()) {
            viewModel.fetchResults(testItem, selectedFilter, userId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        AppHeader("$field Progress Chart")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {


            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedDisplay,
                    onValueChange = {},
                    label = { Text("Select Filter") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    displayOptions.forEach { label ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                selectedFilter = filterMap[label] ?: "all"
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                error != null -> {
                    Text(
                        "Error: $error",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                results != null -> {
                    LineChartView(results)
                }

                else -> {
                    Text(
                        "No data available.",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}
