package com.aman.cricmate.screen


import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aman.cricmate.components.AppHeader
import com.aman.cricmate.components.EventCard
import com.aman.cricmate.di.UserSessionManager
import com.aman.cricmate.model.Event
import com.aman.cricmate.viewModel.EventViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    navController: NavController,
    user: UserSessionManager,
    viewModel: EventViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val eventFilterOptions = listOf("All", "My Events")
    val selectedOptionText = viewModel.eventFilterOption

    LaunchedEffect(Unit) {
        viewModel.loadAllEvents()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AppHeader("Upcoming Events")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedOptionText,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Filter Events") },
                        trailingIcon = {
                            TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        eventFilterOptions.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    viewModel.setEventFilter(selectionOption, user.id.value!!)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Button(onClick = { navController.navigate("addEvent") }) {
                    Text("Add Your Event")
                }
            }


            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    top = 10.dp,
                    bottom = 60.dp,
                    start = 0.dp,
                    end = 0.dp
                ),

                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viewModel.filteredEvents) { event ->
                    EventCard(
                        event = event,
                        currentUserId = user.id.value!!,
                        onPreview = {
                            navController.navigate("eventPreview/${user.id.value!!}/${event._id}")
                        },
                        onApply = {
                            viewModel.applyToEvent(event._id) { success ->
                                if (success) {
                                    viewModel.loadAllEvents()
                                    Toast.makeText(
                                        context,
                                        "Successfully applied to event!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Failed to apply. Please try again.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                        }
                    )
                }
            }
        }
    }
}



@Composable
fun IconText(icon: ImageVector, text: String, modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Icon(icon, contentDescription = null, tint = Color.DarkGray, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, fontSize = 13.sp, color = Color.DarkGray)
    }
}