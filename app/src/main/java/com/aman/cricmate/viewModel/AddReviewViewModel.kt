package com.aman.cricmate.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aman.cricmate.model.PlayerReviewRequest
import com.aman.cricmate.utils.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddReviewViewModel @Inject constructor(
    private val apiService: ApiService,
    private val preferenceHelper: PreferenceHelper
) : ViewModel() {

    var reviewText by mutableStateOf("")
    var isSubmitting by mutableStateOf(false)
    var successMessage by mutableStateOf<String?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    fun submitReview(userId: String) {
        errorMessage=null
        successMessage=null
        viewModelScope.launch {
            try {
                isSubmitting = true
                val token = preferenceHelper.getAuthToken()
                val request = PlayerReviewRequest(userId, reviewText)
                val response = apiService.addPlayerReview(token!!, request)
                if (response.isSuccessful) {
                    successMessage = "Review Added Successfully"
                } else {
                    errorMessage = "Failed to submit review"
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "An error occurred"
            } finally {
                isSubmitting = false
            }
        }
    }
}
