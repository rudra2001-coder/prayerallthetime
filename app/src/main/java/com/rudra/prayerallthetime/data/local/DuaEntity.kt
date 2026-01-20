package com.rudra.prayerallthetime.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "duas")
data class DuaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val arabicText: String,
    val transliteration: String?,
    val translation: String,
    val reference: String?,
    val category: String // e.g., "Morning/Evening", "Anxiety", "Protection"
)
