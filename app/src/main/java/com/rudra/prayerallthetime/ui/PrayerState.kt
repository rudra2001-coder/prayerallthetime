package com.rudra.prayerallthetime.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.rudra.prayerallthetime.data.Prayer

sealed class PrayerState {
    object Loading : PrayerState()
    data class Success(val data: PrayerData) : PrayerState()
    data class Error(val message: String) : PrayerState()
}

data class PrayerData(
    val nextPrayer: PrayerDetails,
    val countdown: String,
    val sunrise: String,
    val hijriDate: String,
    val gregorianDate: String,
    val city: String,
    val qiblaDirection: String,
    val prayerProgress: Float
)

data class PrayerDetails(
    val name: String,
    val arabicName: String,
    val time: String
)

enum class TimeRange(val displayName: String) {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly")
}

enum class TimePeriod(val displayName: String) {
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    YEARLY("Yearly")
}

data class Badge(
    val id: Int,
    val title: String,
    val description: String,
    val color: Color,
    val icon: String? = null,
    val iconImage: ImageVector? = null,
    val dateEarned: String = "",
    val requirement: String = ""
)

data class StreakData(
    val id: Int,
    val name: String,
    val description: String,
    val days: Int,
    val color: Color,
    val icon: ImageVector? = null,
    val nextMilestone: Int = 30
)

data class DayData(
    val dayName: String,
    val dayAbbr: String,
    val completedPrayers: Int,
    val completionRate: Float,
    val isToday: Boolean = false
)

data class MonthData(
    val monthName: String,
    val totalPrayers: Int,
    val averageCompletion: Float,
    val changePercentage: Int,
    val trend: Trend
)

enum class Trend {
    UP,
    DOWN,
    STABLE
}

data class ComparisonData(
    val weeklyComparison: Int,
    val monthlyComparison: Int
)
