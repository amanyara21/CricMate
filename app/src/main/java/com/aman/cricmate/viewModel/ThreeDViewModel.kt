package com.aman.cricmate.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aman.cricmate.model.BallResponse
import com.aman.cricmate.utils.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sceneview.math.Position
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThreeDViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {
    var trackingPoints by mutableStateOf<List<Position>>(emptyList())
//    var ballResponse by mutableStateOf<BallResponse?>(null)
    var error by mutableStateOf<String?>(null)
    fun getBallDetails(ballId: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getBallById(ballId)
                if (response.isSuccessful) {
                    trackingPoints = response.body()?.coordinates ?: emptyList()
                }
            } catch (e: Exception) {
                error=e.message
            }
        }
    }
}
