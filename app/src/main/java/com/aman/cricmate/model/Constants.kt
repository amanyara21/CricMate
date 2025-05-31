package com.aman.cricmate.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp

object Constants {
    const val serverUrl = ""
    val HomeBottomNavItems = listOf(
        BottomNavItem("home", "Home", Icons.Filled.Home),
        BottomNavItem("data", "Data", Icons.AutoMirrored.Filled.List),
        BottomNavItem("event", "Event", Icons.Filled.EventAvailable),
        BottomNavItem("review", "Review", Icons.Filled.Star)
    )
    val CoachBottomNavItems = listOf(
        BottomNavItem("home", "Home", Icons.Filled.Home),
        BottomNavItem("data", "Data", Icons.AutoMirrored.Filled.List),
    )
    val testItems = listOf(
        "100 Meter Race" to "hundredMeterTime",
        "Yoyo Test" to "yoYoTestLevel",
        "Beep Test" to "yoYoTestLevel",
        "Shoulder Flexibility" to "shoulderFlexibilityCm",
        "Core Strength" to "coreStrength"
    ).toMap()


}