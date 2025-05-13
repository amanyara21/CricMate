package com.aman.cricmate.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aman.cricmate.model.PlayerDetails
import com.aman.cricmate.utils.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val apiService: ApiService,
    val preferenceHelper: PreferenceHelper
) : ViewModel() {
    var logout by mutableStateOf<Boolean>(false)

    private val _playerDetails = MutableStateFlow<PlayerDetails?>(null)
    val playerDetails: StateFlow<PlayerDetails?> = _playerDetails

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun getPlayerDetails(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = apiService.getDetails(id)
                if (response.isSuccessful) {
                    _playerDetails.value = response.body()
                } else {
                    _error.value = "Error: ${response.code()} ${response.message()}"
                    Log.d("Error", response.errorBody()?.string()!!)
                }
            } catch (e: Exception) {
                _error.value = "Exception: ${e.message}"
                Log.d("Error", e.toString())
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun userLogout(){
        try {
            preferenceHelper.deletePrefrences()
            logout=true
        }catch (e:Exception){

        }
    }
}
