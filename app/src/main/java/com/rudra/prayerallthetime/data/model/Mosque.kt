package com.rudra.prayerallthetime.data.model

data class Mosque(
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val distance: Float = 0f
)
