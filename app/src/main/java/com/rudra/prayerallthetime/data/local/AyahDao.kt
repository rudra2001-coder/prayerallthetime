package com.rudra.prayerallthetime.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AyahDao {
    @Query("SELECT * FROM quran_ayats WHERE surah = :surahId ORDER BY ayah ASC")
    fun getAyahsForSurah(surahId: Int): Flow<List<AyahEntity>>

    @Query("SELECT * FROM quran_ayats WHERE isBookmarked = 1")
    fun getBookmarkedAyahs(): Flow<List<AyahEntity>>

    @Query("SELECT * FROM quran_ayats WHERE text LIKE '%' || :query || '%' OR translation LIKE '%' || :query || '%' OR translationBn LIKE '%' || :query || '%'")
    suspend fun searchQuran(query: String): List<AyahEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAyahs(ayahs: List<AyahEntity>)

    @Update
    suspend fun updateAyah(ayah: AyahEntity)

    @Query("SELECT * FROM quran_ayats ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomAyah(): AyahEntity?

    @Query("SELECT COUNT(*) FROM quran_ayats")
    suspend fun getAyahCount(): Int

    @Query("SELECT * FROM quran_ayats WHERE number = :number LIMIT 1")
    suspend fun getAyahByNumber(number: Int): AyahEntity?
}
