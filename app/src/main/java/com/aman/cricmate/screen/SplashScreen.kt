package com.aman.cricmate.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aman.cricmate.R
import com.aman.cricmate.di.UserSessionManager
import com.aman.cricmate.model.Role
import com.aman.cricmate.viewModel.SplashViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    userSessionManager: UserSessionManager,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val isLoggedIn by userSessionManager.isLoggedIn
    val angle = viewModel.angle
    LaunchedEffect(isLoggedIn) {
        delay(2000)

        if (isLoggedIn) {
            val role = userSessionManager.role.value

            if (role == Role.COACH) {
                navController.navigate("Coach") {
                    popUpTo("Splash") { inclusive = true }
                }
            } else {
                navController.navigate(if (angle == "Side") "Camera" else "Main") {
                    popUpTo("Splash") { inclusive = true }
                }
            }
        } else {
            navController.navigate("Login") {
                popUpTo("Splash") { inclusive = true }
            }
        }
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "CricMate Logo",
            modifier = Modifier
                .size(300.dp)
                .clip(
                    RoundedCornerShape(30)
                )
        )
    }
}
