package com.rudra.prayerallthetime.ui

import android.app.Application
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.IslamicCalendar
import android.util.Log
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.Qibla
import com.batoulapps.adhan.data.DateComponents
import com.rudra.prayerallthetime.data.Prayer
import com.rudra.prayerallthetime.data.RetrofitInstance
import com.rudra.prayerallthetime.data.local.AppDatabase
import com.rudra.prayerallthetime.data.local.FamilyMemberRecord
import com.rudra.prayerallthetime.data.local.PrayerRecord
import com.rudra.prayerallthetime.data.local.TaraweehRecord
import com.rudra.prayerallthetime.data.local.TasbeehRecord
import com.rudra.prayerallthetime.ui.components.Badge
import com.rudra.prayerallthetime.ui.components.DayData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.random.Random

// Enums for UI state
enum class TimeRange(val displayName: String) {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    YEARLY("Yearly")
}

enum class TimePeriod(val displayName: String) {
    WEEKLY("Weekly"),
    MONTHLY("Monthly")
}

class PrayerViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val prayerDao = db.prayerDao()

    private val _prayers = MutableStateFlow<List<Prayer>>(emptyList())
    val prayers: StateFlow<List<Prayer>> = _prayers

    private val _nextPrayerName = MutableStateFlow("")
    val nextPrayerName: StateFlow<String> = _nextPrayerName

    private val _countdown = MutableStateFlow("00:00:00")
    val countdown: StateFlow<String> = _countdown

    private val _sunriseTime = MutableStateFlow("--:--")
    val sunriseTime: StateFlow<String> = _sunriseTime

    private val _hijriDate = MutableStateFlow("")
    val hijriDate: StateFlow<String> = _hijriDate

    private val _gregorianDate = MutableStateFlow("")
    val gregorianDate: StateFlow<String> = _gregorianDate

    private val _cityName = MutableStateFlow("Dhaka")
    val cityName: StateFlow<String> = _cityName

    private var currentCoordinates = Coordinates(23.6556256, 90.6257555) // Default to Dhaka

    // Qibla State
    private val _qiblaDirection = MutableStateFlow(0f)
    val qiblaDirection: StateFlow<Float> = _qiblaDirection

    // Fasting / Ramadan State
    private val _isRamadan = MutableStateFlow(false)
    val isRamadan: StateFlow<Boolean> = _isRamadan

    private val _ramadanDay = MutableStateFlow(0)
    val ramadanDay: StateFlow<Int> = _ramadanDay

    private val _suhoorTime = MutableStateFlow("--:--")
    val suhoorTime: StateFlow<String> = _suhoorTime

    private val _iftarTime = MutableStateFlow("--:--")
    val iftarTime: StateFlow<String> = _iftarTime

    private val _fastingCountdown = MutableStateFlow("00:00:00")
    val fastingCountdown: StateFlow<String> = _fastingCountdown

    private val _taraweehCount = MutableStateFlow(0)
    val taraweehCount: StateFlow<Int> = _taraweehCount

    // Tasbeeh State
    private val _tasbeehCount = MutableStateFlow(0)
    val tasbeehCount: StateFlow<Int> = _tasbeehCount

    // Family State
    private val _familyMembers = MutableStateFlow<List<FamilyMemberRecord>>(emptyList())
    val familyMembers: StateFlow<List<FamilyMemberRecord>> = _familyMembers

    // Ayat of the Day State
    private val _ayatArabic = MutableStateFlow("Loading...")
    val ayatArabic: StateFlow<String> = _ayatArabic

    private val _ayatEnglish = MutableStateFlow("")
    val ayatEnglish: StateFlow<String> = _ayatEnglish

    private val _surahInfo = MutableStateFlow("")
    val surahInfo: StateFlow<String> = _surahInfo

    private val _isAyatBookmarked = MutableStateFlow(false)
    val isAyatBookmarked: StateFlow<Boolean> = _isAyatBookmarked

    // Hadith State
    private val _isHadithBookmarked = MutableStateFlow(false)
    val isHadithBookmarked: StateFlow<Boolean> = _isHadithBookmarked

    private val _isAlarmSet = MutableStateFlow(true)

    // Analytics State
    private val _prayerStats = MutableStateFlow<Map<String, Float>>(emptyMap())
    val prayerStats: StateFlow<Map<String, Float>> = _prayerStats

    private val _completionRate = MutableStateFlow(0f)
    val completionRate: StateFlow<Float> = _completionRate

    private val _weeklyDayData = MutableStateFlow<List<DayData>>(emptyList())
    val weeklyDayData: StateFlow<List<DayData>> = _weeklyDayData

    private val _currentStreak = MutableStateFlow(0)
    val currentStreak: StateFlow<Int> = _currentStreak

    // Badges State
    private val _earnedBadges = MutableStateFlow<List<Badge>>(emptyList())
    val earnedBadges: StateFlow<List<Badge>> = _earnedBadges

    private val _upcomingBadges = MutableStateFlow<List<Badge>>(emptyList())
    val upcomingBadges: StateFlow<List<Badge>> = _upcomingBadges

    init {
        updateLocation(23.6556256, 90.6257555) // Initial call
        calculateHijriDate()
        calculateGregorianDate()
        fetchAyatOfTheDay()
        startTimers()
        observePrayerRecords()
        loadTasbeehAndTaraweeh()
        observeFamilyMembers()
        calculateAnalytics()
    }

    private fun observePrayerRecords() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModelScope.launch {
            prayerDao.getRecordsForDate(today).collectLatest { records ->
                val recordMap = records.associateBy { it.prayerName }
                _prayers.value = _prayers.value.map { prayer ->
                    prayer.copy(isPrayed = recordMap[prayer.name]?.isCompleted ?: false)
                }
            }
        }
    }

    private fun loadTasbeehAndTaraweeh() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModelScope.launch {
            val tasbeeh = prayerDao.getTasbeehForDate(today)
            _tasbeehCount.value = tasbeeh?.totalCount ?: 0

            val taraweeh = prayerDao.getTaraweehForDate(today)
            _taraweehCount.value = taraweeh?.rakatCount ?: 0
        }
    }

    private fun observeFamilyMembers() {
        viewModelScope.launch {
            prayerDao.getAllFamilyMembers().collectLatest { members ->
                _familyMembers.value = members
            }
        }
    }

    fun addFamilyMember(name: String) {
        viewModelScope.launch {
            prayerDao.insertFamilyMember(FamilyMemberRecord(name = name, completedPrayers = Random.nextInt(0, 6)))
        }
    }

    fun removeFamilyMember(member: FamilyMemberRecord) {
        viewModelScope.launch {
            prayerDao.deleteFamilyMember(member)
        }
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        currentCoordinates = Coordinates(latitude, longitude)
        _cityName.value = "Dhaka"
        calculatePrayerTimes()
        calculateQibla()
    }

    private fun calculatePrayerTimes() {
        val dateComponents = DateComponents.from(Date())
        val params = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters

        val prayerTimes = PrayerTimes(currentCoordinates, dateComponents, params)
        val formatter = java.text.SimpleDateFormat("hh:mm a", Locale.getDefault())

        val prayerList = listOf(
            Prayer("Fajr", formatter.format(prayerTimes.fajr)),
            Prayer("Dhuhr", formatter.format(prayerTimes.dhuhr)),
            Prayer("Asr", formatter.format(prayerTimes.asr)),
            Prayer("Maghrib", formatter.format(prayerTimes.maghrib)),
            Prayer("Isha", formatter.format(prayerTimes.isha))
        )
        
        _prayers.value = prayerList
        _sunriseTime.value = formatter.format(prayerTimes.sunrise)
        _suhoorTime.value = formatter.format(prayerTimes.fajr)
        _iftarTime.value = formatter.format(prayerTimes.maghrib)
    }

    private fun calculateHijriDate() {
        val islamicCalendar = IslamicCalendar()
        val day = islamicCalendar.get(IslamicCalendar.DAY_OF_MONTH)
        val monthIdx = islamicCalendar.get(IslamicCalendar.MONTH)
        
        val sdf = SimpleDateFormat("MMMM", Locale.getDefault())
        sdf.calendar = islamicCalendar
        val monthName = sdf.format(islamicCalendar.time)
        
        val year = islamicCalendar.get(IslamicCalendar.YEAR)
        _hijriDate.value = "$day $monthName $year AH"
        _isRamadan.value = monthIdx == 8 // RAMADAN index
        _ramadanDay.value = day
    }

    private fun calculateGregorianDate() {
        val sdf = java.text.SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        _gregorianDate.value = sdf.format(Date())
    }

    private fun fetchAyatOfTheDay() {
        viewModelScope.launch {
            try {
                val randomAyah = Random.nextInt(1, 6236)
                val response = RetrofitInstance.api.getAyatOfTheDay(randomAyah)
                if (response.code == 200 && response.data.size >= 2) {
                    _ayatArabic.value = response.data[0].text
                    _ayatEnglish.value = response.data[1].text
                    _surahInfo.value = "${response.data[0].surah.englishName}, Ayah ${response.data[0].numberInSurah}"
                }
            } catch (e: Exception) {
                Log.e("PrayerViewModel", "Error fetching ayat: ${e.message}")
                _ayatArabic.value = "يَا أَيُّهَا الَّذِينَ آمَنُوا اسْتَعِينُوا بِالصَّبْرِ وَالصَّلَاةِ ۚ إِنَّ اللَّهَ مَعَ الصَّابِرِينَ"
                _ayatEnglish.value = "O you who have believed, seek help through patience and prayer. Indeed, Allah is with the patient."
                _surahInfo.value = "Surah Al-Baqarah, 153"
            }
        }
    }

    fun shareContent(text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        val shareIntent = Intent.createChooser(intent, null).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        getApplication<Application>().startActivity(shareIntent)
    }

    fun toggleAyatBookmark() {
        _isAyatBookmarked.value = !_isAyatBookmarked.value
        val message = if (_isAyatBookmarked.value) "Ayat Bookmarked" else "Bookmark Removed"
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
    }

    fun toggleHadithBookmark() {
        _isHadithBookmarked.value = !_isHadithBookmarked.value
        val message = if (_isHadithBookmarked.value) "Hadith Bookmarked" else "Bookmark Removed"
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
    }

    fun playAudio(text: String) {
        Toast.makeText(getApplication(), "Playing audio...", Toast.LENGTH_SHORT).show()
    }

    fun getNextPrayerTime(): String {
        val dateComponents = DateComponents.from(Date())
        val params = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters
        val prayerTimes = PrayerTimes(currentCoordinates, dateComponents, params)
        val nextPrayer = prayerTimes.nextPrayer()
        val time = prayerTimes.timeForPrayer(nextPrayer)
        return if (time != null) {
            val formatter = java.text.SimpleDateFormat("hh:mm a", Locale.getDefault())
            formatter.format(time)
        } else "--:--"
    }

    fun isAlarmSet(): Boolean = _isAlarmSet.value

    fun toggleAlarm() {
        _isAlarmSet.value = !_isAlarmSet.value
    }

    fun getQiblaDirection(): Float = _qiblaDirection.value

    fun updateChartRange(range: TimeRange) {}

    fun updateTimePeriod(period: TimePeriod) {}

    private fun calculateQibla() {
        _qiblaDirection.value = Qibla(currentCoordinates).direction.toFloat()
    }

    private fun startTimers() {
        viewModelScope.launch {
            while (true) {
                updatePrayerCountdown()
                updateFastingCountdown()
                delay(1000)
            }
        }
    }

    private fun updatePrayerCountdown() {
        val dateComponents = DateComponents.from(Date())
        val params = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters
        val prayerTimes = PrayerTimes(currentCoordinates, dateComponents, params)

        val now = Date()
        val nextPrayer = prayerTimes.nextPrayer()
        val nextPrayerTime = prayerTimes.timeForPrayer(nextPrayer)

        if (nextPrayerTime != null) {
            val diff = nextPrayerTime.time - now.time
            if (diff > 0) {
                _countdown.value = formatMillis(diff)
                _nextPrayerName.value = nextPrayer.name
            } else {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_YEAR, 1)
                val tomorrowComponents = DateComponents.from(calendar.time)
                val tomorrowTimes = PrayerTimes(currentCoordinates, tomorrowComponents, params)
                val tomorrowFajr = tomorrowTimes.fajr
                val nextDiff = tomorrowFajr.time - now.time
                _countdown.value = formatMillis(nextDiff)
                _nextPrayerName.value = "Fajr"
            }
        }
    }

    private fun updateFastingCountdown() {
        val dateComponents = DateComponents.from(Date())
        val prayerTimes = PrayerTimes(currentCoordinates, dateComponents, CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters)
        val now = Date()
        val maghribTime = prayerTimes.maghrib.time
        val diff = if (now.time < maghribTime) maghribTime - now.time else 0L
        _fastingCountdown.value = if (diff > 0) formatMillis(diff) else "00:00:00"
    }

    private fun formatMillis(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun togglePrayerState(prayer: Prayer) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModelScope.launch {
            val existing = prayerDao.getRecord(today, prayer.name)
            val updatedRecord = PrayerRecord(
                id = existing?.id ?: 0,
                date = today,
                prayerName = prayer.name,
                isCompleted = !prayer.isPrayed
            )
            prayerDao.insertRecord(updatedRecord)
            calculateAnalytics() // Refresh stats after update
        }
    }

    private fun calculateAnalytics() {
        viewModelScope.launch {
            prayerDao.getAllRecords().collectLatest { allRecords ->
                if (allRecords.isEmpty()) {
                    _prayerStats.value = emptyMap()
                    _completionRate.value = 0f
                    _weeklyDayData.value = emptyList()
                    _currentStreak.value = 0
                    return@collectLatest
                }

                // 1. Per prayer stats
                val prayerGroups = allRecords.groupBy { it.prayerName }
                val stats = prayerGroups.mapValues { entry ->
                    val completedCount = entry.value.count { it.isCompleted }
                    val totalDays = allRecords.map { it.date }.distinct().size
                    if (totalDays > 0) completedCount.toFloat() / totalDays else 0f
                }
                _prayerStats.value = stats

                // 2. Overall completion rate
                val totalPossible = allRecords.map { it.date }.distinct().size * 5
                val totalCompleted = allRecords.count { it.isCompleted }
                _completionRate.value = if (totalPossible > 0) totalCompleted.toFloat() / totalPossible else 0f

                // 3. Weekly breakdown
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
                val fullNameFormat = SimpleDateFormat("EEEE", Locale.getDefault())
                
                val weeklyList = mutableListOf<DayData>()
                for (i in 6 downTo 0) {
                    val tempCal = Calendar.getInstance()
                    tempCal.add(Calendar.DAY_OF_YEAR, -i)
                    val dateStr = dateFormat.format(tempCal.time)
                    val dayRecords = allRecords.filter { it.date == dateStr }
                    val completedCount = dayRecords.count { it.isCompleted }
                    
                    weeklyList.add(DayData(
                        dayName = fullNameFormat.format(tempCal.time),
                        dayAbbr = dayFormat.format(tempCal.time),
                        completedPrayers = completedCount,
                        completionRate = completedCount.toFloat() / 5f,
                        isToday = i == 0
                    ))
                }
                _weeklyDayData.value = weeklyList

                // 4. Streak calculation
                var streak = 0
                val sortedDates = allRecords.filter { it.isCompleted }
                    .groupBy { it.date }
                    .filter { it.value.size == 5 }
                    .keys.sortedDescending()
                
                if (sortedDates.isNotEmpty()) {
                    val checkCal = Calendar.getInstance()
                    val todayStr = dateFormat.format(checkCal.time)
                    
                    var expectedDate = todayStr
                    if (!sortedDates.contains(todayStr)) {
                        checkCal.add(Calendar.DAY_OF_YEAR, -1)
                        expectedDate = dateFormat.format(checkCal.time)
                    }

                    for (date in sortedDates) {
                        if (date == expectedDate) {
                            streak++
                            checkCal.add(Calendar.DAY_OF_YEAR, -1)
                            expectedDate = dateFormat.format(checkCal.time)
                        } else {
                            break
                        }
                    }
                }
                _currentStreak.value = streak

                // 5. Badges Logic
                updateBadges(totalCompleted, streak, allRecords.count { it.prayerName == "Fajr" && it.isCompleted })
            }
        }
    }

    private fun updateBadges(totalCompleted: Int, streak: Int, fajrCount: Int) {
        val earned = mutableListOf<Badge>()
        val upcoming = mutableListOf<Badge>()

        // Consistency Badge
        if (streak >= 7) {
            earned.add(Badge(4, "Consistency", "Perfect prayers for 7 days", Color(0xFF45B7D1), iconImage = Icons.Default.AutoAwesome, dateEarned = "Earned"))
        } else {
            upcoming.add(Badge(4, "Consistency", "Perfect prayers for 7 days", Color(0xFF45B7D1), iconImage = Icons.Default.AutoAwesome, requirement = "7 days perfect prayers"))
        }

        // Early Riser
        if (fajrCount >= 30) {
            earned.add(Badge(1, "Early Riser", "Pray Fajr on time for 30 days", Color(0xFFFFD700), icon = "☀️", dateEarned = "Earned"))
        } else {
            upcoming.add(Badge(1, "Early Riser", "Pray Fajr on time for 30 days", Color(0xFFFFD700), icon = "☀️", requirement = "$fajrCount/30 Fajr prayers"))
        }

        // 100 Prayers
        if (totalCompleted >= 100) {
            earned.add(Badge(7, "1000 Prayers", "Complete 100 prayers", Color(0xFF6A5ACD), iconImage = Icons.Default.Star, dateEarned = "Earned"))
        } else {
            upcoming.add(Badge(7, "1000 Prayers", "Complete 100 prayers", Color(0xFF6A5ACD), iconImage = Icons.Default.Star, requirement = "$totalCompleted/100 recorded prayers"))
        }

        _earnedBadges.value = earned
        _upcomingBadges.value = upcoming
    }

    fun incrementTasbeeh() {
        _tasbeehCount.value++
        saveTasbeeh()
    }

    fun resetTasbeeh() {
        _tasbeehCount.value = 0
        saveTasbeeh()
    }

    private fun saveTasbeeh() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModelScope.launch {
            prayerDao.insertTasbeeh(TasbeehRecord(today, _tasbeehCount.value))
        }
    }

    fun incrementTaraweeh() {
        if (_taraweehCount.value < 20) {
            _taraweehCount.value += 2
            saveTaraweeh()
        }
    }

    fun resetTaraweeh() {
        _taraweehCount.value = 0
        saveTaraweeh()
    }

    private fun saveTaraweeh() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModelScope.launch {
            prayerDao.insertTaraweeh(TaraweehRecord(today, _taraweehCount.value))
        }
    }
}
