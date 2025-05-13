package com.aman.cricmate.viewModel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aman.cricmate.model.TestResult
import com.aman.cricmate.utils.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class TestResultViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    var testResults by mutableStateOf<TestResult?>(null)

    var error by mutableStateOf<String?>(null)
    var filter by mutableStateOf<String>("last30days")

    var isLoading by mutableStateOf(false)

    fun fetchResults(fieldName: String, filter: String, userId: String ) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                val response = apiService.getPlayerTestData(fieldName, filter, userId)
                if (response.isSuccessful) {
                    testResults = response.body()!!
                } else {
                    error = JSONObject(response.errorBody()?.string() ?: "").getString("error")
                }
            } catch (e: Exception) {
                error = "Failed: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }
}
