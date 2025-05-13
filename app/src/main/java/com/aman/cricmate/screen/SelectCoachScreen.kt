package com.aman.cricmate.screen

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aman.cricmate.components.AppHeader
import com.aman.cricmate.viewModel.AddPlayerViewModel

@Composable
fun CoachSelectionScreen(
    navController: NavController,
    addPlayerViewModel: AddPlayerViewModel
) {
    val context = LocalContext.current
    val filteredCoaches = addPlayerViewModel.filteredCoaches
    val selectedCoaches = addPlayerViewModel.selectedCoaches
    val isCoachSelected = selectedCoaches.isNotEmpty()
    val response = addPlayerViewModel.addResponse

    LaunchedEffect(response?.success) {
        if (response?.success == true) {
            navController.navigate("Splash") {
                popUpTo("add_player_flow") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AppHeader("Select Coaches")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = addPlayerViewModel.coachSearchQuery,
                onValueChange = { addPlayerViewModel.coachSearchQuery = it },
                label = { Text("Search Coaches") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredCoaches) { coach ->
                    val isSelected = selectedCoaches.contains(coach)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                addPlayerViewModel.toggleCoachSelection(coach)
                            },
                        elevation = CardDefaults.cardElevation(4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = {
                                    addPlayerViewModel.toggleCoachSelection(coach)
                                }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = coach.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (isSelected)
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isCoachSelected) {
                        addPlayerViewModel.addDetail(context)
                    }
                },
                modifier = Modifier.align(Alignment.End),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Next")
            }
        }
    }
}

