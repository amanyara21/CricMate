package com.aman.cricmate.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aman.cricmate.model.Event
import com.aman.cricmate.screen.IconText
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun EventCard(
    event: Event,
    currentUserId: String,
    onPreview: () -> Unit,
    onApply: (String) -> Unit
) {
    val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(event.date)
    val hasApplied = event.applicants.contains(currentUserId)
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB)),
        modifier = Modifier.fillMaxWidth()
    ) {
        val limitedDescription = event.description
            .split(" ")
            .take(100)
            .joinToString(" ") + if (event.description.split(" ").size > 100) "..." else ""

        Column(modifier = Modifier.padding(16.dp)) {
            Text(event.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(limitedDescription, fontSize = 14.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconText(icon = Icons.Default.Place, text = event.location)
                IconText(
                    icon = Icons.Default.DateRange,
                    text = formattedDate,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onPreview,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Preview", color = Color.White)
                }

                if (event.createdBy._id != currentUserId) {
                    Button(
                        onClick = { onApply(event._id) },
                        enabled = !hasApplied,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (hasApplied) Color.Gray else Color(0xFF1976D2)
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = if (hasApplied) "Applied" else "Apply", color = Color.White)
                    }
                }
            }

        }
    }
}
