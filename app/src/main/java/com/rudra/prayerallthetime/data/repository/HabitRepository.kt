package com.rudra.prayerallthetime.data.repository

import com.rudra.prayerallthetime.data.local.HabitDao
import com.rudra.prayerallthetime.data.local.HabitEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepository @Inject constructor(
    private val habitDao: HabitDao
) {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun getAllHabits(): Flow<List<HabitEntity>> = habitDao.getAllHabits()

    suspend fun addHabit(title: String, goalValue: Int, unit: String, category: String) {
        val today = LocalDate.now().format(dateFormatter)
        val habit = HabitEntity(
            title = title,
            goalValue = goalValue,
            unit = unit,
            category = category,
            lastUpdated = today
        )
        habitDao.insertHabit(habit)
    }

    suspend fun incrementProgress(habitId: Int, increment: Int = 1) {
        val today = LocalDate.now().format(dateFormatter)
        habitDao.incrementProgress(habitId, increment, today)
    }

    suspend fun deleteHabit(habit: HabitEntity) {
        habitDao.deleteHabit(habit)
    }

    suspend fun resetDailyHabitsIfNecessary() {
        val today = LocalDate.now().format(dateFormatter)
        habitDao.resetDailyHabits(today)
    }
}
