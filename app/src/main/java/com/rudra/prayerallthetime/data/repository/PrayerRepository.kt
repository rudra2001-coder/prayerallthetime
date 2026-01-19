package com.rudra.prayerallthetime.data.repository

import android.content.Context
import com.rudra.prayerallthetime.core.calendar.HijriCalendar
import com.rudra.prayerallthetime.core.config.CalculationMethod
import com.rudra.prayerallthetime.core.config.Madhab
import com.rudra.prayerallthetime.core.prayer.PrayerTimesCalculator
import com.rudra.prayerallthetime.data.local.PrayerDao
import com.rudra.prayerallthetime.data.local.PrayerTimeEntity
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerData
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerDetails
import com.rudra.prayerallthetime.util.NetworkUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrayerRepository @Inject constructor(
    private val prayerDao: PrayerDao,
    private val networkUtils: NetworkUtils,
    @ApplicationContext private val context: Context
) {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val timeFormat = java.text.SimpleDateFormat("hh:mm a", Locale.getDefault())

    suspend fun getPrayerTimes(latitude: Double, longitude: Double, date: LocalDate): PrayerData {
        val dateStr = date.format(dateFormatter)
        
        // 1. Try Local DB Cache
        val cachedEntity = prayerDao.getPrayerTimesByDate(dateStr)
        
        // 2. Decide whether to fetch from Remote or Offline Calculation
        val entity = if (networkUtils.isOnline()) {
            fetchRemoteAndCache(latitude, longitude, date)
        } else {
            cachedEntity ?: calculateWithCoreLogic(latitude, longitude, date)
        }

        // Using Internal Hijri Logic
        val hijriDateStr = HijriCalendar.getHijriDate(date)

        val allPrayers = listOf(
            PrayerDetails("Fajr", "الفجر", entity.fajr),
            PrayerDetails("Sunrise", "الشروق", entity.sunrise),
            PrayerDetails("Dhuhr", "الظهر", entity.dhuhr),
            PrayerDetails("Asr", "العصر", entity.asr),
            PrayerDetails("Maghrib", "المغرب", entity.maghrib),
            PrayerDetails("Isha", "العشاء", entity.isha)
        )

        val now = Calendar.getInstance()
        val nextPrayer = findNextPrayer(allPrayers, now)

        return PrayerData(
            nextPrayer = nextPrayer,
            allPrayers = allPrayers,
            countdown = calculateCountdown(nextPrayer.time),
            nextPrayerMillis = parseTimeToMillis(nextPrayer.time, date),
            sunrise = entity.sunrise,
            hijriDate = hijriDateStr,
            gregorianDate = date.toString(),
            city = entity.city,
            qiblaDirection = calculateQibla(latitude, longitude),
            prayerProgress = calculateProgress(allPrayers, now),
            tahajjudTime = entity.isha 
        )
    }

    private suspend fun fetchRemoteAndCache(latitude: Double, longitude: Double, date: LocalDate): PrayerTimeEntity {
        return calculateWithCoreLogic(latitude, longitude, date).apply {
            prayerDao.insertPrayerTimes(this)
        }
    }

    private fun calculateWithCoreLogic(latitude: Double, longitude: Double, date: LocalDate): PrayerTimeEntity {
        val calculator = PrayerTimesCalculator(CalculationMethod.BANGLADESH, Madhab.SHAFI)
        
        val tz = TimeZone.getDefault().rawOffset / (1000.0 * 60 * 60)
        val times = calculator.calculatePrayerTimes(date, latitude, longitude, tz)

        return PrayerTimeEntity(
            date = date.format(dateFormatter),
            fajr = times.fajr,
            sunrise = times.sunrise,
            dhuhr = times.dhuhr,
            asr = times.asr,
            maghrib = times.maghrib,
            isha = times.isha,
            hijriDate = HijriCalendar.getHijriDate(date),
            city = "Current Location",
            latitude = latitude,
            longitude = longitude,
            isFromApi = false
        )
    }

    private fun findNextPrayer(prayers: List<PrayerDetails>, now: Calendar): PrayerDetails {
        val currentTime = timeFormat.format(now.time)
        for (prayer in prayers) {
            if (prayer.name == "Sunrise") continue
            if (isTimeAfter(prayer.time, currentTime)) {
                return prayer
            }
        }
        return prayers[0] // Fajr
    }

    private fun isTimeAfter(time1: String, time2: String): Boolean {
        return try {
            val d1 = timeFormat.parse(time1)
            val d2 = timeFormat.parse(time2)
            d1?.after(d2) ?: false
        } catch (e: Exception) {
            false
        }
    }

    private fun parseTimeToMillis(timeStr: String, date: LocalDate): Long {
        return try {
            val time = timeFormat.parse(timeStr) ?: return 0L
            val cal = Calendar.getInstance()
            cal.time = time
            val result = Calendar.getInstance()
            result.set(date.year, date.monthValue - 1, date.dayOfMonth, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
            result.timeInMillis
        } catch (e: Exception) {
            0L
        }
    }

    private fun calculateCountdown(targetTimeStr: String): String {
        val now = System.currentTimeMillis()
        val target = parseTimeToMillis(targetTimeStr, LocalDate.now())
        var diff = target - now
        
        if (diff <= 0) {
            val tomorrowTarget = parseTimeToMillis(targetTimeStr, LocalDate.now().plusDays(1))
            diff = tomorrowTarget - now
        }
        
        if (diff <= 0) return "00:00:00"
        
        val hours = diff / (1000 * 60 * 60)
        val minutes = (diff / (1000 * 60)) % 60
        val seconds = (diff / 1000) % 60
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun calculateProgress(prayers: List<PrayerDetails>, now: Calendar): Float {
        val currentMillis = now.timeInMillis
        val dayStart = parseTimeToMillis(prayers[0].time, LocalDate.now())
        val dayEnd = parseTimeToMillis(prayers.last().time, LocalDate.now())
        
        if (currentMillis < dayStart) return 0f
        if (currentMillis > dayEnd) return 1f
        
        return (currentMillis - dayStart).toFloat() / (dayEnd - dayStart).toFloat()
    }

    private fun calculateQibla(latitude: Double, longitude: Double): String {
        val mLat = 21.4225
        val mLon = 39.8262
        
        val latRad = Math.toRadians(latitude)
        val lonRad = Math.toRadians(longitude)
        val mLatRad = Math.toRadians(mLat)
        val mLonRad = Math.toRadians(mLon)
        
        val y = Math.sin(mLonRad - lonRad)
        val x = Math.cos(latRad) * Math.tan(mLatRad) - Math.sin(latRad) * Math.cos(mLonRad - lonRad)
        var qibla = Math.toDegrees(Math.atan2(y, x))
        if (qibla < 0) qibla += 360.0
        return "${qibla.toInt()}°"
    }
}
