package com.rudra.prayerallthetime.ui.screen.prayer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.LocationService
import com.rudra.prayerallthetime.data.Prayer
import com.rudra.prayerallthetime.data.local.FamilyMemberRecord
import com.rudra.prayerallthetime.data.local.LocalSettings
import com.rudra.prayerallthetime.data.local.PrayerDao
import com.rudra.prayerallthetime.data.local.PrayerRecord
import com.rudra.prayerallthetime.data.local.TaraweehRecord
import com.rudra.prayerallthetime.data.local.TasbeehRecord
import com.rudra.prayerallthetime.data.repository.PrayerRepository
import com.rudra.prayerallthetime.ui.screen.dashboard.components.DayData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class PrayerViewModel @Inject constructor(
    private val prayerRepository: PrayerRepository,
    private val locationService: LocationService,
    private val localSettings: LocalSettings,
    private val prayerDao: PrayerDao
) : ViewModel() {
    
    val prayerState = mutableStateOf<PrayerState>(PrayerState.Loading)
    
    private val _prayers = MutableStateFlow<List<Prayer>>(emptyList())
    val prayers: StateFlow<List<Prayer>> = _prayers.asStateFlow()

    private val _cityName = MutableStateFlow("Dhaka")
    val cityName: StateFlow<String> = _cityName.asStateFlow()

    private val _qiblaDirection = MutableStateFlow(0f)
    val qiblaDirection: StateFlow<Float> = _qiblaDirection.asStateFlow()

    private val _tasbeehCount = MutableStateFlow(0)
    val tasbeehCount: StateFlow<Int> = _tasbeehCount.asStateFlow()

    // Persistent Worship States
    val wuduStatus = localSettings.wuduStatus.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
    val currentSurah = localSettings.currentSurah.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Al-Fatihah")
    val tahajjudTimeStr = localSettings.tahajjudTime.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "03:45 AM")

    // Dashboard specific states
    val nextPrayerName = MutableStateFlow("Fajr")
    val countdown = MutableStateFlow("00:00:00")
    val nextPrayerMillis = MutableStateFlow(0L)
    val sunriseTime = MutableStateFlow("--:--")
    val hijriDate = MutableStateFlow("-- -- ----")
    val gregorianDate = MutableStateFlow(LocalDate.now().toString())
    
    val isRamadan = MutableStateFlow(true)
    val taraweehCount = MutableStateFlow(0)
    val currentStreak = MutableStateFlow(7)
    val completionRate = MutableStateFlow(0.85f)
    val weeklyDayData = MutableStateFlow(emptyList<DayData>())
    
    val fastingCountdown = MutableStateFlow("12:34:56")
    val suhoorTime = MutableStateFlow("04:30 AM")
    val iftarTime = MutableStateFlow("06:15 PM")
    val ramadanDay = MutableStateFlow(10)

    val ayatArabic = MutableStateFlow("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ")
    val ayatEnglish = MutableStateFlow("In the name of Allah, the Entirely Merciful, the Especially Merciful.")
    val surahInfo = MutableStateFlow("Al-Fatihah 1:1")
    val isAyatBookmarked = MutableStateFlow(false)
    val isHadithBookmarked = MutableStateFlow(false)

    val familyMembers: StateFlow<List<FamilyMemberRecord>> = prayerDao.getAllFamilyMembers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _prayerStats = MutableStateFlow<Map<String, Float>>(mapOf(
        "Fajr" to 0.8f,
        "Dhuhr" to 0.9f,
        "Asr" to 0.75f,
        "Maghrib" to 0.95f,
        "Isha" to 0.85f
    ))
    val prayerStats: StateFlow<Map<String, Float>> = _prayerStats.asStateFlow()

    val earnedBadges = MutableStateFlow(emptyList<Badge>())
    val upcomingBadges = MutableStateFlow(emptyList<Badge>())

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val todayStr = LocalDate.now().format(dateFormatter)

    init {
        loadInitialData()
        loadLocalTasbeeh()
        loadLocalTaraweeh()
    }
    
    private fun loadInitialData() {
        viewModelScope.launch {
            val savedLocation = localSettings.userLocation.first()
            if (savedLocation != null) {
                _cityName.value = localSettings.cityName.first() ?: "Unknown"
                fetchPrayerTimes(savedLocation.first, savedLocation.second)
            } else {
                refreshLocation()
            }
        }
    }

    private fun loadLocalTasbeeh() {
        viewModelScope.launch {
            val record = prayerDao.getTasbeehForDate(todayStr)
            _tasbeehCount.value = record?.totalCount ?: 0
        }
    }

    private fun loadLocalTaraweeh() {
        viewModelScope.launch {
            val record = prayerDao.getTaraweehForDate(todayStr)
            taraweehCount.value = record?.rakatCount ?: 0
        }
    }

    private fun fetchPrayerTimes(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val data = prayerRepository.getPrayerTimes(lat, lon, LocalDate.now())
                prayerState.value = PrayerState.Success(data)
                
                nextPrayerName.value = data.nextPrayer.name
                countdown.value = data.countdown
                nextPrayerMillis.value = data.nextPrayerMillis
                sunriseTime.value = data.sunrise
                hijriDate.value = data.hijriDate
                gregorianDate.value = data.gregorianDate
                _qiblaDirection.value = data.qiblaDirection.replace("°", "").toFloatOrNull() ?: 0f
                
                syncPrayersWithLocalDb(data)
                
            } catch (e: Exception) {
                prayerState.value = PrayerState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private suspend fun syncPrayersWithLocalDb(data: PrayerData) {
        val allPrayerNames = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")
        val localRecords = prayerDao.getRecordsForDate(todayStr).first()
        
        val prayerList = allPrayerNames.map { name ->
            val isPrayed = localRecords.find { it.prayerName == name }?.isCompleted ?: false
            Prayer(name, if (name == data.nextPrayer.name) data.nextPrayer.time else "--:--", isPrayed)
        }
        _prayers.value = prayerList
    }

    fun refreshLocation() {
        prayerState.value = PrayerState.Loading
        viewModelScope.launch {
            try {
                val location = locationService.getCurrentLocation()
                val city = "Detected Location" 
                localSettings.saveLocation(location.latitude, location.longitude, city)
                _cityName.value = city
                fetchPrayerTimes(location.latitude, location.longitude)
            } catch (e: Exception) {
                prayerState.value = PrayerState.Error("Location access failed: ${e.message}")
            }
        }
    }
    
    fun togglePrayerState(prayer: Prayer) {
        viewModelScope.launch {
            val newStatus = !prayer.isPrayed
            val currentList = _prayers.value.toMutableList()
            val index = currentList.indexOfFirst { it.name == prayer.name }
            if (index != -1) {
                currentList[index] = currentList[index].copy(isPrayed = newStatus)
                _prayers.value = currentList
                
                val record = prayerDao.getRecord(todayStr, prayer.name)
                if (record != null) {
                    prayerDao.insertRecord(record.copy(isCompleted = newStatus))
                } else {
                    prayerDao.insertRecord(PrayerRecord(date = todayStr, prayerName = prayer.name, isCompleted = newStatus))
                }
            }
        }
    }

    fun toggleWuduStatus() {
        viewModelScope.launch {
            localSettings.updateWuduStatus(!wuduStatus.value)
        }
    }

    fun updateCurrentSurah(surah: String) {
        viewModelScope.launch {
            localSettings.updateCurrentSurah(surah)
        }
    }

    fun incrementTasbeeh() {
        viewModelScope.launch {
            val newCount = _tasbeehCount.value + 1
            _tasbeehCount.value = newCount
            prayerDao.insertTasbeeh(TasbeehRecord(todayStr, newCount))
        }
    }

    fun resetTasbeeh() {
        viewModelScope.launch {
            _tasbeehCount.value = 0
            prayerDao.insertTasbeeh(TasbeehRecord(todayStr, 0))
        }
    }

    fun incrementTaraweeh() {
        viewModelScope.launch {
            val newCount = (taraweehCount.value + 2).coerceAtMost(20)
            taraweehCount.value = newCount
            prayerDao.insertTaraweeh(TaraweehRecord(todayStr, newCount))
        }
    }

    fun resetTaraweeh() {
        viewModelScope.launch {
            taraweehCount.value = 0
            prayerDao.insertTaraweeh(TaraweehRecord(todayStr, 0))
        }
    }

    fun addFamilyMember(name: String) {
        viewModelScope.launch {
            prayerDao.insertFamilyMember(FamilyMemberRecord(name = name, completedPrayers = 0))
        }
    }

    fun removeFamilyMember(member: FamilyMemberRecord) {
        viewModelScope.launch {
            prayerDao.deleteFamilyMember(member)
        }
    }

    fun getNextPrayerTime(): String = nextPrayerName.value
    fun isAlarmSet(): Boolean = true
    fun getQiblaDirection(): Float = _qiblaDirection.value
    fun updateLocation(lat: Double, lng: Double) { fetchPrayerTimes(lat, lng) }
    fun toggleAlarm() {}
    fun toggleAyatBookmark() { isAyatBookmarked.value = !isAyatBookmarked.value }
    fun toggleHadithBookmark() { isHadithBookmarked.value = !isHadithBookmarked.value }
    fun shareContent(content: String) {}
    fun playAudio(text: String) {}
    fun refreshData() { loadInitialData() }
}
