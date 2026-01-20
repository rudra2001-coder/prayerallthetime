package com.rudra.prayerallthetime.ui.screen.tahajjud

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.local.LocalSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TahajjudViewModel @Inject constructor(
    private val localSettings: LocalSettings
) : ViewModel() {

    val tahajjudTimeStr: StateFlow<String> = localSettings.tahajjudTime
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "03:45 AM"
        )
}
