package com.aman.cricmate.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aman.cricmate.components.ReusableClickableCard
import com.aman.cricmate.model.Event
import com.aman.cricmate.viewModel.EventViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun EventPreviewScreen(
    navController:NavController,
    currentUserId: String,
    eventId: String,
    viewModel: EventViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val selectedEvent= viewModel.selectedEvent
    val applicants= viewModel.applicants
    var isApplied by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getEventById(eventId)
        viewModel.loadApplicants(eventId)
    }

    selectedEvent?.let { ev ->
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    ev.title,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D47A1)
                )
                Text(ev.description, fontSize = 16.sp)
                Text("📅 Date: ${SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(ev.date)}")
                Text("📍 Location: ${ev.location}")
                Text("👤 Created by: ${ev.createdBy.name}")

                Spacer(modifier = Modifier.height(20.dp))

                if (ev.createdBy._id == currentUserId) {
                    Text(
                        "👥 Applicants (${ev.applicants.size}):",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    applicants.forEach { applicant ->
                        ReusableClickableCard(
                            text = applicant.name,
                            backgroundColor = Color(0xFFBBDEFB),
                            buttonText = "Show Profile"
                        ) {
                            navController.navigate("profilewithTest/${applicant._id}")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                } else {
                    Button(
                        onClick = {
                            viewModel.applyToEvent(ev._id) { success ->
                                if (success) {
                                    isApplied = true
                                    Toast.makeText(context, "Applied!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Failed to apply!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        enabled = !isApplied,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isApplied) Color.Gray else Color(0xFF1976D2)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isApplied) "✅ Applied" else "Apply",
                            color = Color.White
                        )
                    }
                }
            }
        }
    } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
