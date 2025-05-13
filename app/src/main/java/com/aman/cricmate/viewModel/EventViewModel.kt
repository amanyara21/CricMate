package com.aman.cricmate.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aman.cricmate.model.Event
import com.aman.cricmate.model.User
import com.aman.cricmate.utils.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val apiService: ApiService,
    private val preferenceHelper: PreferenceHelper
) : ViewModel() {

    private var allEvents by mutableStateOf<List<Event>>(emptyList())
    var selectedEvent by mutableStateOf<Event?>(null)
    var applicants by mutableStateOf<List<User>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var filteredEvents by mutableStateOf<List<Event>>(emptyList())
    var eventFilterOption by mutableStateOf("All")

    fun loadAllEvents() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = apiService.getAllEvents()
                if (response.isSuccessful) {
                    response.body()?.let {
                        allEvents = it
                        filteredEvents = it
                    }
                } else {
                    errorMessage = "Failed to fetch events"
                }
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun setEventFilter(filter: String, currentUserId: String) {
        eventFilterOption = filter
        filteredEvents = if (filter == "My Events") {
            allEvents.filter { it.createdBy._id == currentUserId }
        } else {
            allEvents
        }
    }

    fun applyToEvent(eventId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val token = preferenceHelper.getAuthToken()
                val response = apiService.applyToEvent(token!!, eventId)
                onResult(response.isSuccessful)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun loadApplicants(eventId: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = apiService.getApplicants(eventId)
                if (response.isSuccessful) {
                    applicants = response.body() ?: emptyList()
                } else {
                    errorMessage = "Failed to load applicants"
                }
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun getEventById(eventId: String) {
        isLoading = true
        viewModelScope.launch {
            try {
                val response = apiService.getEventById(eventId)
                if (response.isSuccessful) {
                    selectedEvent = response.body()!!
                } else {
                    errorMessage = "Failed to load applicants"
                }
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

}
