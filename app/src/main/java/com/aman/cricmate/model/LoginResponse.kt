package com.aman.cricmate.model

data class LoginResponse(
    val success: Boolean,
    val token: String,
    val role: Role,
)
data class ApiError(
    var error: String
)
