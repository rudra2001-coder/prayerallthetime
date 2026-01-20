package com.rudra.prayerallthetime.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        PrayerRecord::class,
        TasbeehRecord::class,
        TaraweehRecord::class,
        FamilyMemberRecord::class,
        PrayerTimeEntity::class,
        HadithEntity::class,
        HabitEntity::class,
        DuaEntity::class,
        AyahEntity::class
    ],
    version = 9,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun prayerDao(): PrayerDao
    abstract fun hadithDao(): HadithDao
    abstract fun habitDao(): HabitDao
    abstract fun duaDao(): DuaDao
    abstract fun ayahDao(): AyahDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "prayer_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
