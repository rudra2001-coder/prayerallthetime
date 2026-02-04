package com.rudra.prayerallthetime.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DuaDao {
    @Query("SELECT * FROM duas ORDER BY dateAdded DESC")
    fun getAllDuas(): Flow<List<DuaEntity>>

    @Query("SELECT * FROM duas WHERE category = :category ORDER BY dateAdded DESC")
    fun getDuasByCategory(category: String): Flow<List<DuaEntity>>

    @Query("SELECT DISTINCT category FROM duas")
    fun getCategories(): Flow<List<String>>

    @Query("SELECT * FROM duas WHERE isFavorite = 1")
    fun getFavoriteDuas(): Flow<List<DuaEntity>>

    @Query("UPDATE duas SET isFavorite = :isFavorite WHERE id = :duaId")
    suspend fun updateFavoriteStatus(duaId: Int, isFavorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDuas(duas: List<DuaEntity>)

    @Query("SELECT COUNT(*) FROM duas")
    suspend fun getDuaCount(): Int

    @Query("SELECT * FROM duas WHERE id = :id LIMIT 1")
    suspend fun getDuaById(id: Int): DuaEntity?
}
