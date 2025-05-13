package com.aman.cricmate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.aman.cricmate.di.UserSessionManager
import com.aman.cricmate.screen.AddEventScreen
import com.aman.cricmate.screen.AddPlayerDetails
import com.aman.cricmate.screen.AddPlayerReview
import com.aman.cricmate.screen.AddTestData
import com.aman.cricmate.screen.BallStatsScreen
import com.aman.cricmate.screen.BallsScreen
import com.aman.cricmate.screen.CameraScreen
import com.aman.cricmate.screen.CoachScreen
import com.aman.cricmate.screen.CoachSelectionScreen
import com.aman.cricmate.screen.EventPreviewScreen
import com.aman.cricmate.screen.LoginScreen
import com.aman.cricmate.screen.MainScreen
import com.aman.cricmate.screen.PlayerProfileScreen
import com.aman.cricmate.screen.ProfileAndTest
import com.aman.cricmate.screen.SignupScreen
import com.aman.cricmate.screen.SplashScreen
import com.aman.cricmate.screen.StatsScreen
import com.aman.cricmate.screen.TestListScreen
import com.aman.cricmate.screen.ThreeDBallView
import com.aman.cricmate.viewModel.AddPlayerViewModel

@Composable
fun AppNavigation(userSessionManager: UserSessionManager) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "splash") {
        composable("Splash") {
            SplashScreen(navController, userSessionManager)
        }
        composable("Login") {
            LoginScreen(navController)
        }
        composable("Main") {
            MainScreen(navController, userSessionManager)
        }
        composable("Signup") {
            SignupScreen(navController)
        }
        composable("Camera") {
            CameraScreen(userSessionManager)
        }
        composable("Coach") {
            CoachScreen(navController)
        }
        composable("TestList/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            TestListScreen(navController, userId = userId)
        }
        composable("stats/{field}/{userId}") { backStackEntry ->
            val field = backStackEntry.arguments?.getString("field") ?: ""
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            StatsScreen(userId = userId, field = field)
        }
        composable("userProfile/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            PlayerProfileScreen(navController, userId, true)
        }
        composable("addEvent") {
            AddEventScreen()
        }
        composable("addData/{field}") { backStackEntry ->
            val field = backStackEntry.arguments?.getString("field") ?: ""
            AddTestData(field)
        }
        composable("eventpreview/{userId}/{eventId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            EventPreviewScreen(navController, userId, eventId)
        }
        composable("profilewithtest/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ProfileAndTest(navController, userId)
        }
        composable("review/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            AddPlayerReview(userId)
        }
        composable("ballScreen/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            BallsScreen(navController, userId)
        }
        composable("ballStats/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            BallStatsScreen(userId)
        }
        composable("show3d/{ballId}") { backStackEntry ->
            val ballId = backStackEntry.arguments?.getString("ballId") ?: ""
            ThreeDBallView(ballId)
        }
        navigation(
            route = "add_player_flow",
            startDestination = "AddDetails"
        ) {
            composable("AddDetails") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("add_player_flow")
                }
                val viewModel: AddPlayerViewModel = hiltViewModel(parentEntry)
                AddPlayerDetails(navController, viewModel)
            }

            composable("Coaches") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("add_player_flow")
                }
                val viewModel: AddPlayerViewModel = hiltViewModel(parentEntry)
                CoachSelectionScreen(navController, viewModel)
            }
        }

    }

}