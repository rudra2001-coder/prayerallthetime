package com.rudra.prayerallthetime.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerDao {
    // Prayer Records (History/Completion)
    @Query("SELECT * FROM prayer_records WHERE date = :date")
    fun getRecordsForDate(date: String): Flow<List<PrayerRecord>>

    @Query("SELECT * FROM prayer_records WHERE date BETWEEN :startDate AND :endDate")
    fun getRecordsInRange(startDate: String, endDate: String): Flow<List<PrayerRecord>>

    @Query("SELECT * FROM prayer_records WHERE date = :date AND prayerName = :prayerName LIMIT 1")
    suspend fun getRecord(date: String, prayerName: String): PrayerRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: PrayerRecord)

    @Query("SELECT * FROM prayer_records")
    fun getAllRecords(): Flow<List<PrayerRecord>>

    @Query("SELECT COUNT(*) FROM prayer_records WHERE isCompleted = 1")
    fun getTotalCompletedCount(): Flow<Int>

    // Prayer Times Cache
    @Query("SELECT * FROM prayer_times WHERE date = :date LIMIT 1")
    suspend fun getPrayerTimesByDate(date: String): PrayerTimeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayerTimes(prayerTime: PrayerTimeEntity)

    @Query("DELETE FROM prayer_times WHERE date < :date")
    suspend fun deleteOldPrayerTimes(date: String)

    // Tasbeeh Records
    @Query("SELECT * FROM tasbeeh_records WHERE date = :date LIMIT 1")
    suspend fun getTasbeehForDate(date: String): TasbeehRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasbeeh(record: TasbeehRecord)

    @Query("SELECT * FROM tasbeeh_records ORDER BY date DESC")
    fun getAllTasbeehRecords(): Flow<List<TasbeehRecord>>

    // Taraweeh Records
    @Query("SELECT * FROM taraweeh_records WHERE date = :date LIMIT 1")
    suspend fun getTaraweehForDate(date: String): TaraweehRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaraweeh(record: TaraweehRecord)

    @Query("SELECT * FROM taraweeh_records ORDER BY date DESC")
    fun getAllTaraweehRecords(): Flow<List<TaraweehRecord>>

    // Family Member Records
    @Query("SELECT * FROM family_members")
    fun getAllFamilyMembers(): Flow<List<FamilyMemberRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFamilyMember(member: FamilyMemberRecord)

    @Delete
    suspend fun deleteFamilyMember(member: FamilyMemberRecord)
}
