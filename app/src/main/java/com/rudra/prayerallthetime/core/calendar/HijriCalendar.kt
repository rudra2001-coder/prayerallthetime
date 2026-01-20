package com.rudra.prayerallthetime.core.calendar

import java.time.LocalDate
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object HijriCalendar {
    private val monthNames = arrayOf(
        "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
        "Jumada al-Ula", "Jumada al-Akhira", "Rajab", "Sha'ban",
        "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
    )

    fun getTodayHijri(): String {
        return getHijriDate(LocalDate.now())
    }

    fun getHijriDate(date: LocalDate): String {
        return try {
            val hijrahDate = HijrahDate.from(date)
            val day = hijrahDate.get(java.time.temporal.ChronoField.DAY_OF_MONTH)
            val month = hijrahDate.get(java.time.temporal.ChronoField.MONTH_OF_YEAR)
            val year = hijrahDate.get(java.time.temporal.ChronoField.YEAR)
            
            "$day ${monthNames[month - 1]} $year AH"
        } catch (e: Exception) {
            "Offline Hijri"
        }
    }
}
