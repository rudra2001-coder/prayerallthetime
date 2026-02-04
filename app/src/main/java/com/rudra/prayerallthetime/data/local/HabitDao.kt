package com.rudra.prayerallthetime.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE category = :category")
    fun getHabitsByCategory(category: String): Flow<List<HabitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity)

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    @Query("UPDATE habits SET currentProgress = currentProgress + :increment, lastUpdated = :today WHERE id = :habitId")
    suspend fun incrementProgress(habitId: Int, increment: Int, today: String)

    @Query("UPDATE habits SET currentProgress = 0 WHERE lastUpdated != :today")
    suspend fun resetDailyHabits(today: String)
}
