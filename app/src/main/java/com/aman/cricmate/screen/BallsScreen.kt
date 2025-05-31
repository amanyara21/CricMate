package com.aman.cricmate.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aman.cricmate.components.AppHeader
import com.aman.cricmate.components.BallCard
import com.aman.cricmate.components.CalendarSelection
import com.aman.cricmate.components.ReusableClickableCard
import com.aman.cricmate.viewModel.BallsViewModel

@Composable
fun BallsScreen(
    navController: NavController,
    userId: String,
    viewModel: BallsViewModel = hiltViewModel(),
) {
    val ballList = viewModel.ballList
    val selectedDate = viewModel.selectedDate
    LaunchedEffect(Unit) {
        viewModel.fetchBalls(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AppHeader(title = "Bowling Data")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            CalendarSelection(
                title = "Select Date",
                selectedDate = selectedDate,
                onDateSelected = {
                    viewModel.selectedDate = it
                    viewModel.fetchBalls(userId)
                }
            )


            Spacer(modifier = Modifier.height(16.dp))

            ReusableClickableCard(
                text = "See Stats Graph",
                backgroundColor = Color.White,
                buttonText = "See Stats",
                onClick = {
                    navController.navigate("ballStats/${userId}")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(ballList.size) { index ->
                        val ball = ballList[index]
                        BallCard(ball, index+1) {
                            navController.navigate("show3d/${ball._id}")
                        }
                    }
                }
            }
        }
    }
}


