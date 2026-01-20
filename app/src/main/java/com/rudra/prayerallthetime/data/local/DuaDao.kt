package com.rudra.prayerallthetime.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DuaDao {
    @Query("SELECT * FROM duas")
    fun getAllDuas(): Flow<List<DuaEntity>>

    @Query("SELECT * FROM duas WHERE category = :category")
    fun getDuasByCategory(category: String): Flow<List<DuaEntity>>

    @Query("SELECT DISTINCT category FROM duas")
    fun getCategories(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDuas(duas: List<DuaEntity>)

    @Query("SELECT COUNT(*) FROM duas")
    suspend fun getDuaCount(): Int
}
