package com.example.data.local

import android.content.Context
import android.content.SharedPreferences

data class UserSession(
    val email: String = "",
    val phone: String = "",
    val isLoggedIn: Boolean = false,
    val isPremium: Boolean = false,
    val verificationCode: String = ""
)

class UserSessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("etisalat_user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_EMAIL = "key_email"
        private const val KEY_PHONE = "key_phone"
        private const val KEY_LOGGED_IN = "key_logged_in"
        private const val KEY_PREMIUM = "key_premium"
        private const val KEY_CODE = "key_code"
    }

    fun getSession(): UserSession {
        val email = prefs.getString(KEY_EMAIL, "") ?: ""
        val phone = prefs.getString(KEY_PHONE, "01108596441") ?: "01108596441"
        val isLoggedIn = prefs.getBoolean(KEY_LOGGED_IN, false)
        val isPremium = prefs.getBoolean(KEY_PREMIUM, false)
        var code = prefs.getString(KEY_CODE, "") ?: ""
        if (code.isBlank()) {
            code = "ET-" + (100000..999999).random().toString()
            prefs.edit().putString(KEY_CODE, code).apply()
        }
        return UserSession(
            email = email,
            phone = phone,
            isLoggedIn = isLoggedIn,
            isPremium = isPremium,
            verificationCode = code
        )
    }

    fun saveLogin(email: String, phone: String) {
        prefs.edit()
            .putString(KEY_EMAIL, email)
            .putString(KEY_PHONE, phone)
            .putBoolean(KEY_LOGGED_IN, true)
            .apply()
    }

    fun setPremiumActivated(activated: Boolean) {
        prefs.edit()
            .putBoolean(KEY_PREMIUM, activated)
            .apply()
    }

    fun logout() {
        prefs.edit()
            .putBoolean(KEY_LOGGED_IN, false)
            .apply()
    }
}
