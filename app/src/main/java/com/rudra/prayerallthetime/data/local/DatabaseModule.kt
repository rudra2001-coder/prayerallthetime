package com.rudra.prayerallthetime.data.local

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun providePrayerDao(database: AppDatabase): PrayerDao {
        return database.prayerDao()
    }

    @Provides
    fun provideHadithDao(database: AppDatabase): HadithDao {
        return database.hadithDao()
    }

    @Provides
    fun provideHabitDao(database: AppDatabase): HabitDao {
        return database.habitDao()
    }

    @Provides
    fun provideDuaDao(database: AppDatabase): DuaDao {
        return database.duaDao()
    }

    @Provides
    fun provideAyahDao(database: AppDatabase): AyahDao {
        return database.ayahDao()
    }

    @Provides
    fun provideCharityDao(database: AppDatabase): CharityDao {
        return database.charityDao()
    }
}
