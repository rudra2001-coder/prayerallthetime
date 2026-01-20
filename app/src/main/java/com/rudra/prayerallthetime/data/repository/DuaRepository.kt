package com.rudra.prayerallthetime.data.repository

import com.rudra.prayerallthetime.data.local.DuaDao
import com.rudra.prayerallthetime.data.local.DuaEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DuaRepository @Inject constructor(
    private val duaDao: DuaDao
) {
    fun getAllDuas(): Flow<List<DuaEntity>> = duaDao.getAllDuas()

    fun getDuasByCategory(category: String): Flow<List<DuaEntity>> = 
        duaDao.getDuasByCategory(category)

    fun getCategories(): Flow<List<String>> = duaDao.getCategories()

    suspend fun preloadDuasIfEmpty() {
        if (duaDao.getDuaCount() == 0) {
            val initialDuas = listOf(
                DuaEntity(
                    title = "Before Eating",
                    arabicText = "بِسْمِ اللَّهِ",
                    transliteration = "Bismillah",
                    translation = "In the name of Allah.",
                    reference = "Abu Dawud, At-Tirmidhi",
                    category = "Daily Life"
                ),
                DuaEntity(
                    title = "After Waking Up",
                    arabicText = "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ",
                    transliteration = "Alhamdu lillahil-ladhi ahyana ba'da ma amatana wa ilayhin-nushur",
                    translation = "Praise is to Allah Who gives us life after He has caused us to die and to Him is the return.",
                    reference = "Sahih al-Bukhari",
                    category = "Morning/Evening"
                ),
                DuaEntity(
                    title = "For Protection",
                    arabicText = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ",
                    transliteration = "Bismillahil-ladhi la yadurru ma'as-mihi shay'un fil-ardi wa la fis-sama'i wa Huwas-Sami'ul-Alim",
                    translation = "In the Name of Allah with Whose Name there is protection against every kind of harm in the earth or in the heaven, and He is the All-Hearing and All-Knowing.",
                    reference = "Abu Dawud, At-Tirmidhi",
                    category = "Protection"
                ),
                DuaEntity(
                    title = "Seeking Forgiveness",
                    arabicText = "أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ",
                    transliteration = "Astaghfirullaha wa atubu ilayh",
                    translation = "I seek Allah's forgiveness and I turn to Him in repentance.",
                    reference = "Sahih al-Bukhari",
                    category = "Forgiveness"
                )
            )
            duaDao.insertDuas(initialDuas)
        }
    }
}
