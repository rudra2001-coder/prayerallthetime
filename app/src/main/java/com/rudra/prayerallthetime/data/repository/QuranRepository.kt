package com.rudra.prayerallthetime.data.repository

import com.rudra.prayerallthetime.data.local.AyahDao
import com.rudra.prayerallthetime.data.local.AyahEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuranRepository @Inject constructor(
    private val ayahDao: AyahDao
) {
    fun getAyahsForSurah(surahId: Int): Flow<List<AyahEntity>> = ayahDao.getAyahsForSurah(surahId)

    fun getBookmarkedAyahs(): Flow<List<AyahEntity>> = ayahDao.getBookmarkedAyahs()

    suspend fun getRandomAyah(): AyahEntity? = ayahDao.getRandomAyah()

    suspend fun toggleBookmark(ayah: AyahEntity) {
        ayahDao.updateAyah(ayah.copy(isBookmarked = !ayah.isBookmarked))
    }

    suspend fun searchQuran(query: String): List<AyahEntity> = ayahDao.searchQuran(query)

    suspend fun getAyahCount(): Int = ayahDao.getAyahCount()

    suspend fun insertAyahs(ayahs: List<AyahEntity>) {
        ayahDao.insertAyahs(ayahs)
    }
}
