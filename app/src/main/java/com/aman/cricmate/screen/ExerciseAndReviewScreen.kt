package com.aman.cricmate.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aman.cricmate.components.AppHeader
import com.aman.cricmate.components.ExerciseCard
import com.aman.cricmate.components.GlassCard
import com.aman.cricmate.viewModel.ExerciseAndReviewViewModel

@Composable
fun ExerciseAndReviewScreen(viewModel: ExerciseAndReviewViewModel = hiltViewModel()) {
    val todaysExercise = viewModel.todaysExercises
    val playerReviews = viewModel.playerReviews
    val errorMessage = viewModel.errorMessage

    LaunchedEffect(Unit) {
        viewModel.getTodaysExercise()
        viewModel.getPlayerReview()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AppHeader("Exercises & Reviews")
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Show Error Message if any
            errorMessage?.let {
                item {
                    Text(text = it, color = Color.Red)
                }
            }

            // Today's Exercises
            if (todaysExercise != null) {
                item {
                    Text(
                        text = "Today's Exercises",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF311B92)
                    )
                }
                items(todaysExercise.exercises) {
                    ExerciseCard(
                        name = it.name,
                        reps = it.reps,
                        duration = it.duration,
                        backgroundColor = Color(0xFFB3E5FC),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (playerReviews != null) {
                item {
                    Text(
                        text = "Coach Reviews",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF311B92)
                    )
                }
                items(playerReviews.reviews) {
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = it.review,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF424242)
                            )
                            Text(
                                text = "- ${it.coach.name}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF757575)
                            )
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}
