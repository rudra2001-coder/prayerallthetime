package com.rudra.prayerallthetime.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val goalValue: Int, // e.g., 10 minutes, 1 surah, 100 tasbeeh
    val unit: String, // e.g., "min", "page", "times"
    val category: String, // e.g., "Quran", "Dhikr", "Tahajjud"
    val frequency: String = "Daily",
    var currentProgress: Int = 0,
    val lastUpdated: String // YYYY-MM-DD to reset progress daily if needed
)
