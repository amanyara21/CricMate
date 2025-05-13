package com.aman.cricmate.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aman.cricmate.model.EventRequest
import com.aman.cricmate.utils.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddEventViewModel @Inject constructor(
    private val apiService: ApiService,
    private val preferenceHelper: PreferenceHelper
) : ViewModel() {

    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var location by mutableStateOf("")
    var date by mutableStateOf<Date?>(null)
    var isLoading by mutableStateOf(false)
    var successMessage by mutableStateOf<String?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    fun onTitleChange(newTitle: String) {
        title = newTitle
    }

    fun onDescriptionChange(newDescription: String) {
        description = newDescription
    }

    fun onLocationChange(newLocation: String) {
        location = newLocation
    }

    fun onDateChange(newDate: Date) {
        date = newDate
    }

    fun createEvent() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            successMessage = null

            try {
                val event = EventRequest(
                    title = title,
                    description = description,
                    location = location,
                    date = date!!
                )
                val token = preferenceHelper.getAuthToken()
                val response = apiService.createEvent(token!!, event)
                if (response.isSuccessful) {
                    successMessage = "Event created successfully!"
                    clearFields()
                } else {
                    errorMessage = "Failed to create event"
                }
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    private fun clearFields() {
        title = ""
        description = ""
        location = ""
        date = null
    }
}
