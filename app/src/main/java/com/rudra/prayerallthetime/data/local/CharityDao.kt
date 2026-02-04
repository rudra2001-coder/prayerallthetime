package com.rudra.prayerallthetime.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CharityDao {
    @Query("SELECT * FROM charity_records ORDER BY date DESC")
    fun getAllCharityRecords(): Flow<List<CharityRecord>>

    @Query("SELECT SUM(amount) FROM charity_records WHERE type = :type")
    fun getTotalAmountByType(type: String): Flow<Double?>

    @Query("SELECT SUM(amount) FROM charity_records")
    fun getTotalCharity(): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharityRecord(record: CharityRecord)

    @Delete
    suspend fun deleteCharityRecord(record: CharityRecord)
}
