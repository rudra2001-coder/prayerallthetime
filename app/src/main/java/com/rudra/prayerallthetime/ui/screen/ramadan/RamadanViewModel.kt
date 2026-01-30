package com.rudra.prayerallthetime.ui.screen.ramadan

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.core.calendar.HijriCalendar
import com.rudra.prayerallthetime.data.local.LocalSettings
import com.rudra.prayerallthetime.data.local.PrayerDao
import com.rudra.prayerallthetime.data.local.TaraweehRecord
import com.rudra.prayerallthetime.data.repository.PrayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class RamadanViewModel @Inject constructor(
    private val localSettings: LocalSettings,
    private val prayerDao: PrayerDao,
    private val prayerRepository: PrayerRepository
) : ViewModel() {

    companion object {
        const val totalRamadanDays = 30
    }

    private val _isRamadan = MutableStateFlow(false)
    val isRamadan: StateFlow<Boolean> = _isRamadan.asStateFlow()

    private val _ramadanDay = MutableStateFlow(0)
    val ramadanDay: StateFlow<Int> = _ramadanDay.asStateFlow()

    private val _suhoorTime = MutableStateFlow("--:--")
    val suhoorTime: StateFlow<String> = _suhoorTime.asStateFlow()

    private val _iftarTime = MutableStateFlow("--:--")
    val iftarTime: StateFlow<String> = _iftarTime.asStateFlow()

    private val _fastingCountdown = MutableStateFlow("00:00:00")
    val fastingCountdown: StateFlow<String> = _fastingCountdown.asStateFlow()

    private val _taraweehCount = MutableStateFlow(0)
    val taraweehCount: StateFlow<Int> = _taraweehCount.asStateFlow()

    private val _fastingProgress = MutableStateFlow(0.0f)
    val fastingProgress: StateFlow<Float> = _fastingProgress.asStateFlow()

    private val _isFasting = MutableStateFlow(false)
    val isFasting: StateFlow<Boolean> = _isFasting.asStateFlow()

    private val _remainingDays = MutableStateFlow(0)
    val remainingDays: StateFlow<Int> = _remainingDays.asStateFlow()

    private val _daysFasted = MutableStateFlow(0)
    val daysFasted: StateFlow<Int> = _daysFasted.asStateFlow()

    private val _waterIntake = MutableStateFlow(0)
    val waterIntake: StateFlow<Int> = _waterIntake.asStateFlow()

    private val _dailyGoals = MutableStateFlow(mapOf(
        "prayers" to 0,
        "quran" to 0,
        "charity" to 0,
        "dhikr" to 0
    ))
    val dailyGoals: StateFlow<Map<String, Int>> = _dailyGoals.asStateFlow()

    private val _charityAmount = MutableStateFlow(0.0)
    val charityAmount: StateFlow<Double> = _charityAmount.asStateFlow()

    private val _quranProgress = MutableStateFlow(0f)
    val quranProgress: StateFlow<Float> = _quranProgress.asStateFlow()

    private val _fastingStreak = MutableStateFlow(0)
    val fastingStreak: StateFlow<Int> = _fastingStreak.asStateFlow()

    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

    private val _nextRamadanCountdown = MutableStateFlow("")
    val nextRamadanCountdown: StateFlow<String> = _nextRamadanCountdown.asStateFlow()

    private val _prayerTimes = MutableStateFlow<Map<String, String>>(emptyMap())
    val prayerTimes: StateFlow<Map<String, String>> = _prayerTimes.asStateFlow()

    init {
        viewModelScope.launch {
            loadInitialData()
            startCountdownTimer()
        }
    }

    private suspend fun loadInitialData() {
        val today = LocalDate.now()
        val hijrahDate = HijrahDate.from(today)
        val month = hijrahDate.get(java.time.temporal.ChronoField.MONTH_OF_YEAR)
        val dayOfMonth = hijrahDate.get(java.time.temporal.ChronoField.DAY_OF_MONTH)
        
        // 9 is the month number for Ramadan in Hijrah calendar
        _isRamadan.value = month == 9
        if (_isRamadan.value) {
            _ramadanDay.value = dayOfMonth
            _remainingDays.value = 30 - dayOfMonth
        } else {
            calculateNextRamadan(today)
        }

        val location = localSettings.userLocation.first() ?: (23.8103 to 90.4125) // Default to Dhaka
        val prayerData = prayerRepository.getPrayerTimes(location.first, location.second, today)
        
        val timesMap = prayerData.allPrayers.associate { it.name to it.time }
        _prayerTimes.value = timesMap
        _suhoorTime.value = timesMap["Fajr"] ?: "--:--"
        _iftarTime.value = timesMap["Maghrib"] ?: "--:--"

        // Load Taraweeh from DB
        val dateStr = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val taraweehRecord = prayerDao.getTaraweehForDate(dateStr)
        _taraweehCount.value = taraweehRecord?.rakatCount ?: 0

        // Load achievements/streak logic
        loadAchievements()
    }

    private fun calculateNextRamadan(today: LocalDate) {
        // Simple estimation for next Ramadan if not currently in Ramadan
        _nextRamadanCountdown.value = "Coming soon" 
    }

    private fun startCountdownTimer() {
        viewModelScope.launch {
            while (true) {
                updateTimers()
                delay(1000)
            }
        }
    }

    private fun updateTimers() {
        val now = LocalTime.now()
        val suhoorStr = _suhoorTime.value
        val iftarStr = _iftarTime.value
        
        if (suhoorStr == "--:--" || iftarStr == "--:--") return

        val suhoorEnd = parseLocalTime(suhoorStr)
        val iftarStart = parseLocalTime(iftarStr)

        _isFasting.value = now.isAfter(suhoorEnd) && now.isBefore(iftarStart)

        if (now.isBefore(suhoorEnd)) {
            val diff = ChronoUnit.SECONDS.between(now, suhoorEnd)
            _fastingCountdown.value = formatSeconds(diff)
            _fastingProgress.value = 0f
        } else if (now.isBefore(iftarStart)) {
            val total = ChronoUnit.SECONDS.between(suhoorEnd, iftarStart)
            val elapsed = ChronoUnit.SECONDS.between(suhoorEnd, now)
            _fastingProgress.value = (elapsed.toFloat() / total.toFloat()).coerceIn(0f, 1f)
            val diff = ChronoUnit.SECONDS.between(now, iftarStart)
            _fastingCountdown.value = formatSeconds(diff)
        } else {
            _fastingProgress.value = 1f
            _fastingCountdown.value = "00:00:00"
        }
    }

    private fun parseLocalTime(timeStr: String): LocalTime {
        return try {
            val formatter = DateTimeFormatter.ofPattern("hh:mm a")
            LocalTime.parse(timeStr, formatter)
        } catch (e: Exception) {
            LocalTime.MIDNIGHT
        }
    }

    private fun formatSeconds(seconds: Long): String {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        return String.format("%02d:%02d:%02d", h, m, s)
    }

    fun addWater() { _waterIntake.update { it + 1 } }
    fun addCharity(amount: Double) { _charityAmount.update { it + amount } }
    fun updateQuranProgress(pages: Int) { _quranProgress.update { (it + (pages / 604f)).coerceIn(0f, 1f) } }

    fun logFast() {
        _daysFasted.update { (it + 1).coerceAtMost(30) }
        _fastingStreak.update { it + 1 }
        loadAchievements()
    }

    private fun loadAchievements() {
        val list = mutableListOf<Achievement>()
        if (_fastingStreak.value >= 3) list.add(Achievement("3-Day Streak", "Consistent fasting", Icons.Filled.Star, true, "Today"))
        if (_daysFasted.value >= 7) list.add(Achievement("Week One", "First 7 days complete", Icons.Filled.EmojiEvents, true, "Yesterday"))
        _achievements.value = list
    }

    fun incrementTaraweeh() {
        val newValue = (_taraweehCount.value + 2).coerceAtMost(20)
        _taraweehCount.value = newValue
        viewModelScope.launch {
            val dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            prayerDao.insertTaraweeh(TaraweehRecord(date = dateStr, rakatCount = newValue))
        }
    }

    fun resetTaraweeh() {
        _taraweehCount.value = 0
        viewModelScope.launch {
            val dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            prayerDao.insertTaraweeh(TaraweehRecord(date = dateStr, rakatCount = 0))
        }
    }

    fun getHijriDate() = HijriCalendar.getTodayHijri()
    fun getRamadanProgress() = _daysFasted.value.toFloat() / 30f
    fun getCurrentPrayer(): String? {
        // Logic to find current prayer based on _prayerTimes and current time
        return "Asr" // Placeholder
    }
    fun getNextPrayer(): Pair<String, String>? {
        return "Maghrib" to "1h 15m" // Placeholder
    }
    fun getJuzCompleted() = (_quranProgress.value * 30).toInt()
}

data class Achievement(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val unlocked: Boolean = false,
    val unlockDate: String? = null
)
