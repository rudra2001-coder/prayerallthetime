package com.rudra.prayerallthetime.core.config

import androidx.compose.ui.graphics.Color

enum class CalculationMethod(
    val displayName: String,
    val fajrAngle: Double,
    val ishaAngle: Double,
    val ishaInterval: Int = 0, // minutes after maghrib if angle is 0
    val color: Color = Color(0xFF1A237E)
) {
    MUSLIM_WORLD_LEAGUE("Muslim World League", 18.0, 17.0, color = Color(0xFF1A237E)),
    ISNA("ISNA (North America)", 15.0, 15.0, color = Color(0xFF283593)),
    EGYPTIAN("Egyptian", 19.5, 17.5, color = Color(0xFF3F51B5)),
    KARACHI("Karachi (Pakistan)", 18.0, 18.0, color = Color(0xFF5C6BC0)),
    UMM_AL_QURA("Umm Al-Qura", 18.5, 0.0, 90, color = Color(0xFF7986CB)),
    DUBAI("Dubai", 18.2, 18.2, color = Color(0xFF9FA8DA)),
    MOONSIGHTING_COMMITTEE("Moonsighting Committee", 18.0, 18.0, color = Color(0xFFC5CAE9)),
    NORTH_AMERICA("North America", 15.0, 15.0, color = Color(0xFFE8EAF6)),
    KUWAIT("Kuwait", 18.0, 17.5, color = Color(0xFF0D47A1)),
    QATAR("Qatar", 18.0, 0.0, 90, color = Color(0xFF1565C0)),
    SINGAPORE("Singapore", 20.0, 18.0, color = Color(0xFF1976D2)),
    TEHRAN("Tehran", 17.7, 14.0, color = Color(0xFF1E88E5)),
    TURKEY("Turkey", 18.0, 17.0, color = Color(0xFF2196F3)),
    BANGLADESH("Bangladesh", 18.0, 17.0, color = Color(0xFF42A5F5));
    
    companion object {
        fun fromString(value: String): CalculationMethod {
            return entries.find { it.name == value } ?: MUSLIM_WORLD_LEAGUE
        }
        
        fun getAllMethods(): List<CalculationMethod> = entries.toList()
    }
}

enum class Madhab(val displayName: String, val shadowFactor: Int) {
    SHAFI("Shafi", 1),
    HANAFI("Hanafi", 2)
}

enum class HighLatitudeRule(val displayName: String) {
    NONE("None"),
    MIDDLE_OF_NIGHT("Middle of Night"),
    SEVENTH_OF_NIGHT("Seventh of Night"),
    TWILIGHT_ANGLE("Twilight Angle")
}
