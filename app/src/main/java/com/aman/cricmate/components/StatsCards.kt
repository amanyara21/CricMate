package com.aman.cricmate.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun FullStatCardWithProgress(title: String, completed: Int, total: Int, backgroundColor: Color) {
    val progress = completed.toFloat() / total

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .graphicsLayer { alpha = 0.9f }
            .background(backgroundColor.copy(alpha = 0.4f), shape = RoundedCornerShape(16.dp))
            .border(1.dp, backgroundColor.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                Text(
                    text = "$completed / $total",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black
                )
            }


            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(80.dp)
            ) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    color = backgroundColor.copy(alpha = 0.8f),
                    strokeWidth = 8.dp,
                    trackColor = backgroundColor.copy(alpha = 0.2f),
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun StatCardGlass(title: String, value: String, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(80.dp)
            .graphicsLayer { alpha = 0.9f }
            .background(backgroundColor.copy(alpha = 0.5f), shape = RoundedCornerShape(12.dp))
            .border(1.dp, backgroundColor.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF424242))
            Text(value, style = MaterialTheme.typography.headlineSmall, color = Color(0xFF212121))
        }
    }
}
