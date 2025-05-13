package com.aman.cricmate.model


data class User(
    val _id:String,
    val name:String,
    val email:String,
    val role:Role,
    val profilePhoto:String?
)
