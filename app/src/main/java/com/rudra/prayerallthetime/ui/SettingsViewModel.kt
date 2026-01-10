package com.rudra.prayerallthetime.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.rudra.prayerallthetime.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SettingsRepository(application)

    private val _notificationsEnabled = MutableStateFlow(repository.isNotificationEnabled())
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled

    private val _darkModeEnabled = MutableStateFlow(repository.isDarkModeEnabled())
    val darkModeEnabled: StateFlow<Boolean> = _darkModeEnabled

    private val _locationEnabled = MutableStateFlow(repository.isLocationEnabled())
    val locationEnabled: StateFlow<Boolean> = _locationEnabled

    private val _premiumEnabled = MutableStateFlow(repository.isPremiumEnabled())
    val premiumEnabled: StateFlow<Boolean> = _premiumEnabled

    fun toggleNotifications(enabled: Boolean) {
        repository.setNotificationEnabled(enabled)
        _notificationsEnabled.value = enabled
    }

    fun toggleDarkMode(enabled: Boolean) {
        repository.setDarkModeEnabled(enabled)
        _darkModeEnabled.value = enabled
    }

    fun toggleLocation(enabled: Boolean) {
        repository.setLocationEnabled(enabled)
        _locationEnabled.value = enabled
    }

    fun togglePremium(enabled: Boolean) {
        repository.setPremiumEnabled(enabled)
        _premiumEnabled.value = enabled
    }
}
