package com.aman.cricmate

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.aman.cricmate.di.UserSessionManager
import com.aman.cricmate.ui.theme.CricMateTheme
import com.aman.cricmate.utils.PermissionManager
import com.aman.cricmate.viewModel.SplashViewModel
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var userSessionManager: UserSessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)

        super.onCreate(savedInstanceState)
        setContent {
            CricMateTheme {
                GetPermission {
                    AppNavigation(userSessionManager)
                }
            }
        }

        if (userSessionManager.name!=null) {
            Log.d("MainActivity", "User is already logged in")
            // Handle navigation or show appropriate UI
        } else {
            Log.d("MainActivity", "User is not logged in")
            // You can handle navigation to login or show splash screen if needed
        }
    }
}


@Composable
fun GetPermission(content: @Composable () -> Unit) {
    val permissionsToRequest = listOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.MANAGE_EXTERNAL_STORAGE
    )

    PermissionManager(
        permissions = permissionsToRequest,
        onPermissionsResult = { results ->
            val permissionsGranted = results.all { it.value }
            if (!permissionsGranted) {
//                Toast.makeText("Hello")
            }
        }
    )

    content()
}




