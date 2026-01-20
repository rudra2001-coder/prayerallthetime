package com.rudra.prayerallthetime.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.LocationService
import com.rudra.prayerallthetime.data.Prayer
import com.rudra.prayerallthetime.data.local.LocalSettings
import com.rudra.prayerallthetime.data.local.PrayerDao
import com.rudra.prayerallthetime.data.local.PrayerRecord
import com.rudra.prayerallthetime.data.local.HabitEntity
import com.rudra.prayerallthetime.data.local.DuaEntity
import com.rudra.prayerallthetime.data.repository.HadithRepository
import com.rudra.prayerallthetime.data.repository.PrayerRepository
import com.rudra.prayerallthetime.data.repository.HabitRepository
import com.rudra.prayerallthetime.data.repository.DuaRepository
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerData
import com.rudra.prayerallthetime.ui.screen.prayer.DayData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.DayOfWeek
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val prayerRepository: PrayerRepository,
    private val locationService: LocationService,
    private val localSettings: LocalSettings,
    private val prayerDao: PrayerDao,
    private val hadithRepository: HadithRepository,
    private val habitRepository: HabitRepository,
    private val duaRepository: DuaRepository
) : ViewModel() {

    private val defaultLat = 23.8103
    private val defaultLon = 90.4125

    private val _prayers = MutableStateFlow<List<Prayer>>(emptyList())
    val prayers: StateFlow<List<Prayer>> = _prayers.asStateFlow()

    private val _cityName = MutableStateFlow("Dhaka")
    val cityName: StateFlow<String> = _cityName.asStateFlow()

    val nextPrayerName = MutableStateFlow("Fajr")
    val nextPrayerArabicName = MutableStateFlow("الفجر")
    val countdown = MutableStateFlow("00:00:00")
    val nextPrayerMillis = MutableStateFlow(0L)
    val sunriseTime = MutableStateFlow("--:--")
    val hijriDate = MutableStateFlow("-- -- ----")
    val gregorianDate = MutableStateFlow(LocalDate.now().toString())
    val qiblaDirection = MutableStateFlow(0f)

    private val _currentTime = MutableStateFlow(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.getDefault())))
    val currentTime: StateFlow<String> = _currentTime.asStateFlow()

    val completionRate = MutableStateFlow(0.0f)
    val currentStreak = MutableStateFlow(0)
    
    private val _weeklyCompletion = MutableStateFlow<List<Boolean>>(List(7) { false })
    val weeklyCompletion: StateFlow<List<Boolean>> = _weeklyCompletion.asStateFlow()

    val tasbeehCount = MutableStateFlow(0)
    val wuduStatus = localSettings.wuduStatus.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
    val currentSurah = localSettings.currentSurah.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Al-Fatihah")
    val tahajjudTimeStr = localSettings.tahajjudTime.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "03:45 AM")

    val hadithArabic = MutableStateFlow("خَيْرُكُمْ مَنْ تَعَلَّمَ الْقُرْآنَ وَعَلَّمَهُ")
    val hadithEnglish = MutableStateFlow("The best among you are those who learn the Quran and teach it.")
    val hadithInfo = MutableStateFlow("Sahih al-Bukhari")

    private val _completedPrayers = MutableStateFlow<Set<String>>(emptySet())
    val completedPrayers: StateFlow<Set<String>> = _completedPrayers.asStateFlow()

    val habits: StateFlow<List<HabitEntity>> = habitRepository.getAllHabits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val dailyDua = MutableStateFlow<DuaEntity?>(null)

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private var lastCheckedDate = LocalDate.now()
    private val todayStr: String get() = LocalDate.now().format(dateFormatter)

    private var completionCollectorJob: Job? = null

    init {
        refreshAllData()
        startTimeTickers()
        viewModelScope.launch {
            habitRepository.resetDailyHabitsIfNecessary()
        }
    }

    private fun refreshAllData() {
        loadInitialData()
        loadLocalCounts()
        fetchHadithOfTheDay()
        loadCompletedPrayers() // This sets up the observer for the current day
        fetchDailyDua()
        loadWeeklyProgress()
        calculateStreak()
    }

    private fun startTimeTickers() {
        viewModelScope.launch {
            while (true) {
                val now = LocalTime.now()
                val nowDate = LocalDate.now()
                
                // Automatic Reset logic at 12:00 AM
                if (nowDate != lastCheckedDate) {
                    lastCheckedDate = nowDate
                    gregorianDate.value = nowDate.toString()
                    refreshAllData()
                    habitRepository.resetDailyHabitsIfNecessary()
                }

                _currentTime.value = now.format(DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.getDefault()))

                if (nextPrayerMillis.value > 0) {
                    val diff = nextPrayerMillis.value - System.currentTimeMillis()
                    if (diff > 0) {
                        val hours = (diff / (1000 * 60 * 60))
                        val minutes = (diff / (1000 * 60)) % 60
                        val seconds = (diff / 1000) % 60
                        countdown.value = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                    } else if (diff < -2000) { 
                        refreshPrayerData()
                    }
                }
                delay(1000)
            }
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val savedLocation = localSettings.userLocation.first()
            if (savedLocation != null) {
                _cityName.value = localSettings.cityName.first() ?: "My Location"
                fetchPrayerTimes(savedLocation.first, savedLocation.second)
            } else {
                fetchPrayerTimes(defaultLat, defaultLon)
                refreshLocation()
            }
        }
    }

    private fun fetchPrayerTimes(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val data = prayerRepository.getPrayerTimes(lat, lon, LocalDate.now())
                nextPrayerName.value = data.nextPrayer.name
                nextPrayerArabicName.value = data.nextPrayer.arabicName
                nextPrayerMillis.value = data.nextPrayerMillis
                sunriseTime.value = data.sunrise
                hijriDate.value = data.hijriDate
                gregorianDate.value = data.gregorianDate
                qiblaDirection.value = data.qiblaDirection.replace("°", "").toFloatOrNull() ?: 0f
                syncPrayersWithLocalDb(data)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun refreshPrayerData() {
        viewModelScope.launch {
            val location = localSettings.userLocation.first() ?: (defaultLat to defaultLon)
            fetchPrayerTimes(location.first, location.second)
        }
    }

    private suspend fun syncPrayersWithLocalDb(data: PrayerData) {
        val localRecords = prayerDao.getRecordsForDate(todayStr).first()
        val prayerList = data.allPrayers.map { detail ->
            val isPrayed = localRecords.find { it.prayerName == detail.name }?.isCompleted ?: false
            Prayer(detail.name, detail.time, isPrayed)
        }
        _prayers.value = prayerList
        
        updateCompletionMetrics(prayerList)
    }

    private fun updateCompletionMetrics(prayerList: List<Prayer>) {
        val prayerOnlyList = prayerList.filter { it.name != "Sunrise" && it.name != "Sunset" }
        val completedCount = prayerOnlyList.count { it.isPrayed }
        completionRate.value = if (prayerOnlyList.isNotEmpty()) completedCount.toFloat() / prayerOnlyList.size else 0f
    }

    private fun loadWeeklyProgress() {
        viewModelScope.launch {
            val today = LocalDate.now()
            val startOfWeek = today.with(DayOfWeek.MONDAY)
            val weekResults = mutableListOf<Boolean>()
            
            for (i in 0..6) {
                val date = startOfWeek.plusDays(i.toLong()).format(dateFormatter)
                val dayRecords = prayerDao.getRecordsForDate(date).first()
                val prayerOnlyRecords = dayRecords.filter { it.prayerName != "Sunrise" && it.prayerName != "Sunset" }
                val isPerfectDay = prayerOnlyRecords.isNotEmpty() && prayerOnlyRecords.all { it.isCompleted }
                weekResults.add(isPerfectDay)
            }
            _weeklyCompletion.value = weekResults
        }
    }

    private fun calculateStreak() {
        viewModelScope.launch {
            prayerDao.getAllRecords().first().let { allRecords ->
                if (allRecords.isEmpty()) {
                    currentStreak.value = 0
                    return@let
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
                currentStreak.value = streak
            }
        }
    }

    fun togglePrayerByName(prayerName: String) {
        viewModelScope.launch {
            val record = prayerDao.getRecord(todayStr, prayerName)
            val newStatus = if (record != null) !record.isCompleted else true
            if (record != null) prayerDao.insertRecord(record.copy(isCompleted = newStatus))
            else prayerDao.insertRecord(PrayerRecord(date = todayStr, prayerName = prayerName, isCompleted = newStatus))
            
            val currentList = _prayers.value.toMutableList()
            val index = currentList.indexOfFirst { it.name == prayerName }
            if (index != -1) {
                currentList[index] = currentList[index].copy(isPrayed = newStatus)
                _prayers.value = currentList
                updateCompletionMetrics(currentList)
            }
            loadWeeklyProgress()
            calculateStreak()
        }
    }

    fun togglePrayerState(prayer: Prayer) = togglePrayerByName(prayer.name)

    fun refreshLocation() {
        viewModelScope.launch {
            try {
                val location = locationService.getCurrentLocation()
                localSettings.saveLocation(location.latitude, location.longitude, "My Location")
                _cityName.value = "My Location"
                fetchPrayerTimes(location.latitude, location.longitude)
            } catch (e: Exception) {}
        }
    }

    private fun loadLocalCounts() {
        viewModelScope.launch {
            prayerDao.getTasbeehForDate(todayStr)?.let { tasbeehCount.value = it.totalCount } ?: run { tasbeehCount.value = 0 }
        }
    }

    private fun fetchHadithOfTheDay() {
        viewModelScope.launch {
            hadithRepository.getRandomHadith()?.let {
                hadithArabic.value = it.hadithArabic ?: ""
                hadithEnglish.value = it.hadithEnglish ?: ""
                hadithInfo.value = "${it.bookName}, Hadith ${it.hadithNumber}"
            }
        }
    }

    private fun fetchDailyDua() {
        viewModelScope.launch {
            duaRepository.preloadDuasIfEmpty()
            duaRepository.getAllDuas().first().let { list ->
                if (list.isNotEmpty()) {
                    dailyDua.value = list.random()
                }
            }
        }
    }

    private fun loadCompletedPrayers() {
        completionCollectorJob?.cancel()
        completionCollectorJob = viewModelScope.launch {
            prayerDao.getRecordsForDate(todayStr).collect { records ->
                _completedPrayers.value = records.filter { it.isCompleted }.map { it.prayerName }.toSet()
            }
        }
    }

    fun incrementHabit(habitId: Int) {
        viewModelScope.launch {
            habitRepository.incrementProgress(habitId)
        }
    }

    fun getNextPrayerTime(): String = prayers.value.find { it.name == nextPrayerName.value }?.time ?: "--:--"
    fun isAlarmSet() = true
    fun getQiblaDirection() = qiblaDirection.value
    fun toggleAlarm() {}
    fun toggleWuduStatus() { viewModelScope.launch { localSettings.updateWuduStatus(!wuduStatus.value) } }
}
