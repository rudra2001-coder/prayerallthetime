package com.rudra.prayerallthetime.data.repository

import com.rudra.prayerallthetime.data.local.DuaDao
import com.rudra.prayerallthetime.data.local.DuaEntity
import com.rudra.prayerallthetime.data.remote.DuaApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DuaRepository @Inject constructor(
    private val duaDao: DuaDao,
    private val apiService: DuaApiService
) {
    fun getAllDuas(): Flow<List<DuaEntity>> = duaDao.getAllDuas()

    fun getDuasByCategory(category: String): Flow<List<DuaEntity>> = 
        duaDao.getDuasByCategory(category)

    fun getCategories(): Flow<List<String>> = duaDao.getCategories()

    fun getFavoriteDuas(): Flow<List<DuaEntity>> = duaDao.getFavoriteDuas()

    suspend fun toggleFavorite(duaId: Int, isFavorite: Boolean) {
        duaDao.updateFavoriteStatus(duaId, isFavorite)
    }

    suspend fun fetchRemoteDuas(page: Int = 1) {
        try {
            val response = apiService.getDuas(page = page)
            if (response.code == 200) {
                val entities = response.data.map { remote ->
                    DuaEntity(
                        title = remote.title,
                        arabicText = remote.arabic,
                        transliteration = remote.transliteration,
                        translation = remote.translation,
                        translationBn = remote.translationBn,
                        reference = remote.reference,
                        category = remote.category
                    )
                }
                duaDao.insertDuas(entities)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun preloadDuasIfEmpty() {
        if (duaDao.getDuaCount() == 0) {
            val initialDuas = listOf(
                DuaEntity(
                    title = "Before Eating",
                    arabicText = "بِسْمِ اللَّهِ",
                    transliteration = "Bismillah",
                    translation = "In the name of Allah.",
                    translationBn = "আল্লাহর নামে শুরু করছি।",
                    reference = "Abu Dawud, At-Tirmidhi",
                    category = "Daily Life"
                ),
                DuaEntity(
                    title = "After Waking Up",
                    arabicText = "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ",
                    transliteration = "Alhamdu lillahil-ladhi ahyana ba'da ma amatana wa ilayhin-nushur",
                    translation = "Praise is to Allah Who gives us life after He has caused us to die and to Him is the return.",
                    translationBn = "সমস্ত প্রশংসা আল্লাহর জন্য, যিনি আমাদের মৃত্যুর পর জীবন দান করেছেন এবং তাঁরই কাছে পুনরুত্থান।",
                    reference = "Sahih al-Bukhari",
                    category = "Morning/Evening"
                )
            )
            duaDao.insertDuas(initialDuas)
        }
    }
}
