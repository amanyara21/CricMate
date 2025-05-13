package com.aman.cricmate.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aman.cricmate.components.AppHeader
import com.aman.cricmate.components.CalendarSelection
import com.aman.cricmate.viewModel.PlayerTestViewModel

@Composable
fun AddTestData(
    fieldName: String,
    viewModel: PlayerTestViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchPlayers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AppHeader(title = "Add $fieldName Test Data")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            CalendarSelection(
                title = "Pick Date to add Data",
                selectedDate = viewModel.date,
                onDateSelected = viewModel::onDateChange
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(viewModel.playerList) { player ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = player.name,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = viewModel.resultsMap[player._id] ?: "",
                            onValueChange = { viewModel.resultsMap[player._id] = it },
                            placeholder = { Text("Enter result") },
                            modifier = Modifier.width(150.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.submitResults(fieldName) {
                        Toast.makeText(context, "Submitted successfully", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !viewModel.isSubmitting
            ) {
                Text(if (viewModel.isSubmitting) "Submitting..." else "Submit")
            }
        }
    }
}