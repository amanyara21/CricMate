package com.aman.cricmate.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aman.cricmate.components.AppHeader
import com.aman.cricmate.components.ReusableClickableCard
import com.aman.cricmate.viewModel.CoachHomeViewModel

@Composable
fun CoachHomeScreen(
    navController: NavController,
    viewModel: CoachHomeViewModel = hiltViewModel()
) {
    val players = viewModel.players
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    val cardColors = listOf(
        Color(0xFFB2EBF2),
        Color(0xFFDCEDC8),
        Color(0xFFF8BBD0),
        Color(0xFFFFF9C4),
        Color(0xFFD1C4E9),
        Color(0xFFFFCCBC)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AppHeader("CricMate Coach")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(vertical = 16.dp))
            }

            errorMessage?.let {
                Text(text = it, color = Color.Red)
            }

            LazyColumn {
                itemsIndexed(players) { index, player ->
                    val backgroundColor = cardColors[index % cardColors.size]

                    ReusableClickableCard(
                        text = player.name,
                        backgroundColor = backgroundColor,
                        buttonText = "Show Data",
                        showAddReviewButton = true,
                        onAddReviewClick = { navController.navigate("review/${player._id}") }
                    ) {
                        navController.navigate("testlist/${player._id}")
                    }
                }
            }
        }
    }
}



