package com.rudra.prayerallthetime.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quran_ayats")
data class AyahEntity(
    @PrimaryKey
    val number: Int, // Global ayah number
    val surah: Int,
    val ayah: Int, // Number in surah
    val text: String,
    val translation: String? = null,
    val translationBn: String? = null,
    val audioUrl: String? = null,
    val surahName: String? = null,
    val isBookmarked: Boolean = false,
    val isDownloaded: Boolean = false,
    val localAudioPath: String? = null
)
