package com.aman.cricmate.viewModel

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceHelper @Inject constructor(@ApplicationContext val context: Context) {
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()
    }

    fun saveAngle(angle: String) {
        prefs.edit().putString("angle", angle).apply()
    }

    fun getAuthToken(): String? {
        return prefs.getString("auth_token", null)
    }
    fun getAngle(): String? {
        return prefs.getString("angle", "front")
    }
    fun deletePrefrences() {
        prefs.edit().remove("auth_token").apply()
        prefs.edit().remove("angle").apply()
    }

}
