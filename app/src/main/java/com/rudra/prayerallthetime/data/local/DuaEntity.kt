package com.rudra.prayerallthetime.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "duas")
data class DuaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val arabicText: String,
    val transliteration: String? = null,
    val translation: String,
    val translationBn: String? = null,
    val reference: String? = null,
    val category: String,
    val isFavorite: Boolean = false,
    val isDownloaded: Boolean = false,
    val dateAdded: Long = System.currentTimeMillis()
)
