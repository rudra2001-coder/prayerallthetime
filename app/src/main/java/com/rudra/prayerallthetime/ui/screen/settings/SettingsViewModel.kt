package com.rudra.prayerallthetime.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.local.AppDatabase
import com.rudra.prayerallthetime.data.local.LocalSettings
import com.rudra.prayerallthetime.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository,
    private val localSettings: LocalSettings,
    private val database: AppDatabase
) : ViewModel() {

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

    fun resetFullApp(onComplete: () -> Unit) {
        viewModelScope.launch {
            // 1. Clear DataStore
            localSettings.clearAllData()
            
            // 2. Clear SharedPreferences
            repository.clearAllData()
            
            // 3. Clear Room Database
            database.clearAllTables()
            
            // 4. Notify UI
            onComplete()
        }
    }
}
