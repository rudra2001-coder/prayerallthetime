package com.rudra.prayerallthetime.core.config

enum class CalculationMethod(
    val fajrAngle: Double,
    val ishaAngle: Double,
    val ishaInterval: Int = 0 // minutes after maghrib if angle is 0
) {
//    MUSLIM_WORLD_LEAGUE(18.0, 17.0),
//    EGYPTIAN(19.5, 17.5),
//    KARACHI(18.0, 18.0),
//    UMM_AL_QURA(18.5, 0, 90),
//    DUBAI(18.2, 18.2),
//    MOONSIGHTING_COMMITTEE(18.0, 18.0),
//    NORTH_AMERICA(15.0, 15.0),
//    KUWAIT(18.0, 17.5),
//    QATAR(18.0, 0, 90),
//    SINGAPORE(20.0, 18.0),
//    TEHRAN(17.7, 14.0),
//    TURKEY(18.0, 17.0),
    BANGLADESH(18.0, 17.0)
}

enum class Madhab(val shadowFactor: Int) {
    SHAFI(1),
    HANAFI(2)
}

enum class HighLatitudeRule {
    NONE,
    MIDDLE_OF_NIGHT,
    SEVENTH_OF_NIGHT,
    TWILIGHT_ANGLE
}
