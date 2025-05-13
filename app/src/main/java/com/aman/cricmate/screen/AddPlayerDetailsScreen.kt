package com.aman.cricmate.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aman.cricmate.components.AppHeader
import com.aman.cricmate.components.CalendarSelection
import com.aman.cricmate.viewModel.AddPlayerViewModel

@Composable
fun AddPlayerDetails(navController: NavController, viewModel: AddPlayerViewModel) {
    val armOptions = listOf("Left Arm", "Right Arm")
    val bowlingOptions = listOf("Pace", "Spin", "Medium Pacer")

    val selectedArm = viewModel.armType
    val selectedBowling = viewModel.bowlingType

    val isSelectionValid = selectedArm.isNotBlank() && selectedBowling.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AppHeader(title = "Add Player Details")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {


            CalendarSelection(
                title = "Date of Birth",
                selectedDate = viewModel.dob,
                onDateSelected = { viewModel.updateDob(it) }
            )

            SelectionCardSection(
                title = "Select Bowling Arm",
                options = armOptions,
                selectedOption = selectedArm,
                onOptionSelected = { viewModel.armType = it }
            )

            SelectionCardSection(
                title = "Select Bowling Type",
                options = bowlingOptions,
                selectedOption = selectedBowling,
                onOptionSelected = { viewModel.bowlingType = it }
            )

            Button(
                onClick = {
                    if (isSelectionValid) navController.navigate("Coaches")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = isSelectionValid
            ) {
                Text("Next", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SelectionCardSection(
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                options.forEach { option ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (option == selectedOption)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surface
                            )
                            .border(
                                width = if (option == selectedOption) 2.dp else 0.dp,
                                color = if (option == selectedOption)
                                    MaterialTheme.colorScheme.primary
                                else
                                    Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onOptionSelected(option) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = if (option == selectedOption)
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else
                                MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}
