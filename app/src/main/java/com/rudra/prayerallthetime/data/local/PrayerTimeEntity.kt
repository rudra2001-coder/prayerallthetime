package com.rudra.prayerallthetime.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer_times")
data class PrayerTimeEntity(
    @PrimaryKey val date: String, // yyyy-MM-dd
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String,
    val hijriDate: String,
    val city: String,
    val latitude: Double,
    val longitude: Double,
    val method: Int? = null,
    val isFromApi: Boolean = false
)
