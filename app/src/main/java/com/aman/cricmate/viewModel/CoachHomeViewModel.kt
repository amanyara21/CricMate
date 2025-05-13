package com.aman.cricmate.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aman.cricmate.model.User
import com.aman.cricmate.utils.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoachHomeViewModel @Inject constructor(
    private val apiService: ApiService,
    private val preferenceHelper: PreferenceHelper
): ViewModel() {

    var players by mutableStateOf<List<User>>(emptyList())
    var isLoading by mutableStateOf(false)

    var errorMessage by mutableStateOf<String?>(null)

    init{
        Log.d("Hello", "Hello")
        getAllPlayers()
    }

    private fun getAllPlayers(){
        viewModelScope.launch {
            isLoading = true
            try {
                Log.d("Hello", "Hello")
                val token = preferenceHelper.getAuthToken()
                val response = apiService.getAllPlayers(token!!)
                if(response.isSuccessful){
                    players= response.body()!!
                }
            } catch (e: Exception) {

            } finally {
                isLoading = false
            }
        }
    }
}