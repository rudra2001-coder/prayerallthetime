package com.rudra.prayerallthetime.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HadithDao {
    @Query("SELECT * FROM hadiths")
    fun getAllHadiths(): Flow<List<HadithEntity>>

    @Query("SELECT * FROM hadiths ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomHadith(): HadithEntity?

    @Query("SELECT * FROM hadiths WHERE hadithEnglish LIKE :query OR hadithArabic LIKE :query")
    fun searchHadiths(query: String): Flow<List<HadithEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHadith(hadith: HadithEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHadiths(hadiths: List<HadithEntity>)

    @Query("SELECT COUNT(*) FROM hadiths")
    suspend fun getHadithCount(): Int
}
