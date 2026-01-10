package com.rudra.prayerallthetime.data.repository

import android.content.Context
import android.content.SharedPreferences

class SettingsRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    fun setNotificationEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("notifications_enabled", enabled).apply()
    }

    fun isNotificationEnabled(): Boolean = prefs.getBoolean("notifications_enabled", true)

    fun setDarkModeEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("dark_mode_enabled", enabled).apply()
    }

    fun isDarkModeEnabled(): Boolean = prefs.getBoolean("dark_mode_enabled", true)

    fun setLocationEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("location_enabled", enabled).apply()
    }

    fun isLocationEnabled(): Boolean = prefs.getBoolean("location_enabled", true)

    fun setPremiumEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("premium_enabled", enabled).apply()
    }

    fun isPremiumEnabled(): Boolean = prefs.getBoolean("premium_enabled", false)
}
