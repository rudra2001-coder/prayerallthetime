package com.rudra.prayerallthetime.ui.screen.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.LocationService
import com.rudra.prayerallthetime.data.Prayer
import com.rudra.prayerallthetime.data.local.LocalSettings
import com.rudra.prayerallthetime.data.local.PrayerDao
import com.rudra.prayerallthetime.data.local.PrayerRecord
import com.rudra.prayerallthetime.data.repository.HadithRepository
import com.rudra.prayerallthetime.data.repository.PrayerRepository
import com.rudra.prayerallthetime.ui.navigation.Screen

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

    // Data-driven Feature List
//    val dashboardItems = listOf(
//        DashboardItem("Prayers", Icons.Default.AccessTime, Screen.Prayers.route),
//        DashboardItem("Quran", Icons.Default.MenuBook, Screen.QuranHadith.route),
//        DashboardItem("Hadith", Icons.Default.HistoryEdu, Screen.Hadith.route),
//        DashboardItem("Tasbeeh", Icons.Default.Favorite, Screen.Tasbeeh.route),
//        DashboardItem("Qibla", Icons.Default.Explore, Screen.Qibla.route),
//        DashboardItem("Ramadan", Icons.Default.NightsStay, Screen.Ramadan.route),
//        DashboardItem("Analytics", Icons.Default.Analytics, Screen.Analytics.route),
//        DashboardItem("Family", Icons.Default.People, Screen.Family.route),
//        DashboardItem("Wudu Guide", Icons.Default.WaterDrop, Screen.Wudu.route)
//    )

    private val _prayers = MutableStateFlow<List<Prayer>>(emptyList())
    val prayers: StateFlow<List<Prayer>> = _prayers.asStateFlow()

    private val _cityName = MutableStateFlow("Dhaka")
    val cityName: StateFlow<String> = _cityName.asStateFlow()

    val nextPrayerName = MutableStateFlow("Fajr")
    val countdown = MutableStateFlow("00:00:00")
    val nextPrayerMillis = MutableStateFlow(0L)
    val sunriseTime = MutableStateFlow("--:--")
    val hijriDate = MutableStateFlow("-- -- ----")
    val gregorianDate = MutableStateFlow(LocalDate.now().toString())

    val isRamadan = MutableStateFlow(true)
    val tasbeehCount = MutableStateFlow(0)
    val taraweehCount = MutableStateFlow(0)
    val currentStreak = MutableStateFlow(7)
    val completionRate = MutableStateFlow(0.85f)


    val fastingCountdown = MutableStateFlow("12:34:56")
    val suhoorTime = MutableStateFlow("04:30 AM")
    val iftarTime = MutableStateFlow("06:15 PM")
    val ramadanDay = MutableStateFlow(10)

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

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val todayStr = LocalDate.now().format(dateFormatter)

    init {
        loadInitialData()
        loadLocalCounts()
        fetchHadithOfTheDay()
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

    private fun fetchPrayerTimes(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val data = prayerRepository.getPrayerTimes(lat, lon, LocalDate.now())
                nextPrayerName.value = data.nextPrayer.name
                countdown.value = data.countdown
                nextPrayerMillis.value = data.nextPrayerMillis
                sunriseTime.value = data.sunrise
                hijriDate.value = data.hijriDate
                gregorianDate.value = data.gregorianDate
                
                syncPrayersWithLocalDb(data)
            } catch (e: Exception) {
                // Handle error
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
        viewModelScope.launch {
            try {
                val location = locationService.getCurrentLocation()
                val city = "Detected Location" 
                localSettings.saveLocation(location.latitude, location.longitude, city)
                _cityName.value = city
                fetchPrayerTimes(location.latitude, location.longitude)
            } catch (e: Exception) {
                // Handle error
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

    fun toggleAyatBookmark() { isAyatBookmarked.value = !isAyatBookmarked.value }
    fun toggleHadithBookmark() { isHadithBookmarked.value = !isHadithBookmarked.value }
    fun shareContent(content: String) {}
    fun playAudio(text: String) {}
    
    fun getNextPrayerTime(): String = nextPrayerName.value
    fun isAlarmSet(): Boolean = true
    fun getQiblaDirection(): Float = 0f // Placeholder
    fun toggleAlarm() {}
}
