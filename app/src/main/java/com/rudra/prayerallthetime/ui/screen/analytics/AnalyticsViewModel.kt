package com.rudra.prayerallthetime.ui.screen.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.local.HabitDao
import com.rudra.prayerallthetime.data.local.HabitEntity
import com.rudra.prayerallthetime.data.local.PrayerDao
import com.rudra.prayerallthetime.data.local.PrayerRecord
import com.rudra.prayerallthetime.ui.screen.prayer.DayData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val prayerDao: PrayerDao,
    private val habitDao: HabitDao
) : ViewModel() {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val prayerConsistency: StateFlow<Map<String, Float>> = prayerDao.getAllRecords()
        .map { records ->
            if (records.isEmpty()) return@map emptyMap<String, Float>()
            val prayerNames = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")
            val distinctDates = records.map { it.date }.distinct().size.coerceAtLeast(1)
            prayerNames.associateWith { name ->
                records.count { it.prayerName == name && it.isCompleted }.toFloat() / distinctDates
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val habitStats: StateFlow<List<HabitEntity>> = habitDao.getAllHabits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val weeklyProgress: StateFlow<List<DayData>> = flow {
        while (true) {
            val today = LocalDate.now()
            val startOfWeek = today.with(DayOfWeek.MONDAY)
            val weekData = mutableListOf<DayData>()
            for (i in 0..6) {
                val date = startOfWeek.plusDays(i.toLong())
                val dateStr = date.format(dateFormatter)
                val dayRecords = prayerDao.getRecordsForDate(dateStr).first()
                val completed = dayRecords.count { it.isCompleted && it.prayerName !in listOf("Sunrise", "Sunset") }
                val total = if (dayRecords.isEmpty()) 5 else dayRecords.size.coerceAtLeast(5)
                weekData.add(DayData(
                    dayName = date.dayOfWeek.name,
                    dayAbbr = date.dayOfWeek.name.take(1),
                    completedPrayers = completed,
                    completionRate = completed.toFloat() / total.toFloat(),
                    isToday = date == today
                ))
            }
            emit(weekData)
            kotlinx.coroutines.delay(60000)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _totalCompletedPrayers = prayerDao.getTotalCompletedCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val totalCompletedPrayers: StateFlow<Int> = _totalCompletedPrayers
}
