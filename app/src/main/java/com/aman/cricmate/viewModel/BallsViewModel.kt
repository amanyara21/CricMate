package com.aman.cricmate.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aman.cricmate.model.BallResponse
import com.aman.cricmate.utils.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class BallsViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    var selectedDate by mutableStateOf<Date?>(Date())
    var ballList by mutableStateOf<List<BallResponse>>(emptyList())
    var isLoading by mutableStateOf(false)


    fun fetchBalls(userId: String) {
        selectedDate?.let { date ->
            viewModelScope.launch {
                isLoading = true
                val response = apiService.getBallsByDate(userId, date)
                ballList = if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    emptyList()
                }
                isLoading = false
            }
        }
    }
}