package com.rudra.prayerallthetime.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val motivation: String = "",
    val goalValue: Int, 
    val unit: String, 
    val category: String, // "Spiritual", "Personality", "Protection"
    val iconEmoji: String = "✨",
    var currentProgress: Int = 0,
    val lastUpdated: String,
    val streak: Int = 0
)
