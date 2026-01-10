package com.rudra.prayerallthetime.data

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

// 1. Smart Prayer Reminders
enum class ReminderType {
    ADHAN_TIME, PREP_TIME, QIBLA_TIME, TAHARA, SUNNAH_RAKAAT
}

// 3. Islamic Calendar Integration
data class IslamicMonth(
    val name: String,
    val days: Int,
    val specialDays: List<SpecialDay>,
    val recommendedDeeds: List<String>
)

data class SpecialDay(
    val date: String,
    val name: String,
    val significance: String,
    val recommendedPrayers: List<String>,
    val duas: List<String>
)

// 4. Tasbih Counter
enum class DhikrType {
    SUBHANALLAH, ALHAMDULILLAH, ALLAHUAKBAR, LA_ILAHA_ILLALLAH, CUSTOM
}

// 5. Prayer Time Visualizations
data class PrayerTime(
    val name: String,
    val time: LocalTime,
    val type: PrayerType
)

// 7. Community Features
data class PrayerGroup(
    val id: String,
    val name: String,
    val members: Int,
    val nextPrayer: LocalDateTime,
    val location: String?,
    val isOnline: Boolean
)

enum class AudioQuality {
    LOW, MEDIUM, HIGH
}

// 8. Learning & Education
enum class PrayerType {
    FAJR, DHUHR, ASR, MAGHRIB, ISHA, EID, JANAZAH, TAHAJJUD
}

enum class Language {
    EN, AR, BN, ID, TR, FR
}

// 9. Personal Prayer Analytics
data class PrayerAnalytics(
    val consistencyScore: Float,
    val averageDelay: Long, // in minutes
    val bestPrayer: String,
    val streak: Int,
    val totalPrayers: Long,
    val improvementAreas: List<String>
)

data class WeeklyReport(
    val weekStarting: LocalDate,
    val dailyCompletion: Map<LocalDate, Float>
)

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean
)

// 10. Smart Athan
enum class AthanStyle {
    TRADITIONAL_EGYPTIAN, MODERN_TURKISH, CLASSICAL_SAUDI, FEMALE_RECITER, INSTRUMENTAL
}

// 11. Family & Group Features
data class FamilyMember(
    val name: String,
    val prayersCompleted: Int,
    val totalPrayers: Int = 5,
    val id: String = java.util.UUID.randomUUID().toString(),
    val role: String = "Member",
    val avatarEmoji: String = "👤"
)

data class FamilyPrayerGroup(
    val familyId: String,
    val members: List<FamilyMember>,
    val prayerSchedule: Map<String, LocalTime>,
    val reminders: List<String>
)

// 12. Context-Aware Notifications
data class SmartNotification(
    val type: String,
    val context: UserContext,
    val priority: Priority,
    val deliveryTime: LocalDateTime
)

enum class UserContext {
    DRIVING, MEETING, SLEEPING, TRAVELING, IN_MOSQUE, NORMAL
}

enum class Priority {
    LOW, MEDIUM, HIGH, URGENT
}

// 13. Offline Mode
data class OfflinePrayerData(
    val city: String,
    val dates: List<LocalDate>,
    val prayerTimes: Map<LocalDate, List<PrayerTime>>,
    val qiblaDirection: Float
)

// 14. Health Integration
data class PrayerHealthMetrics(
    val prayerCalories: Int,
    val focusTime: Long, // in minutes
    val stressReduction: Float,
    val sleepImprovement: Float,
    val postureScore: Float
)

// 15. Mosque Management
data class MosqueAdminDashboard(
    val congregationSize: Int,
    val prayerAttendance: Map<String, Int>,
    val announcements: List<String>
)

// 17. Theme
enum class AppTheme {
    CLASSICAL_OTTOMAN, MODERN_ARABIC, MINIMALIST, RAMADAN_SPECIAL, HAJJ_SEASON
}

// 18. Accessibility
data class AccessibilityFeatures(
    val highContrastMode: Boolean,
    val screenReaderOptimized: Boolean,
    val gestureNavigation: Boolean,
    val audioDescriptions: Boolean
)

// 19. Backend Services Interface
interface PrayerAppServices {
    suspend fun getPrayerTimes(latitude: Double, longitude: Double): List<PrayerTime>
    suspend fun getQiblaDirection(latitude: Double, longitude: Double): Float
    suspend fun getUserAnalytics(userId: String): PrayerAnalytics
    suspend fun getDailyDua(): String
}
