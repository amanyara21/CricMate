package com.aman.cricmate.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aman.cricmate.model.DailyExercise
import com.aman.cricmate.model.PlayerReviewResponse
import com.aman.cricmate.model.StatsData
import com.aman.cricmate.utils.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val apiService: ApiService,
    private val preferenceHelper: PreferenceHelper
) : ViewModel() {

    var ballStats by mutableStateOf<StatsData?>(null)
    var playerReviews by mutableStateOf<PlayerReviewResponse?>(null)
    var todaysExerices by mutableStateOf<DailyExercise?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    fun getBallStats(userId: String) {
        viewModelScope.launch {
            try {
                val date = Date()
                val response = apiService.getDateWiseStats(userId, date)
                if (response.isSuccessful) {
                    ballStats = response.body()!!
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
                val response = apiService.getPlayerReview(token!!, "yesterday")
                if (response.isSuccessful) {
                    playerReviews = response.body()!!
                } else {
                    errorMessage = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    fun getTodaysExercise() {
        viewModelScope.launch {
            try {
                val response = apiService.getTodaysExercise()
                if (response.isSuccessful) {
                    todaysExerices = response.body()!!
                } else {
                    errorMessage = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }
}

