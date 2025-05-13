package com.aman.cricmate.model

import java.util.Date

data class AddDetailsRequest(
    val dob: Date,
    val bowlingType: String,
    val bowlingArm:String,
    val coaches: List<Coach>
)
data class AddDetailsResponse(
    val success:Boolean
)
