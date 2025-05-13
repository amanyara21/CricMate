package com.aman.cricmate.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aman.cricmate.components.AppHeader
import com.aman.cricmate.model.Constants.testItems
import com.aman.cricmate.components.ReusableClickableCard

@Composable
fun TestListScreen(navController: NavController, userId: String) {

    val cardColors = listOf(
        Color(0xFFE1BEE7),
        Color(0xFFB2DFDB),
        Color(0xFFFFF9C4),
        Color(0xFFFFCCBC),
        Color(0xFFB3E5FC),
        Color(0xFFD7CCC8)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AppHeader("Player Test Categories")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ReusableClickableCard(
                text = "Bowling Data",
                backgroundColor = Color(0xFFB3E5FC),
                buttonText = "Show Data"
            ) {
                navController.navigate("ballScreen/${userId}")
            }

            testItems.entries.forEachIndexed { index, entry ->
                val backgroundColor = cardColors[index % cardColors.size]
                ReusableClickableCard(
                    text = entry.key,
                    backgroundColor = backgroundColor,
                    buttonText = "Show Data"
                ) {
                    navController.navigate("stats/${entry.key}/${userId}")
                }
            }
        }
    }
}


