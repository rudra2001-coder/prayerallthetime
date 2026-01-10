package com.rudra.prayerallthetime.data

class PrayerDataSource {
    fun getPrayers(): List<Prayer> {
        return listOf(
            Prayer("Fajr", "05:41 AM"),
            Prayer("Dhuhr", "01:30 PM"),
            Prayer("Asr", "05:00 PM"),
            Prayer("Maghrib", "07:00 PM"),
            Prayer("Isha", "08:30 PM")
        )
    }
}
