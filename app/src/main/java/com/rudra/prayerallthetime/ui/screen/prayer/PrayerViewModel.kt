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

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
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

    private val _cityName = MutableStateFlow("Detecting...")
    val cityName: StateFlow<String> = _cityName.asStateFlow()

    private val _qiblaDirection = MutableStateFlow(0f)
    val qiblaDirection: StateFlow<Float> = _qiblaDirection.asStateFlow()

    private val _tasbeehCount = MutableStateFlow(0)
    val tasbeehCount: StateFlow<Int> = _tasbeehCount.asStateFlow()

    // Persistent Worship States
    val wuduStatus = localSettings.wuduStatus.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
    val currentSurah = localSettings.currentSurah.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Al-Fatihah")
    val tahajjudTimeStr = localSettings.tahajjudTime.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "03:45 AM")
    
    // User location for Qibla distance calculation
    val userLocation: StateFlow<Pair<Double, Double>?> = localSettings.userLocation.stateIn(
        viewModelScope, 
        SharingStarted.WhileSubscribed(5000), 
        null
    )

    // Manual vs Auto Settings
    val useManualPrayerTimes = localSettings.useManualPrayerTimes.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    val manualFajr = localSettings.manualFajr.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "05:00 AM")
    val manualDhuhr = localSettings.manualDhuhr.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "12:30 PM")
    val manualAsr = localSettings.manualAsr.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "04:30 PM")
    val manualMaghrib = localSettings.manualMaghrib.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "06:15 PM")
    val manualIsha = localSettings.manualIsha.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "08:00 PM")

    // Dashboard specific states
    val nextPrayerName = MutableStateFlow("Fajr")
    val countdown = MutableStateFlow("00:00:00")
    val nextPrayerMillis = MutableStateFlow(0L)
    val sunriseTime = MutableStateFlow("--:--")
    val hijriDate = MutableStateFlow("-- -- ----")
    val gregorianDate = MutableStateFlow(LocalDate.now().toString())
    
    val isRamadan = MutableStateFlow(true)
    val taraweehCount = MutableStateFlow(0)
    
    private val _currentStreak = MutableStateFlow(0)
    val currentStreak: StateFlow<Int> = _currentStreak.asStateFlow()
    
    val completionRate = MutableStateFlow(0.0f)
    val weeklyDayData = MutableStateFlow<List<DayData>>(emptyList())
    
    val fastingCountdown = MutableStateFlow("00:00:00")
    val iftarTime = MutableStateFlow("--:--")
    val suhoorTime = MutableStateFlow("--:--")
    val ramadanDay = MutableStateFlow(0)

    val ayatArabic = MutableStateFlow("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ")
    val ayatEnglish = MutableStateFlow("In the name of Allah, the Entirely Merciful, the Especially Merciful.")
    val surahInfo = MutableStateFlow("Al-Fatihah 1:1")
    val isAyatBookmarked = MutableStateFlow(false)
    val isHadithBookmarked = MutableStateFlow(false)

    val familyMembers: StateFlow<List<FamilyMemberRecord>> = prayerDao.getAllFamilyMembers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _prayerStats = MutableStateFlow<Map<String, Float>>(emptyMap())
    val prayerStats: StateFlow<Map<String, Float>> = _prayerStats.asStateFlow()

    val earnedBadges = MutableStateFlow(emptyList<Badge>())
    val upcomingBadges = MutableStateFlow(emptyList<Badge>())

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private var lastCheckedDate = LocalDate.now()
    private val todayStr: String get() = LocalDate.now().format(dateFormatter)

    init {
        loadInitialData()
        loadLocalTasbeeh()
        loadLocalTaraweeh()
        startCountdownTicker()
        observeManualSettingsChange()
        observePrayerRecords()
    }
    
    private fun observeManualSettingsChange() {
        viewModelScope.launch {
            merge(
                useManualPrayerTimes, manualFajr, manualDhuhr, 
                manualAsr, manualMaghrib, manualIsha
            ).collectLatest {
                refreshData()
            }
        }
    }

    private fun startCountdownTicker() {
        viewModelScope.launch {
            while (true) {
                val nowDate = LocalDate.now()
                if (nowDate != lastCheckedDate) {
                    lastCheckedDate = nowDate
                    gregorianDate.value = nowDate.toString()
                    refreshData()
                    loadLocalTasbeeh()
                    loadLocalTaraweeh()
                }

                if (nextPrayerMillis.value > 0) {
                    val diff = nextPrayerMillis.value - System.currentTimeMillis()
                    if (diff > 0) {
                        val hours = (diff / (1000 * 60 * 60))
                        val minutes = (diff / (1000 * 60)) % 60
                        val seconds = (diff / 1000) % 60
                        countdown.value = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                    } else if (diff <= 0 && diff > -2000) {
                        playAthan()
                        refreshData()
                        delay(2000)
                    } else if (diff < -5000) {
                        refreshData()
                    }
                }
                delay(1000)
            }
        }
    }

    private fun observePrayerRecords() {
        viewModelScope.launch {
            prayerDao.getAllRecords().collect { allRecords ->
                calculateConsistencyStats(allRecords)
                calculateStreak(allRecords)
                loadWeeklyProgress()
            }
        }
        
        viewModelScope.launch {
            prayerDao.getRecordsForDate(todayStr).collect { records ->
                val currentList = _prayers.value
                if (currentList.isNotEmpty()) {
                    val updatedList = currentList.map { prayer ->
                        val record = records.find { it.prayerName == prayer.name }
                        prayer.copy(isPrayed = record?.isCompleted ?: false)
                    }
                    _prayers.value = updatedList
                    
                    val dailyPrayers = updatedList.filter { it.name != "Sunrise" && it.name != "Sunset" }
                    if (dailyPrayers.isNotEmpty()) {
                        completionRate.value = dailyPrayers.count { it.isPrayed }.toFloat() / dailyPrayers.size
                    }
                }
            }
        }
    }

    private fun playAthan() {
        playAudio("Athan Time for ${nextPrayerName.value}")
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            localSettings.userLocation.collectLatest { savedLocation ->
                if (savedLocation != null) {
                    _cityName.value = localSettings.cityName.first() ?: "Unknown"
                    fetchPrayerTimes(savedLocation.first, savedLocation.second)
                } else {
                    refreshLocation()
                }
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
                
                suhoorTime.value = data.allPrayers.find { it.name.equals("Fajr", true) }?.time ?: "--:--"
                iftarTime.value = data.allPrayers.find { it.name.equals("Maghrib", true) }?.time ?: "--:--"
                
                val initialRecords = prayerDao.getRecordsForDate(todayStr).first()
                val prayerList = data.allPrayers.map { detail ->
                    val isPrayed = initialRecords.find { it.prayerName == detail.name }?.isCompleted ?: false
                    Prayer(detail.name, detail.time, isPrayed)
                }
                _prayers.value = prayerList
                
            } catch (e: Exception) {
                prayerState.value = PrayerState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun loadWeeklyProgress() {
        viewModelScope.launch {
            val today = LocalDate.now()
            val startOfWeek = today.with(DayOfWeek.MONDAY)
            val weekDataList = mutableListOf<DayData>()
            
            for (i in 0..6) {
                val date = startOfWeek.plusDays(i.toLong())
                val dateStr = date.format(dateFormatter)
                val dayRecords = prayerDao.getRecordsForDate(dateStr).first()
                val prayerOnlyRecords = dayRecords.filter { it.prayerName != "Sunrise" && it.prayerName != "Sunset" }
                val completed = prayerOnlyRecords.count { it.isCompleted }
                val total = if (prayerOnlyRecords.isNotEmpty()) prayerOnlyRecords.size else 5
                val rate = completed.toFloat() / total.toFloat()
                
                weekDataList.add(
                    DayData(
                        dayName = date.dayOfWeek.name,
                        dayAbbr = date.dayOfWeek.name.take(1),
                        completedPrayers = completed,
                        completionRate = rate,
                        isToday = date == today
                    )
                )
            }
            weeklyDayData.value = weekDataList
        }
    }

    private fun calculateConsistencyStats(allRecords: List<PrayerRecord>) {
        if (allRecords.isEmpty()) {
            _prayerStats.value = emptyMap()
            return
        }
        val prayerNames = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")
        val stats = mutableMapOf<String, Float>()
        val distinctDates = allRecords.map { it.date }.distinct()
        val totalDays = distinctDates.size.coerceAtLeast(1)
        prayerNames.forEach { name ->
            val completedDays = allRecords.count { it.prayerName == name && it.isCompleted }
            stats[name] = completedDays.toFloat() / totalDays.toFloat()
        }
        _prayerStats.value = stats
    }

    private fun calculateStreak(allRecords: List<PrayerRecord>) {
        if (allRecords.isEmpty()) {
            _currentStreak.value = 0
            return
        }
        val prayerNames = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")
        val dailyCompletion = allRecords.groupBy { it.date }.mapValues { entry ->
            val dayPrayers = entry.value.filter { it.prayerName in prayerNames }
            dayPrayers.size >= 5 && dayPrayers.all { it.isCompleted }
        }
        var streak = 0
        var currentDate = LocalDate.now()
        if (dailyCompletion[currentDate.format(dateFormatter)] != true) {
            currentDate = currentDate.minusDays(1)
        }
        while (dailyCompletion[currentDate.format(dateFormatter)] == true) {
            streak++
            currentDate = currentDate.minusDays(1)
        }
        _currentStreak.value = streak
    }

    fun refreshLocation() {
        prayerState.value = PrayerState.Loading
        viewModelScope.launch {
            try {
                val location = locationService.getCurrentLocation()
                val city = "Current Location" 
                localSettings.saveLocation(location.latitude, location.longitude, city)
                _cityName.value = city
                fetchPrayerTimes(location.latitude, location.longitude)
            } catch (e: Exception) {
                _cityName.value = "Default Location"
                fetchPrayerTimes(23.8103, 90.4125)
            }
        }
    }

    fun togglePrayerState(prayer: Prayer) {
        viewModelScope.launch {
            val record = prayerDao.getRecord(todayStr, prayer.name)
            val newStatus = if (record != null) !record.isCompleted else true
            if (record != null) prayerDao.insertRecord(record.copy(isCompleted = newStatus))
            else prayerDao.insertRecord(PrayerRecord(date = todayStr, prayerName = prayer.name, isCompleted = newStatus))
        }
    }

    fun toggleWuduStatus() {
        viewModelScope.launch { localSettings.updateWuduStatus(!wuduStatus.value) }
    }

    fun updateCurrentSurah(surah: String) {
        viewModelScope.launch { localSettings.updateCurrentSurah(surah) }
    }

    fun setManualMode(enabled: Boolean) {
        viewModelScope.launch {
            localSettings.setUseManualPrayerTimes(enabled)
            refreshData()
        }
    }

    fun updateManualTime(prayerName: String, time: String) {
        viewModelScope.launch {
            localSettings.updateManualPrayerTime(prayerName, time)
            refreshData()
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

    fun addFamilyMember(name: String, relationship: String) {
        viewModelScope.launch {
            val today = LocalDate.now().format(dateFormatter)
            prayerDao.insertFamilyMember(
                FamilyMemberRecord(
                    name = name,
                    relationship = relationship,
                    lastActiveDate = today
                )
            )
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
    fun playAudio(text: String) { println("Playing audio: $text") }
    fun refreshData() { 
        viewModelScope.launch {
            val location = localSettings.userLocation.first() ?: (23.8103 to 90.4125)
            fetchPrayerTimes(location.first, location.second)
        }
    }
}
