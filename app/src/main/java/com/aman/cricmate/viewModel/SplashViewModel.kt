package com.aman.cricmate.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    var angle by mutableStateOf("")

    init {
        initUserSession()
    }

    private fun initUserSession() {
        angle = authRepository.getAngle()!!
        viewModelScope.launch {
            authRepository.getUser()
        }
    }
}
