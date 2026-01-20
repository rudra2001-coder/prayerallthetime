package com.rudra.prayerallthetime.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hadiths")
data class HadithEntity(
    @PrimaryKey
    val id: Int,
    val hadithNumber: String,
    val englishNarrator: String?,
    val hadithEnglish: String?,
    val hadithArabic: String?,
    val bookSlug: String,
    val bookName: String,
    val writerName: String,
    val status: String? = "Sahih" // Authenticity indicator
)
