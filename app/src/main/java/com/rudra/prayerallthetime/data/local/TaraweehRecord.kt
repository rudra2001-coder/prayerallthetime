package com.rudra.prayerallthetime.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taraweeh_records")
data class TaraweehRecord(
    @PrimaryKey
    val date: String, // YYYY-MM-DD
    val rakatCount: Int
)
