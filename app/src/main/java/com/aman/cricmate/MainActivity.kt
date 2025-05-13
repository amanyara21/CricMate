package com.aman.cricmate

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.aman.cricmate.di.UserSessionManager
import com.aman.cricmate.ui.theme.CricMateTheme
import com.aman.cricmate.utils.PermissionManager
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
    }
}


@Composable
fun GetPermission(content: @Composable () -> Unit) {
    val context = LocalContext.current
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
                Toast.makeText(context,"Hello", Toast.LENGTH_SHORT).show()
            }
        }
    )

    content()
}




