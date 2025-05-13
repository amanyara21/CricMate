package com.aman.cricmate.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aman.cricmate.model.ApiError
import com.aman.cricmate.model.LoginRequest
import com.aman.cricmate.model.LoginResponse
import com.aman.cricmate.model.Role
import com.aman.cricmate.model.SignupRequest
import com.aman.cricmate.utils.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val apiService: ApiService,
    private val preferenceHelper: PreferenceHelper
): ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var name by mutableStateOf("")
    var profilePhoto by mutableStateOf<Uri?>(null)
    var isLoading by mutableStateOf(false)
    var signupState by mutableStateOf<LoginResponse?>(null)
    var errorMessage by mutableStateOf<ApiError?>(null)
    var role by mutableStateOf(Role.PLAYER)

    fun onRoleSelected(selected: Role) {
        role = selected
    }
    fun onNameChange(value: String) {
        name = value
    }

    fun onEmailChange(value: String) {
        email = value
    }
    fun onPasswordChange(value: String) {
        password = value
    }
    fun onProfileImageChange(uri: Uri?) {
        profilePhoto = uri
    }

    fun signup(){
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            errorMessage =ApiError("Please fill all fields before signing up.")
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val contentResolver = preferenceHelper.context.contentResolver
                val imagePart = profilePhoto?.let {
                    val inputStream = contentResolver.openInputStream(it)
                    val file = File.createTempFile("temp", ".jpg", preferenceHelper.context.cacheDir)
                    inputStream?.use { input ->
                        file.outputStream().use { output -> input.copyTo(output) }
                    }

                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("profilePhoto", file.name, requestFile)
                }

                val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
                val emailPart = email.toRequestBody("text/plain".toMediaTypeOrNull())
                val passwordPart = password.toRequestBody("text/plain".toMediaTypeOrNull())
                val rolePart = role.name.toRequestBody("text/plain".toMediaTypeOrNull())

                val response = apiService.signup(
                    imagePart,
                    namePart,
                    emailPart,
                    passwordPart,
                    rolePart
                )
                if (response.isSuccessful) {
                    signupState = response.body()
                    signupState?.token?.let { token ->
                        preferenceHelper.saveAuthToken(token)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val error = try {
                        JSONObject(errorBody ?: "").getString("error")
                    } catch (e: Exception) {
                        "Something went wrong"
                    }
                    errorMessage = ApiError(error)
                }
            } catch (e: IOException) {
                errorMessage = ApiError("Network error: Please check your connection.")
            } catch (e: Exception) {
                errorMessage = ApiError("Unexpected error: ${e.localizedMessage}")
            } finally {
                isLoading = false
            }
        }

    }
}