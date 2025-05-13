package com.aman.cricmate.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aman.cricmate.model.AddDetailsRequest
import com.aman.cricmate.model.AddDetailsResponse
import com.aman.cricmate.model.Coach
import com.aman.cricmate.utils.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddPlayerViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    var dob by mutableStateOf<Date?>(null)

    fun updateDob(date: Date) {
       dob=date
    }
    var armType by mutableStateOf("")
    var bowlingType by mutableStateOf("")
    var selectedCoaches = mutableStateListOf<Coach>()
    var addResponse by mutableStateOf<AddDetailsResponse?>(null)


    private var coachList by mutableStateOf<List<Coach>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    var coachSearchQuery by mutableStateOf("")

    val filteredCoaches: List<Coach>
        get() = coachList.filter { it.name.contains(coachSearchQuery, ignoreCase = true) }

    init {
        fetchCoaches()
    }

    fun toggleCoachSelection(coach: Coach) {
        if (selectedCoaches.contains(coach)) {
            selectedCoaches.remove(coach)
        } else {
            selectedCoaches.add(coach)
        }
    }

    private fun fetchCoaches() {
        isLoading = true
        viewModelScope.launch {
            try {
                val response = apiService.getAllCoaches()
                if(response.isSuccessful){
                    coachList= response.body()!!
                }
                isLoading = false
            } catch (e: Exception) {
                isLoading = false
                errorMessage = "Failed to load coaches: ${e.localizedMessage}"
            }
        }
    }
    fun addDetail(context:Context){
        Log.d("token", "Is Calling")
        isLoading=true
        errorMessage=null
        viewModelScope.launch {
            try {
                val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                val token= sharedPreferences.getString("auth_token", null)

                val addDetailsRequest = AddDetailsRequest(
                    dob =dob!!,
                    bowlingType = bowlingType,
                    bowlingArm = armType,
                    coaches = selectedCoaches
                )
                val response = apiService.addDetail(token!!, addDetailsRequest)
                if(response.isSuccessful){
                    addResponse=response.body()!!
                    Log.d("token", response.body()!!.toString())
                }
                isLoading = false
            } catch (e: Exception) {
                Log.d("token", e.toString())
                isLoading = false
                errorMessage = "Failed to load coaches: ${e.localizedMessage}"
            }
        }
    }
}

