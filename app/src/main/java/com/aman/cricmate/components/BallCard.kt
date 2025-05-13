package com.aman.cricmate.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aman.cricmate.model.BallResponse

@Composable
fun BallCard(ball: BallResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(150.dp)
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Type: ${ball.type}",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF1E88E5)
            )
            Text(
                text = "Speed: ${ball.speed} km/h",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF424242)
            )
        }
    }
}