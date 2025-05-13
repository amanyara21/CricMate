package com.aman.cricmate.viewModel


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aman.cricmate.model.DateStatsData
import com.aman.cricmate.utils.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BallStatsViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    var filter by mutableStateOf("last7days")
    var stats by mutableStateOf<List<DateStatsData>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    private val sampleStatsData = listOf(
        DateStatsData(
            date = "2023-04-01",
            totalBalls = 120,
            avgSpeed = 135.5,
            yorkers = 15,
            bouncers = 8,
            short = 12,
            goodLength = 40
        ),
        DateStatsData(
            date = "2023-04-02",
            totalBalls = 100,
            avgSpeed = 140.2,
            yorkers = 10,
            bouncers = 6,
            short = 14,
            goodLength = 35
        ),
        DateStatsData(
            date = "2023-04-03",
            totalBalls = 130,
            avgSpeed = 138.8,
            yorkers = 18,
            bouncers = 9,
            short = 10,
            goodLength = 45
        ),
        DateStatsData(
            date = "2023-04-04",
            totalBalls = 110,
            avgSpeed = 130.3,
            yorkers = 14,
            bouncers = 7,
            short = 8,
            goodLength = 40
        ),
        DateStatsData(
            date = "2023-04-05",
            totalBalls = 125,
            avgSpeed = 137.0,
            yorkers = 16,
            bouncers = 10,
            short = 13,
            goodLength = 42
        ),
        DateStatsData(
            date = "2023-04-06",
            totalBalls = 105,
            avgSpeed = 142.5,
            yorkers = 12,
            bouncers = 5,
            short = 9,
            goodLength = 39
        ),
        DateStatsData(
            date = "2023-04-07",
            totalBalls = 140,
            avgSpeed = 145.0,
            yorkers = 20,
            bouncers = 11,
            short = 7,
            goodLength = 48
        )
    )

    fun fetchStats(userId: String) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                val response = apiService.getStatsByFilter(userId, filter)
                stats = sampleStatsData
                if (response.isSuccessful) {
//                    stats = response.body()?.stats ?: emptyList()
                } else {
                    error = "Failed to load stats"
                }
            } catch (e: Exception) {
                Log.d("Stats", e.message.toString())
                error = e.localizedMessage
            } finally {
                isLoading = false
            }
        }
    }
}