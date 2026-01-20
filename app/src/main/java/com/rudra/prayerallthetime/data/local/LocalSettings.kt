package com.rudra.prayerallthetime.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "settings")

@Singleton
class LocalSettings @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val LATITUDE = floatPreferencesKey("latitude")
    private val LONGITUDE = floatPreferencesKey("longitude")
    private val CITY_NAME = stringPreferencesKey("city_name")
    private val WUDU_STATUS = booleanPreferencesKey("wudu_status")
    private val TAHAJJUD_TIME = stringPreferencesKey("tahajjud_time")
    private val CURRENT_SURAH = stringPreferencesKey("current_surah")
    
    // New settings for manual vs auto prayer times
    private val USE_MANUAL_PRAYER_TIMES = booleanPreferencesKey("use_manual_prayer_times")
    private val MANUAL_FAJR = stringPreferencesKey("manual_fajr")
    private val MANUAL_DHUHR = stringPreferencesKey("manual_dhuhr")
    private val MANUAL_ASR = stringPreferencesKey("manual_asr")
    private val MANUAL_MAGHRIB = stringPreferencesKey("manual_maghrib")
    private val MANUAL_ISHA = stringPreferencesKey("manual_isha")

    val userLocation: Flow<Pair<Double, Double>?> = context.dataStore.data.map { preferences ->
        val lat = preferences[LATITUDE]?.toDouble()
        val lon = preferences[LONGITUDE]?.toDouble()
        if (lat != null && lon != null) lat to lon else null
    }

    val cityName: Flow<String?> = context.dataStore.data.map { it[CITY_NAME] }
    
    val wuduStatus: Flow<Boolean> = context.dataStore.data.map { it[WUDU_STATUS] ?: false }
    
    val tahajjudTime: Flow<String> = context.dataStore.data.map { it[TAHAJJUD_TIME] ?: "03:45 AM" }
    
    val currentSurah: Flow<String> = context.dataStore.data.map { it[CURRENT_SURAH] ?: "Al-Fatihah" }

    val useManualPrayerTimes: Flow<Boolean> = context.dataStore.data.map { it[USE_MANUAL_PRAYER_TIMES] ?: false }
    
    val manualFajr: Flow<String> = context.dataStore.data.map { it[MANUAL_FAJR] ?: "05:00 AM" }
    val manualDhuhr: Flow<String> = context.dataStore.data.map { it[MANUAL_DHUHR] ?: "12:30 PM" }
    val manualAsr: Flow<String> = context.dataStore.data.map { it[MANUAL_ASR] ?: "04:30 PM" }
    val manualMaghrib: Flow<String> = context.dataStore.data.map { it[MANUAL_MAGHRIB] ?: "06:15 PM" }
    val manualIsha: Flow<String> = context.dataStore.data.map { it[MANUAL_ISHA] ?: "08:00 PM" }

    suspend fun saveLocation(lat: Double, lon: Double, city: String) {
        context.dataStore.edit { preferences ->
            preferences[LATITUDE] = lat.toFloat()
            preferences[LONGITUDE] = lon.toFloat()
            preferences[CITY_NAME] = city
        }
    }

    suspend fun updateWuduStatus(status: Boolean) {
        context.dataStore.edit { it[WUDU_STATUS] = status }
    }

    suspend fun updateTahajjudTime(time: String) {
        context.dataStore.edit { it[TAHAJJUD_TIME] = time }
    }

    suspend fun updateCurrentSurah(surah: String) {
        context.dataStore.edit { it[CURRENT_SURAH] = surah }
    }

    suspend fun setUseManualPrayerTimes(use: Boolean) {
        context.dataStore.edit { it[USE_MANUAL_PRAYER_TIMES] = use }
    }

    suspend fun updateManualPrayerTime(prayerName: String, time: String) {
        context.dataStore.edit { preferences ->
            when (prayerName.lowercase()) {
                "fajr" -> preferences[MANUAL_FAJR] = time
                "dhuhr" -> preferences[MANUAL_DHUHR] = time
                "asr" -> preferences[MANUAL_ASR] = time
                "maghrib" -> preferences[MANUAL_MAGHRIB] = time
                "isha" -> preferences[MANUAL_ISHA] = time
            }
        }
    }
}
