package com.rudra.prayerallthetime.data

data class FamilyMember(
    val name: String,
    val prayersCompleted: Int,
    val totalPrayers: Int = 5
)
