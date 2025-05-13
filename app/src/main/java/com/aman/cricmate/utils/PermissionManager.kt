package com.aman.cricmate.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionManager(
    permissions: List<String>,
    onPermissionsResult: (Map<String, Boolean>) -> Unit
) {
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions)

    LaunchedEffect(key1 = true) {
        multiplePermissionsState.launchMultiplePermissionRequest()
    }

    if (multiplePermissionsState.allPermissionsGranted) {
        onPermissionsResult(permissions.associateWith { true })
    } else if (multiplePermissionsState.shouldShowRationale) {
        onPermissionsResult(
            permissions.associateWith { perm ->
                multiplePermissionsState.permissions.find { it.permission == perm }?.status?.isGranted == true
            }
        )
    }
}
