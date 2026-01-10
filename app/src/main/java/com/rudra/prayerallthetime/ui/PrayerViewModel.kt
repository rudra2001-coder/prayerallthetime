package com.rudra.prayerallthetime.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.LocationService
import com.rudra.prayerallthetime.data.Prayer
import com.rudra.prayerallthetime.data.local.FamilyMemberRecord
import com.rudra.prayerallthetime.data.repository.PrayerRepository
import com.rudra.prayerallthetime.ui.components.DayData
import com.rudra.prayerallthetime.ui.components.StreakData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class PrayerViewModel @Inject constructor(
    private val prayerRepository: PrayerRepository,
    private val locationService: LocationService
) : ViewModel() {
    
    val prayerState = mutableStateOf<PrayerState>(PrayerState.Loading)
    
    private val _prayers = MutableStateFlow<List<Prayer>>(emptyList())
    val prayers: StateFlow<List<Prayer>> = _prayers.asStateFlow()

    private val _nextPrayerName = MutableStateFlow("Fajr")
    val nextPrayerName: StateFlow<String> = _nextPrayerName.asStateFlow()

    private val _countdown = MutableStateFlow("00:00:00")
    val countdown: StateFlow<String> = _countdown.asStateFlow()

    private val _sunriseTime = MutableStateFlow("05:30 AM")
    val sunriseTime: StateFlow<String> = _sunriseTime.asStateFlow()

    private val _hijriDate = MutableStateFlow("1 Ramadan 1445")
    val hijriDate: StateFlow<String> = _hijriDate.asStateFlow()

    private val _ayatArabic = MutableStateFlow("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ")
    val ayatArabic: StateFlow<String> = _ayatArabic.asStateFlow()

    private val _ayatEnglish = MutableStateFlow("In the name of Allah, the Entirely Merciful, the Especially Merciful.")
    val ayatEnglish: StateFlow<String> = _ayatEnglish.asStateFlow()

    private val _surahInfo = MutableStateFlow("Al-Fatihah 1:1")
    val surahInfo: StateFlow<String> = _surahInfo.asStateFlow()

    private val _gregorianDate = MutableStateFlow(LocalDate.now().toString())
    val gregorianDate: StateFlow<String> = _gregorianDate.asStateFlow()

    private val _cityName = MutableStateFlow("Dhaka")
    val cityName: StateFlow<String> = _cityName.asStateFlow()

    private val _isRamadan = MutableStateFlow(true)
    val isRamadan: StateFlow<Boolean> = _isRamadan.asStateFlow()

    private val _tasbeehCount = MutableStateFlow(0)
    val tasbeehCount: StateFlow<Int> = _tasbeehCount.asStateFlow()

    private val _taraweehCount = MutableStateFlow(0)
    val taraweehCount: StateFlow<Int> = _taraweehCount.asStateFlow()

    private val _currentStreak = MutableStateFlow(7)
    val currentStreak: StateFlow<Int> = _currentStreak.asStateFlow()

    private val _weeklyDayData = MutableStateFlow<List<DayData>>(emptyList())
    val weeklyDayData: StateFlow<List<DayData>> = _weeklyDayData.asStateFlow()

    private val _completionRate = MutableStateFlow(0.85f)
    val completionRate: StateFlow<Float> = _completionRate.asStateFlow()

    private val _isAyatBookmarked = MutableStateFlow(false)
    val isAyatBookmarked: StateFlow<Boolean> = _isAyatBookmarked.asStateFlow()

    private val _isHadithBookmarked = MutableStateFlow(false)
    val isHadithBookmarked: StateFlow<Boolean> = _isHadithBookmarked.asStateFlow()

    private val _prayerStats = MutableStateFlow<Map<String, Float>>(
        mapOf("Fajr" to 0.9f, "Dhuhr" to 0.8f, "Asr" to 0.85f, "Maghrib" to 0.95f, "Isha" to 0.75f)
    )
    val prayerStats: StateFlow<Map<String, Float>> = _prayerStats.asStateFlow()

    private val _familyMembers = MutableStateFlow<List<FamilyMemberRecord>>(emptyList())
    val familyMembers: StateFlow<List<FamilyMemberRecord>> = _familyMembers.asStateFlow()

    private val _qiblaDirection = MutableStateFlow(0f)
    val qiblaDirection: StateFlow<Float> = _qiblaDirection.asStateFlow()

    private val _fastingCountdown = MutableStateFlow("12:30:00")
    val fastingCountdown: StateFlow<String> = _fastingCountdown.asStateFlow()

    private val _suhoorTime = MutableStateFlow("04:30 AM")
    val suhoorTime: StateFlow<String> = _suhoorTime.asStateFlow()

    private val _iftarTime = MutableStateFlow("06:15 PM")
    val iftarTime: StateFlow<String> = _iftarTime.asStateFlow()

    private val _ramadanDay = MutableStateFlow(15)
    val ramadanDay: StateFlow<Int> = _ramadanDay.asStateFlow()

    private val _earnedBadges = MutableStateFlow<List<Badge>>(emptyList())
    val earnedBadges: StateFlow<List<Badge>> = _earnedBadges.asStateFlow()

    private val _upcomingBadges = MutableStateFlow<List<Badge>>(emptyList())
    val upcomingBadges: StateFlow<List<Badge>> = _upcomingBadges.asStateFlow()

    init {
        loadPrayerData()
        _prayers.value = listOf(
            Prayer("Fajr", "05:10 AM"),
            Prayer("Dhuhr", "12:30 PM"),
            Prayer("Asr", "04:30 PM"),
            Prayer("Maghrib", "06:15 PM"),
            Prayer("Isha", "07:45 PM")
        )
    }
    
    private fun loadPrayerData() {
        viewModelScope.launch {
            try {
                val location = locationService.getCurrentLocation()
                val data = prayerRepository.getPrayerTimes(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    date = LocalDate.now()
                )
                prayerState.value = PrayerState.Success(data)
                _cityName.value = data.city
                _qiblaDirection.value = data.qiblaDirection.replace("°", "").toFloatOrNull() ?: 0f
            } catch (e: Exception) {
                prayerState.value = PrayerState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    fun refreshData() {
        prayerState.value = PrayerState.Loading
        loadPrayerData()
    }

    fun getNextPrayerTime(): String = "06:15 PM"
    fun isAlarmSet(): Boolean = false
    fun getQiblaDirection(): Float = _qiblaDirection.value
    fun updateLocation(lat: Double, lng: Double) { loadPrayerData() }
    fun toggleAlarm() {}
    fun togglePrayerState(prayer: Prayer) {
        val currentList = _prayers.value.toMutableList()
        val index = currentList.indexOfFirst { it.name == prayer.name }
        if (index != -1) {
            currentList[index] = currentList[index].copy(isPrayed = !currentList[index].isPrayed)
            _prayers.value = currentList
        }
    }
    fun toggleAyatBookmark() { _isAyatBookmarked.value = !_isAyatBookmarked.value }
    fun toggleHadithBookmark() { _isHadithBookmarked.value = !_isHadithBookmarked.value }
    fun shareContent(content: String) {}
    fun playAudio(text: String) {}
    fun incrementTasbeeh() { _tasbeehCount.value++ }
    fun resetTasbeeh() { _tasbeehCount.value = 0 }
    fun incrementTaraweeh() { _taraweehCount.value++ }
    fun resetTaraweeh() { _taraweehCount.value = 0 }
    fun addFamilyMember(name: String) {}
    fun removeFamilyMember(member: FamilyMemberRecord) {}
}
