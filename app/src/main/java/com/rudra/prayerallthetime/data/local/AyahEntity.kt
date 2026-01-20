package com.rudra.prayerallthetime.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quran_ayats")
data class AyahEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val surah: Int,
    val ayah: Int,
    val text: String,
    val translation: String? = null,
    val surahName: String? = null,
    val isBookmarked: Boolean = false
)
