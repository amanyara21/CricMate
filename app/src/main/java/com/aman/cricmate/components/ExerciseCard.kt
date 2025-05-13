package com.aman.cricmate.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun ExerciseCard(
    name: String,
    reps: String? = null,
    duration: String? = null,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    val finalModifier = modifier
        .then(
            if (modifier == Modifier) Modifier.width(160.dp) else Modifier
        )
        .height(110.dp)
        .graphicsLayer { alpha = 0.9f }
        .background(backgroundColor.copy(alpha = 0.5f), shape = RoundedCornerShape(12.dp))
        .border(1.dp, backgroundColor.copy(alpha = 0.7f), RoundedCornerShape(12.dp))

    Box(
        modifier = finalModifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF424242)
            )
            reps?.let {
                Text(
                    text = "Reps: $it",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF424242)
                )
            }
            duration?.let {
                Text(
                    text = "Duration: $it",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF424242)
                )
            }
        }
    }
}
