package com.aman.cricmate.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aman.cricmate.di.UserSessionManager
import com.aman.cricmate.utils.BottomNavigationBar


@Composable
fun MainScreen(navController: NavController, userSessionManager: UserSessionManager) {
    val innerNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = innerNavController, type = "Home")
        }
    ) { padding ->
        NavHostContainer(
            navController = innerNavController,
            padding = padding,
            outerNavController = navController,
            userSessionManager
        )
    }
}

@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues,
    outerNavController: NavController,
    userSessionManager: UserSessionManager
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(
            start = padding.calculateStartPadding(LayoutDirection.Ltr),
            end = padding.calculateEndPadding(LayoutDirection.Ltr),
            top = padding.calculateTopPadding(),
            bottom = 0.dp
        )
    ) {
        composable("home") { HomeScreen(outerNavController, userSessionManager) }
        composable("data") { TestListScreen(outerNavController, userSessionManager.id.value!!) }
        composable("event") { EventListScreen(outerNavController, userSessionManager) }
        composable("review") { ExerciseAndReviewScreen() }
    }
}

