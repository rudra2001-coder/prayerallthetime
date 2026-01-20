package com.rudra.prayerallthetime.ui.screen.analytics

import androidx.lifecycle.ViewModel
import com.rudra.prayerallthetime.data.Prayer
import com.rudra.prayerallthetime.ui.screen.prayer.Badge
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor() : ViewModel() {

    private val _prayerStats = MutableStateFlow<Map<String, Float>>(mapOf(
        "Fajr" to 0.8f,
        "Dhuhr" to 0.9f,
        "Asr" to 0.75f,
        "Maghrib" to 0.95f,
        "Isha" to 0.85f
    ))
    val prayerStats: StateFlow<Map<String, Float>> = _prayerStats.asStateFlow()

    val currentStreak = MutableStateFlow(7)
    val completionRate = MutableStateFlow(0.85f)

    
    val earnedBadges = MutableStateFlow(emptyList<Badge>())
    val upcomingBadges = MutableStateFlow(emptyList<Badge>())
}
