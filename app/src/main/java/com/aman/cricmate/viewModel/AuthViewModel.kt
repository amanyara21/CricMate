package com.aman.cricmate.viewModel

import com.aman.cricmate.di.UserSessionManager
import com.aman.cricmate.utils.ApiService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val preferenceHelper: PreferenceHelper,
    private val userSessionManager: UserSessionManager
) {
    suspend fun getUser() {
        val token = preferenceHelper.getAuthToken()
        if (token != null) {
            try {
                val response = apiService.getUser(token)
                if (response.isSuccessful) {
                    val user = response.body()

                    userSessionManager.setUser(
                        user?._id!!,
                        user.name,
                        user.email,
                        user.role,
                        user.profilePhoto
                    )
                } else {
                }
            } catch (e: Exception) {
            }
        }
    }

    fun getAngle(): String? {
        return preferenceHelper.getAngle()
    }
}
