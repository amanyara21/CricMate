package com.aman.cricmate.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aman.cricmate.model.ResultEntry
import com.aman.cricmate.model.TestResult
import com.aman.cricmate.model.User
import com.aman.cricmate.model.Constants
import com.aman.cricmate.utils.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class PlayerTestViewModel @Inject constructor(
    private val apiService: ApiService,
    private val preferenceHelper: PreferenceHelper
) : ViewModel() {

    var playerList by mutableStateOf<List<User>>(emptyList())
    var resultsMap = mutableStateMapOf<String, String>()
    var isSubmitting by mutableStateOf(false)
    var date by mutableStateOf<Date>(Date())

    fun onDateChange(newDate: Date){
        date= newDate
    }
    fun fetchPlayers() {
        viewModelScope.launch {
            try {
                val token = preferenceHelper.getAuthToken() ?: return@launch
                val response = apiService.getAllPlayers(token)
                if (response.isSuccessful) {
                    playerList = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun submitResults(fieldName: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                isSubmitting = true
                val token = preferenceHelper.getAuthToken() ?: return@launch

                val results = resultsMap.mapNotNull { (userId, value) ->
                    ResultEntry(
                        userId = userId,
                        testDate = date,
                        result = value
                    )
                }

                if (results.isEmpty()) {
                    isSubmitting = false
                    return@launch
                }

                val testResult = TestResult(
                    fieldName = Constants.testItems[fieldName]!!,
                    results = results
                )

                val response = apiService.addPlayerTestData(token, testResult)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    Log.e("Submit", "Failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isSubmitting = false
            }
        }
    }
}

