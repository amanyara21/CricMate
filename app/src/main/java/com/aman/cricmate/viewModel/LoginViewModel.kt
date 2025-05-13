package com.aman.cricmate.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aman.cricmate.model.LoginRequest
import com.aman.cricmate.model.LoginResponse
import com.aman.cricmate.utils.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: ApiService,
    private val preferenceHelper: PreferenceHelper
): ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var alreadyLogin by mutableStateOf(false)
    var sideAngle by mutableStateOf(false)
    var loginState by mutableStateOf<LoginResponse?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }


    fun login(sideAngle: Boolean) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = apiService.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    loginState = response.body()
                    loginState?.token?.let { token ->
                        preferenceHelper.saveAuthToken(token)
                        if (sideAngle) {
                            preferenceHelper.saveAngle("Side")
                        }
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorMessage = when (response.code()) {
                        400 -> "Invalid credentials"
                        401 -> "Unauthorized access"
                        500 -> "Server error"
                        else -> "Error ${response.code()}: ${errorBody ?: "Unknown error"}"
                    }
                }
            } catch (e: IOException) {
                Log.d("Response", e.toString())
                errorMessage = "Network error: Please check your connection."
            } catch (e: Exception) {
                errorMessage = "Unexpected error: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

}