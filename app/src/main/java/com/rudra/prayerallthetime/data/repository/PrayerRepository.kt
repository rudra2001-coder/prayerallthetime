package com.rudra.prayerallthetime.data.repository

import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.Qibla
import com.batoulapps.adhan.data.DateComponents
import com.rudra.prayerallthetime.ui.PrayerData
import com.rudra.prayerallthetime.ui.PrayerDetails
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
        
        val prayerTimes = PrayerTimes(coordinates, dateComponents, params)
        val qibla = Qibla(coordinates)

        // Find next prayer
        val nextPrayer = prayerTimes.nextPrayer()
        val nextPrayerTime = prayerTimes.timeForPrayer(nextPrayer)
        val nextPrayerName = nextPrayer.name
        
        // Simple formatting for demonstration
        val timeFormat = java.text.SimpleDateFormat("hh:mm AM", java.util.Locale.getDefault())
        
        return PrayerData(
            nextPrayer = PrayerDetails(
                name = nextPrayerName,
                arabicName = getArabicName(nextPrayerName),
                time = if (nextPrayerTime != null) timeFormat.format(nextPrayerTime) else "--:--"
            ),
            countdown = calculateCountdown(nextPrayerTime),
            sunrise = timeFormat.format(prayerTimes.sunrise),
            hijriDate = "Calculation Pending",
            gregorianDate = date.toString(),
            city = "Current Location",
            qiblaDirection = "${qibla.direction.toInt()}°",
            prayerProgress = calculateProgress(prayerTimes)
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
        return 0.5f // Placeholder
    }
}
