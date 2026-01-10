package com.rudra.prayerallthetime.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasbeeh_records")
data class TasbeehRecord(
    @PrimaryKey
    val date: String, // YYYY-MM-DD
    val totalCount: Int
)
