package com.aman.cricmate.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aman.cricmate.model.DailyExercise
import com.aman.cricmate.model.PlayerReviewResponse
import com.aman.cricmate.utils.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseAndReviewViewModel @Inject constructor(
    private val apiService: ApiService,
    private val preferenceHelper: PreferenceHelper
) : ViewModel() {

    var todaysExercises by mutableStateOf<DailyExercise?>(null)
    var playerReviews by mutableStateOf<PlayerReviewResponse?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    fun getTodaysExercise() {
        viewModelScope.launch {
            try {
                val response = apiService.getTodaysExercise()
                if (response.isSuccessful) {
                    todaysExercises = response.body()
                } else {
                    errorMessage = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    fun getPlayerReview() {
        viewModelScope.launch {
            try {
                val token = preferenceHelper.getAuthToken()
                Log.d("review", token!!)
                val response = apiService.getPlayerReview(token, "last7days")
                Log.d("review", response.body().toString())
                if (response.isSuccessful) {
                    playerReviews = response.body()
                } else {
                    errorMessage = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }
}

