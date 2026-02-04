package com.rudra.prayerallthetime.data.repository

import com.rudra.prayerallthetime.data.AlQuranApiService
import com.rudra.prayerallthetime.data.SurahSummary
import com.rudra.prayerallthetime.data.local.AyahDao
import com.rudra.prayerallthetime.data.local.AyahEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuranRepository @Inject constructor(
    private val ayahDao: AyahDao,
    private val apiService: AlQuranApiService
) {
    suspend fun getSurahList(): List<SurahSummary> {
        return try {
            val response = apiService.getSurahList()
            if (response.code == 200) response.data else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun fetchAndSaveSurah(surahNumber: Int) {
        try {
            val response = apiService.getSurahDetails(surahNumber)
            if (response.code == 200 && response.data.size >= 3) {
                val uthmani = response.data[0].ayahs
                val bangla = response.data[1].ayahs
                val audio = response.data[2].ayahs
                
                val surahName = response.data[0].name
                
                val ayahs = uthmani.mapIndexed { index, ayahData ->
                    AyahEntity(
                        number = ayahData.number,
                        surah = surahNumber,
                        ayah = index + 1,
                        text = ayahData.text,
                        translationBn = bangla[index].text,
                        audioUrl = audio[index].audio,
                        surahName = surahName
                    )
                }
                ayahDao.insertAyahs(ayahs)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAyahsForSurah(surahId: Int): Flow<List<AyahEntity>> = ayahDao.getAyahsForSurah(surahId)

    fun getBookmarkedAyahs(): Flow<List<AyahEntity>> = ayahDao.getBookmarkedAyahs()

    suspend fun getRandomAyah(): AyahEntity? = ayahDao.getRandomAyah()

    suspend fun toggleBookmark(ayah: AyahEntity) {
        ayahDao.updateAyah(ayah.copy(isBookmarked = !ayah.isBookmarked))
    }

    suspend fun searchQuran(query: String): List<AyahEntity> = ayahDao.searchQuran(query)

    suspend fun getAyahCount(): Int = ayahDao.getAyahCount()

    suspend fun updateAyah(ayah: AyahEntity) {
        ayahDao.updateAyah(ayah)
    }
}
