package com.aman.cricmate.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ProfileAndTest(navController: NavController, userId: String) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB)))),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PlayerProfileScreen(navController = navController, userId = userId, isMe = false)
        }
        item {
            TestListScreen(navController = navController, userId = userId)
        }
    }
}
