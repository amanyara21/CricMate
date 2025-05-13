package com.aman.cricmate.model

import java.util.Date

data class PlayerDetails(
    val user: User,
    val dob: Date,
    val bowlingType: String,
    val bowlingArm: String,
    val coaches: List<User>,
)
