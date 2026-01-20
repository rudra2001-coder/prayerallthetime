package com.rudra.prayerallthetime.ui.screen.ramadan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.local.LocalSettings
import com.rudra.prayerallthetime.data.local.PrayerDao
import com.rudra.prayerallthetime.data.local.TaraweehRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class RamadanViewModel @Inject constructor(
    private val localSettings: LocalSettings,
    private val prayerDao: PrayerDao
) : ViewModel() {

    private val _isRamadan = MutableStateFlow(true)
    val isRamadan: StateFlow<Boolean> = _isRamadan.asStateFlow()

    private val _ramadanDay = MutableStateFlow(calculateRamadanDay())
    val ramadanDay: StateFlow<Int> = _ramadanDay.asStateFlow()

    private val _suhoorTime = MutableStateFlow(calculateSuhoorTime())
    val suhoorTime: StateFlow<String> = _suhoorTime.asStateFlow()

    private val _iftarTime = MutableStateFlow(calculateIftarTime())
    val iftarTime: StateFlow<String> = _iftarTime.asStateFlow()

    private val _fastingCountdown = MutableStateFlow(calculateFastingCountdown())
    val fastingCountdown: StateFlow<String> = _fastingCountdown.asStateFlow()

    private val _taraweehCount = MutableStateFlow(0)
    val taraweehCount: StateFlow<Int> = _taraweehCount.asStateFlow()

    private val _fastingProgress = MutableStateFlow(0.75f)
    val fastingProgress: StateFlow<Float> = _fastingProgress.asStateFlow()

    private val _isFasting = MutableStateFlow(checkIfFasting())
    val isFasting: StateFlow<Boolean> = _isFasting.asStateFlow()

    private val _remainingDays = MutableStateFlow(20)
    val remainingDays: StateFlow<Int> = _remainingDays.asStateFlow()

    private val _daysFasted = MutableStateFlow(10)
    val daysFasted: StateFlow<Int> = _daysFasted.asStateFlow()

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val todayStr = LocalDate.now().format(dateFormatter)

    init {
        loadLocalTaraweeh()
        startCountdownTimer()
        updateFastingProgress()
    }

    private fun startCountdownTimer() {
        viewModelScope.launch {
            while (true) {
                _fastingCountdown.value = calculateFastingCountdown()
                _fastingProgress.value = calculateFastingProgress()
                _isFasting.value = checkIfFasting()
                delay(1000) // Update every second
            }
        }
    }

    private fun calculateRamadanDay(): Int {
        // Implement actual Ramadan date calculation based on Hijri calendar
        val ramadanStart = LocalDate.of(2024, 3, 11)
        val today = LocalDate.now()
        return if (today.isAfter(ramadanStart)) {
            ramadanStart.until(today).days + 1
        } else {
            0
        }.coerceIn(1, 30)
    }

    private fun calculateSuhoorTime(): String {
        // This should be calculated based on Fajr prayer time
        return "04:30 AM"
    }

    private fun calculateIftarTime(): String {
        // This should be calculated based on Maghrib prayer time
        return "06:15 PM"
    }

    private fun calculateFastingCountdown(): String {
        val iftarTime = LocalTime.of(18, 15) // Should come from prayer times
        val now = LocalTime.now()

        return if (now.isBefore(iftarTime)) {
            val hours = iftarTime.hour - now.hour
            val minutes = iftarTime.minute - now.minute
            val seconds = 60 - now.second
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            "00:00:00"
        }
    }

    private fun calculateFastingProgress(): Float {
        val suhoorEnd = LocalTime.of(4, 30)
        val iftarTime = LocalTime.of(18, 15)
        val now = LocalTime.now()

        val totalDuration = ChronoUnit.SECONDS.between(suhoorEnd, iftarTime).toFloat()
        val elapsedDuration = ChronoUnit.SECONDS.between(suhoorEnd, now).toFloat()

        return (elapsedDuration / totalDuration).coerceIn(0f, 1f)
    }

    private fun checkIfFasting(): Boolean {
        val now = LocalTime.now()
        val suhoorEnd = LocalTime.of(4, 30)
        val iftarTime = LocalTime.of(18, 15)
        return now.isAfter(suhoorEnd) && now.isBefore(iftarTime)
    }

    private fun updateFastingProgress() {
        viewModelScope.launch {
            while (true) {
                _fastingProgress.value = calculateFastingProgress()
                delay(60000) // Update every minute
            }
        }
    }

    private fun loadLocalTaraweeh() {
        viewModelScope.launch {
            val record = prayerDao.getTaraweehForDate(todayStr)
            _taraweehCount.value = record?.rakatCount ?: 0
        }
    }

    fun incrementTaraweeh() {
        viewModelScope.launch {
            val newCount = (_taraweehCount.value + 2).coerceAtMost(20)
            _taraweehCount.value = newCount
            prayerDao.insertTaraweeh(TaraweehRecord(todayStr, newCount))
        }
    }

    fun resetTaraweeh() {
        viewModelScope.launch {
            _taraweehCount.value = 0
            prayerDao.insertTaraweeh(TaraweehRecord(todayStr, 0))
        }
    }

    fun updateDaysFasted(count: Int) {
        _daysFasted.value = count
    }

    fun getRamadanStats(): Map<String, Any> {
        return mapOf(
            "totalDays" to 30,
            "remainingDays" to _remainingDays.value,
            "daysFasted" to _daysFasted.value,
            "completionPercentage" to (_daysFasted.value / 30.0 * 100).toInt()
        )
    }
}