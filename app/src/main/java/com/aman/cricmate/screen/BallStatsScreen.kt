package com.aman.cricmate.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aman.cricmate.components.AppHeader
import com.aman.cricmate.components.StatsLineChart
import com.aman.cricmate.viewModel.BallStatsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BallStatsScreen(userId: String, viewModel: BallStatsViewModel = hiltViewModel()) {
    val filterOptions = listOf("last7days", "last30days")
    val stats = viewModel.stats

    LaunchedEffect(viewModel.filter) {
        viewModel.fetchStats(userId)
    }

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AppHeader(title = "Bowling Stats")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = viewModel.filter,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Duration") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    filterOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                viewModel.filter = option
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.isLoading) {
                CircularProgressIndicator()
            } else if (viewModel.error != null) {
                Text("Error: ${viewModel.error}", color = Color.Red)
            }

            if (stats.isNotEmpty()) {

                ChartWithTitle("Total Balls") {
                    StatsLineChart(
                        statsData = stats,
                        label = "Total Balls",
                        getValueForEntry = { it.totalBalls.toFloat() }
                    )
                }

                ChartWithTitle("Avg Speed") {
                    StatsLineChart(
                        statsData = stats,
                        label = "Avg Speed",
                        getValueForEntry = { it.avgSpeed.toFloat() }
                    )
                }

                ChartWithTitle("Yorkers") {
                    StatsLineChart(
                        statsData = stats,
                        label = "Yorkers",
                        getValueForEntry = { it.yorkers.toFloat() }
                    )
                }

                ChartWithTitle("Bouncers") {
                    StatsLineChart(
                        statsData = stats,
                        label = "Bouncers",
                        getValueForEntry = { it.bouncers.toFloat() }
                    )
                }

                ChartWithTitle("Short") {
                    StatsLineChart(
                        statsData = stats,
                        label = "Short",
                        getValueForEntry = { it.short.toFloat() }
                    )
                }

                ChartWithTitle("Good Length") {
                    StatsLineChart(
                        statsData = stats,
                        label = "Good Length",
                        getValueForEntry = { it.goodLength.toFloat() }
                    )
                }
            } else {
                Text("No data available.")
            }
        }
    }
}

@Composable
fun ChartWithTitle(title: String, content: @Composable () -> Unit) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        content()
        Spacer(modifier = Modifier.height(24.dp))
    }
}
