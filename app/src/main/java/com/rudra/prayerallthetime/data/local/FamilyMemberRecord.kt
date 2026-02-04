package com.rudra.prayerallthetime.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "family_members")
data class FamilyMemberRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val relationship: String, // e.g., "Father", "Mother", "Sibling", "Spouse", "Child"
    val completedPrayersToday: Int = 0,
    val totalCompletedPrayers: Int = 0,
    val lastActiveDate: String = "" // YYYY-MM-DD
)
