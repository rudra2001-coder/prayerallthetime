package com.rudra.prayerallthetime.data.repository

import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.Madhab
import com.batoulapps.adhan.Prayer
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.Qibla
import com.batoulapps.adhan.SunnahTimes
import com.batoulapps.adhan.data.DateComponents
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerData
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerDetails
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrayerRepository @Inject constructor() {

    suspend fun getPrayerTimes(latitude: Double, longitude: Double, date: LocalDate): PrayerData {
        val coordinates = Coordinates(latitude, longitude)
        val dateComponents = DateComponents.from(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))
        val params = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters
        params.madhab = Madhab.HANAFI // Set based on user preference if possible
        
        val prayerTimes = PrayerTimes(coordinates, dateComponents, params)
        val qibla = Qibla(coordinates)
        val sunnahTimes = SunnahTimes(prayerTimes)

        val nextPrayerEnum = prayerTimes.nextPrayer()
        val nextPrayerTime = prayerTimes.timeForPrayer(nextPrayerEnum)
        val nextPrayerName = if (nextPrayerEnum != Prayer.NONE) nextPrayerEnum.name else "Fajr"
        
        val timeFormat = java.text.SimpleDateFormat("hh:mm AM", java.util.Locale.getDefault())
        
        return PrayerData(
            nextPrayer = PrayerDetails(
                name = nextPrayerName,
                arabicName = getArabicName(nextPrayerName),
                time = if (nextPrayerTime != null) timeFormat.format(nextPrayerTime) else "--:--"
            ),
            countdown = calculateCountdown(nextPrayerTime),
            nextPrayerMillis = nextPrayerTime?.time ?: 0L,
            sunrise = timeFormat.format(prayerTimes.sunrise),
            hijriDate = "Calculation Pending", // Consider using a Hijri library or API for accurate Hijri
            gregorianDate = date.toString(),
            city = "Current Location",
            qiblaDirection = "${qibla.direction.toInt()}°",
            prayerProgress = calculateProgress(prayerTimes),
            tahajjudTime = timeFormat.format(sunnahTimes.lastThirdOfTheNight)
        )
    }

    private fun getArabicName(englishName: String): String {
        return when (englishName.uppercase()) {
            "FAJR" -> "الفجر"
            "DHUHR" -> "الظهر"
            "ASR" -> "العصر"
            "MAGHRIB" -> "المغرب"
            "ISHA" -> "العشاء"
            else -> ""
        }
    }

    private fun calculateCountdown(targetDate: Date?): String {
        if (targetDate == null) return "00:00:00"
        val diff = targetDate.time - System.currentTimeMillis()
        if (diff <= 0) return "00:00:00"
        
        val hours = diff / (1000 * 60 * 60)
        val minutes = (diff / (1000 * 60)) % 60
        val seconds = (diff / 1000) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun calculateProgress(times: PrayerTimes): Float {
        val now = Date().time
        val current = times.currentPrayer()
        val next = times.nextPrayer()
        
        val startTime = times.timeForPrayer(current)?.time ?: (now - 3600000)
        val endTime = times.timeForPrayer(next)?.time ?: (now + 3600000)
        
        if (now >= endTime) return 1f
        if (now <= startTime) return 0f
        
        return (now - startTime).toFloat() / (endTime - startTime).toFloat()
    }
}
