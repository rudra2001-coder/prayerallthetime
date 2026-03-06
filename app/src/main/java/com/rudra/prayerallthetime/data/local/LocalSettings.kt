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
    
    // Prayer calculation method
    private val PRAYER_CALCULATION_METHOD = stringPreferencesKey("prayer_calculation_method")
    
    // Dark mode settings
    private val DARK_MODE = booleanPreferencesKey("dark_mode")
    private val AUTO_DARK_MODE = booleanPreferencesKey("auto_dark_mode")
    
    // Cached prayer times for offline mode
    private val CACHED_PRAYER_TIMES = stringPreferencesKey("cached_prayer_times")
    private val CACHED_PRAYER_DATE = stringPreferencesKey("cached_prayer_date")
    
    // Quran reading settings
    private val QURAN_FONT_SIZE = floatPreferencesKey("quran_font_size")
    private val SHOW_TRANSLATION = booleanPreferencesKey("show_translation")
    private val SHOW_ARABIC = booleanPreferencesKey("show_arabic")
    private val LAST_READ_SURAH = intPreferencesKey("last_read_surah")
    private val LAST_READ_AYAH = intPreferencesKey("last_read_ayah")
    
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
    
    // Prayer calculation method (MWL, ISNA, Umm Al-Qura, etc.)
    val prayerCalculationMethod: Flow<String> = context.dataStore.data.map { 
        it[PRAYER_CALCULATION_METHOD] ?: "MWL" 
    }
    
    // Dark mode settings
    val darkMode: Flow<Boolean> = context.dataStore.data.map { it[DARK_MODE] ?: true }
    val autoDarkMode: Flow<Boolean> = context.dataStore.data.map { it[AUTO_DARK_MODE] ?: true }
    
    // Quran reading settings
    val quranFontSize: Flow<Float> = context.dataStore.data.map { it[QURAN_FONT_SIZE] ?: 24f }
    val showTranslation: Flow<Boolean> = context.dataStore.data.map { it[SHOW_TRANSLATION] ?: true }
    val showArabic: Flow<Boolean> = context.dataStore.data.map { it[SHOW_ARABIC] ?: true }
    val lastReadSurah: Flow<Int> = context.dataStore.data.map { it[LAST_READ_SURAH] ?: 1 }
    val lastReadAyah: Flow<Int> = context.dataStore.data.map { it[LAST_READ_AYAH] ?: 1 }
    
    // Cached prayer times
    val cachedPrayerTimes: Flow<String?> = context.dataStore.data.map { it[CACHED_PRAYER_TIMES] }
    val cachedPrayerDate: Flow<String?> = context.dataStore.data.map { it[CACHED_PRAYER_DATE] }
    
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
    
    suspend fun setPrayerCalculationMethod(method: String) {
        context.dataStore.edit { it[PRAYER_CALCULATION_METHOD] = method }
    }
    
    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[DARK_MODE] = enabled }
    }
    
    suspend fun setAutoDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[AUTO_DARK_MODE] = enabled }
    }
    
    suspend fun setQuranFontSize(size: Float) {
        context.dataStore.edit { it[QURAN_FONT_SIZE] = size }
    }
    
    suspend fun setShowTranslation(show: Boolean) {
        context.dataStore.edit { it[SHOW_TRANSLATION] = show }
    }
    
    suspend fun setShowArabic(show: Boolean) {
        context.dataStore.edit { it[SHOW_ARABIC] = show }
    }
    
    suspend fun updateLastReadPosition(surah: Int, ayah: Int) {
        context.dataStore.edit { preferences ->
            preferences[LAST_READ_SURAH] = surah
            preferences[LAST_READ_AYAH] = ayah
        }
    }
    
    suspend fun cachePrayerTimes(timesJson: String, date: String) {
        context.dataStore.edit { preferences ->
            preferences[CACHED_PRAYER_TIMES] = timesJson
            preferences[CACHED_PRAYER_DATE] = date
        }
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

    suspend fun clearAllData() {
        context.dataStore.edit { it.clear() }
    }
}
