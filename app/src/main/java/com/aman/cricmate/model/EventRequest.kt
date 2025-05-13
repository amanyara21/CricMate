package com.aman.cricmate.model

import java.util.Date

data class Event(
    val _id:String,
    val title: String,
    val description: String,
    val location: String,
    val date: Date,
    val createdBy: User,
    val applicants: List<String> = emptyList()
)
data class EventRequest(
    val title: String,
    val description: String,
    val location: String,
    val date: Date,
)

data class ApiResponse(
    val success: Boolean
)