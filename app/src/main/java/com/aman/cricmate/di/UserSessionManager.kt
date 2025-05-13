package com.aman.cricmate.di

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.aman.cricmate.model.Role
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSessionManager @Inject constructor() {
    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> get() = _isLoggedIn

    private val _id = mutableStateOf<String?>(null)
    val id: State<String?> get() = _id

    private val _profilePhoto = mutableStateOf<String?>(null)
    val profilePhoto: State<String?> get() = _profilePhoto

    private val _name = mutableStateOf<String?>(null)
    val name: State<String?> get() = _name

    private val _email = mutableStateOf<String?>(null)
    val email: State<String?> get() = _email

    private val _role = mutableStateOf<Role?>(null)
    val role: State<Role?> get() = _role

    fun setUser(id: String, name: String, email: String, role: Role, profilePhoto: String?) {
        _id.value = id
        _name.value = name
        _email.value = email
        _role.value = role
        _profilePhoto.value = profilePhoto
        _isLoggedIn.value = true
    }

    fun clear() {
        _id.value = null
        _name.value = null
        _email.value = null
        _role.value = null
        _profilePhoto.value = null
        _isLoggedIn.value = false
    }
}
