package com.rudra.prayerallthetime.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "charity_records")
data class CharityRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val type: String, // "Sadaqah", "Zakat", "Fitrana"
    val description: String?,
    val date: Long = System.currentTimeMillis()
)
