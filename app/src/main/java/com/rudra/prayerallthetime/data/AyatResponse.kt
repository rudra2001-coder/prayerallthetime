package com.rudra.prayerallthetime.data

data class AyatResponse(
    val code: Int,
    val status: String,
    val data: AyatData
)

data class AyatData(
    val text: String,
    val edition: Edition,
    val surah: Surah,
    val numberInSurah: Int
)

data class Edition(
    val identifier: String,
    val language: String,
    val name: String,
    val englishName: String,
    val format: String,
    val type: String
)

data class Surah(
    val number: Int,
    val name: String,
    val englishName: String,
    val englishNameTranslation: String,
    val numberOfAyahs: Int,
    val revelationType: String
)
