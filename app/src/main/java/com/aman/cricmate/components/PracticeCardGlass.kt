package com.aman.cricmate.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun PracticeCardGlass(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .graphicsLayer {
                alpha = 0.95f
                shadowElevation = 10f
                shape = RoundedCornerShape(20.dp)
                clip = true
            }
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFD1C4E9).copy(alpha = 0.6f),
                        Color(0xFFB39DDB).copy(alpha = 0.6f)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                1.5.dp,
                Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.8f),
                        Color.White.copy(alpha = 0.3f)
                    )
                ),
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Practice with the Camera",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF311B92)
            )

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2)),
                shape = RoundedCornerShape(50),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Start Practice", color = Color.White)
            }
        }
    }
}