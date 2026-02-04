package com.rudra.prayerallthetime.data.repository

import com.rudra.prayerallthetime.data.local.HabitDao
import com.rudra.prayerallthetime.data.local.HabitEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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

    suspend fun addHabit(habit: HabitEntity) {
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

    suspend fun seedCoreHabitsIfEmpty() {
        val count = habitDao.getAllHabits().first().size
        if (count == 0) {
            val today = LocalDate.now().format(dateFormatter)
            val coreHabits = listOf(
                HabitEntity(
                    title = "Five Daily Prayers",
                    description = "Perform all 5 obligatory prayers on time.",
                    motivation = "Prayer is the pillar of religion and the first thing questioned on the Day of Judgment.",
                    goalValue = 5,
                    unit = "prayers",
                    category = "Spiritual",
                    iconEmoji = "🕌",
                    lastUpdated = today
                ),
                HabitEntity(
                    title = "Speak Only Truth",
                    description = "Commit to absolute honesty in every conversation today.",
                    motivation = "Truthfulness leads to righteousness, and righteousness leads to Paradise.",
                    goalValue = 1,
                    unit = "day",
                    category = "Personality",
                    iconEmoji = "🗣️",
                    lastUpdated = today
                ),
                HabitEntity(
                    title = "Avoid Harmful Speech",
                    description = "Refrain from backbiting, lying, or using foul language.",
                    motivation = "Whoever believes in Allah and the Last Day should speak good or remain silent.",
                    goalValue = 1,
                    unit = "day",
                    category = "Protection",
                    iconEmoji = "🛡️",
                    lastUpdated = today
                ),
                HabitEntity(
                    title = "Religious Learning",
                    description = "Spend time reading Quran or a religious book.",
                    motivation = "Seeking knowledge is an obligation upon every Muslim.",
                    goalValue = 15,
                    unit = "min",
                    category = "Spiritual",
                    iconEmoji = "📚",
                    lastUpdated = today
                ),
                HabitEntity(
                    title = "Control Anger",
                    description = "Stay calm and patient even in difficult situations.",
                    motivation = "The strong man is not the one who can overpower others, but the one who controls himself in anger.",
                    goalValue = 1,
                    unit = "day",
                    category = "Personality",
                    iconEmoji = "🧘",
                    lastUpdated = today
                )
            )
            coreHabits.forEach { habitDao.insertHabit(it) }
        }
    }
}
