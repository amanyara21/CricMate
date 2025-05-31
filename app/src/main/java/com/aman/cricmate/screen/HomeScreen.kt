package com.aman.cricmate.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.aman.cricmate.components.ExerciseCard
import com.aman.cricmate.components.FullStatCardWithProgress
import com.aman.cricmate.components.GlassCard
import com.aman.cricmate.components.PracticeCardGlass
import com.aman.cricmate.components.StatCardGlass
import com.aman.cricmate.di.UserSessionManager
import com.aman.cricmate.model.Constants
import com.aman.cricmate.viewModel.HomeScreenViewModel


@Composable
fun HomeScreen(
    outerNavController: NavController,
    userSessionManager: UserSessionManager,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val ballStats = viewModel.ballStats
    val playerReviews = viewModel.playerReviews
    val todaysExercise = viewModel.todaysExerices
    LaunchedEffect(Unit) {
        userSessionManager.id.value!!.let { viewModel.getBallStats(userId = it) }
        viewModel.getPlayerReview()
        viewModel.getTodaysExercise()
    }


    val screenGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFFAF9F6), Color(0xFFE8F0F2)),
        tileMode = TileMode.Clamp
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = screenGradient)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { outerNavController.navigate("userProfile/${userSessionManager.id.value!!}") }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = Constants.serverUrl + userSessionManager.profilePhoto.value,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color(0xFFB39DDB), CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Welcome back,",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF5B5B5B)
                    )
                    Text(
                        text = userSessionManager.name.value ?: "Aman",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF311B92)
                    )
                }
            }
        }

        Text(
            text = "Your Bowling Stats",
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF311B92)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.clickable {
                outerNavController.navigate("ballScreen/${userSessionManager.id.value}")
            }
        ) {
            FullStatCardWithProgress(
                title = "Balls Bowled",
                completed = ballStats?.totalBalls ?: 0,
                total = 50,
                backgroundColor = Color(0xFFCE93D8)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatCardGlass(
                    title = "Yorkers",
                    value = ballStats?.yorkers?.toString() ?: "0",
                    backgroundColor = Color(0xFF80CBC4)
                )
                StatCardGlass(
                    title = "Short Balls",
                    value = ballStats?.short?.toString() ?: "0",
                    backgroundColor = Color(0xFFFFF59D)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatCardGlass(
                    title = "Length Balls",
                    value = ballStats?.goodLength?.toString() ?: "0",
                    backgroundColor = Color(0xFF80DEEA)
                )
                StatCardGlass(
                    title = "Bouncers",
                    value = ballStats?.bouncers?.toString() ?: "0",
                    backgroundColor = Color(0xFFFFAB91)
                )
            }
        }


        PracticeCardGlass(onClick = { outerNavController.navigate("camera") })

        Text(
            text = "Today's Exercises",
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF311B92)
        )
        if (todaysExercise != null) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(todaysExercise.exercises) {
                    ExerciseCard(it.name, it.reps, it.duration, Color(0xFFB3E5FC))
                }
            }
        }


        if (playerReviews != null && playerReviews.reviews.isNotEmpty()) {
            Text(
                text = "Yesterday's Coach Review",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF311B92)
            )
            LazyRow {
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
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}




