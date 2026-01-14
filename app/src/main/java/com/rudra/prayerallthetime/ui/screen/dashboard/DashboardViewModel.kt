package com.rudra.prayerallthetime.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.LocationService
import com.rudra.prayerallthetime.data.Prayer
import com.rudra.prayerallthetime.data.local.LocalSettings
import com.rudra.prayerallthetime.data.local.PrayerDao
import com.rudra.prayerallthetime.data.local.PrayerRecord
import com.rudra.prayerallthetime.data.repository.HadithRepository
import com.rudra.prayerallthetime.data.repository.PrayerRepository
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val prayerRepository: PrayerRepository,
    private val locationService: LocationService,
    private val localSettings: LocalSettings,
    private val prayerDao: PrayerDao,
    private val hadithRepository: HadithRepository
) : ViewModel() {

    private val _prayers = MutableStateFlow<List<Prayer>>(emptyList())
    val prayers: StateFlow<List<Prayer>> = _prayers.asStateFlow()

    private val _cityName = MutableStateFlow("Detecting...")
    val cityName: StateFlow<String> = _cityName.asStateFlow()

    val nextPrayerName = MutableStateFlow("Fajr")
    val nextPrayerArabicName = MutableStateFlow("")
    val countdown = MutableStateFlow("00:00:00")
    val nextPrayerMillis = MutableStateFlow(0L)
    val sunriseTime = MutableStateFlow("--:--")
    val hijriDate = MutableStateFlow("-- -- ----")
    val gregorianDate = MutableStateFlow(LocalDate.now().toString())
    val qiblaDirection = MutableStateFlow(0f)

    val isRamadan = MutableStateFlow(true)
    val tasbeehCount = MutableStateFlow(0)
    val taraweehCount = MutableStateFlow(0)
    val currentStreak = MutableStateFlow(7)
    val completionRate = MutableStateFlow(0.0f)

    val suhoorTime = MutableStateFlow("--:--")
    val iftarTime = MutableStateFlow("--:--")
    val ramadanDay = MutableStateFlow(0)

    val ayatArabic = MutableStateFlow("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ")
    val ayatEnglish = MutableStateFlow("In the name of Allah, the Entirely Merciful, the Especially Merciful.")
    val surahInfo = MutableStateFlow("Al-Fatihah 1:1")
    
    val hadithArabic = MutableStateFlow("خَيْرُكُمْ مَنْ تَعَلَّمَ الْقُرْآنَ وَعَلَّمَهُ")
    val hadithEnglish = MutableStateFlow("The best among you are those who learn the Quran and teach it.")
    val hadithInfo = MutableStateFlow("Sahih al-Bukhari")
    
    val isAyatBookmarked = MutableStateFlow(false)
    val isHadithBookmarked = MutableStateFlow(false)

    val wuduStatus = localSettings.wuduStatus.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
    val currentSurah = localSettings.currentSurah.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Al-Fatihah")
    val tahajjudTimeStr = localSettings.tahajjudTime.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "03:45 AM")

    private val _completedPrayers = MutableStateFlow<Set<String>>(emptySet())
    val completedPrayers: StateFlow<Set<String>> = _completedPrayers.asStateFlow()

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val todayStr = LocalDate.now().format(dateFormatter)

    init {
        loadInitialData()
        loadLocalCounts()
        fetchHadithOfTheDay()
        loadCompletedPrayers()
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

    private fun fetchHadithOfTheDay() {
        viewModelScope.launch {
            val hadith = hadithRepository.getRandomHadith()
            if (hadith != null) {
                hadithArabic.value = hadith.hadithArabic ?: ""
                hadithEnglish.value = hadith.hadithEnglish ?: ""
                hadithInfo.value = "${hadith.book.bookName}, Hadith ${hadith.hadithNumber}"
            }
        }
    }

    private fun loadLocalCounts() {
        viewModelScope.launch {
            val tasbeehRecord = prayerDao.getTasbeehForDate(todayStr)
            tasbeehCount.value = tasbeehRecord?.totalCount ?: 0
            
            val taraweehRecord = prayerDao.getTaraweehForDate(todayStr)
            taraweehCount.value = taraweehRecord?.rakatCount ?: 0
        }
    }

    private fun loadCompletedPrayers() {
        viewModelScope.launch {
            prayerDao.getRecordsForDate(todayStr).collect { records ->
                _completedPrayers.value = records.filter { it.isCompleted }.map { it.prayerName }.toSet()
            }
        }
    }

    private fun fetchPrayerTimes(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val data = prayerRepository.getPrayerTimes(lat, lon, LocalDate.now())
                nextPrayerName.value = data.nextPrayer.name
                nextPrayerArabicName.value = data.nextPrayer.arabicName
                countdown.value = data.countdown
                nextPrayerMillis.value = data.nextPrayerMillis
                sunriseTime.value = data.sunrise
                hijriDate.value = data.hijriDate
                gregorianDate.value = data.gregorianDate
                qiblaDirection.value = data.qiblaDirection.replace("°", "").toFloatOrNull() ?: 0f
                
                suhoorTime.value = data.allPrayers.find { it.name.equals("Fajr", true) }?.time ?: "--:--"
                iftarTime.value = data.allPrayers.find { it.name.equals("Maghrib", true) }?.time ?: "--:--"
                
                syncPrayersWithLocalDb(data)
            } catch (e: Exception) {
                // Error handling
            }
        }
    }

    private suspend fun syncPrayersWithLocalDb(data: PrayerData) {
        val localRecords = prayerDao.getRecordsForDate(todayStr).first()
        
        val prayerList = data.allPrayers.map { detail ->
            val isPrayed = localRecords.find { it.prayerName == detail.name }?.isCompleted ?: false
            Prayer(detail.name, detail.time, isPrayed)
        }
        _prayers.value = prayerList
        
        // Update completion rate
        val prayerOnlyList = prayerList.filter { it.name != "Sunrise" }
        val completedCount = prayerOnlyList.count { it.isPrayed }
        completionRate.value = if (prayerOnlyList.isNotEmpty()) completedCount.toFloat() / prayerOnlyList.size else 0f
    }

    fun refreshLocation() {
        viewModelScope.launch {
            try {
                val location = locationService.getCurrentLocation()
                val city = "Current Location" 
                localSettings.saveLocation(location.latitude, location.longitude, city)
                _cityName.value = city
                fetchPrayerTimes(location.latitude, location.longitude)
            } catch (e: Exception) {
                // Error handling
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
                
                // Refresh completion rate
                val prayerOnlyList = currentList.filter { it.name != "Sunrise" }
                val completedCount = prayerOnlyList.count { it.isPrayed }
                completionRate.value = if (prayerOnlyList.isNotEmpty()) completedCount.toFloat() / prayerOnlyList.size else 0f
            }
        }
    }

    fun togglePrayerByName(prayerName: String) {
        viewModelScope.launch {
            val record = prayerDao.getRecord(todayStr, prayerName)
            val newStatus = if (record != null) !record.isCompleted else true
            
            if (record != null) {
                prayerDao.insertRecord(record.copy(isCompleted = newStatus))
            } else {
                prayerDao.insertRecord(PrayerRecord(date = todayStr, prayerName = prayerName, isCompleted = newStatus))
            }
            
            // Also sync the _prayers list for UI consistency
            val currentList = _prayers.value.toMutableList()
            val index = currentList.indexOfFirst { it.name == prayerName }
            if (index != -1) {
                currentList[index] = currentList[index].copy(isPrayed = newStatus)
                _prayers.value = currentList
            }
        }
    }

    fun addTodayToStreak() {
        viewModelScope.launch {
            // Logic to increment streak in LocalSettings or DB
            val current = currentStreak.value
            currentStreak.value = current + 1
            // You might want to persist this in LocalSettings
        }
    }

    fun toggleWuduStatus() {
        viewModelScope.launch {
            localSettings.updateWuduStatus(!wuduStatus.value)
        }
    }

    fun toggleAyatBookmark() { isAyatBookmarked.value = !isAyatBookmarked.value }
    fun toggleHadithBookmark() { isHadithBookmarked.value = !isHadithBookmarked.value }
    fun shareContent(content: String) {}
    fun playAudio(text: String) {}
    
    fun getNextPrayerTime(): String = prayers.value.find { it.name == nextPrayerName.value }?.time ?: "--:--"
    fun isAlarmSet(): Boolean = true
    fun getQiblaDirection(): Float = qiblaDirection.value
    fun toggleAlarm() {}
}
