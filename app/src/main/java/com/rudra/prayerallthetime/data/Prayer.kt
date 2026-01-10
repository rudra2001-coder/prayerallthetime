package com.rudra.prayerallthetime.data

data class Prayer(
    val name: String,
    val time: String,
    var isPrayed: Boolean = false,
    val emoji: String = "🕌"
)
