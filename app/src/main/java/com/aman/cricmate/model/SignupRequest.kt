package com.aman.cricmate.model

import android.net.Uri

data class SignupRequest (
    val name: String,
    val email:String,
    val password: String,
    val role: Role,
    val profilePhoto: Uri?
)
