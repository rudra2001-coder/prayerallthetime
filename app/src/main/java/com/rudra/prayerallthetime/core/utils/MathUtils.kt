package com.rudra.prayerallthetime.core.utils

import kotlin.math.*

object MathUtils {
    fun dSin(d: Double): Double = sin(Math.toRadians(d))
    fun dCos(d: Double): Double = cos(Math.toRadians(d))
    fun dTan(d: Double): Double = tan(Math.toRadians(d))
    fun dAsin(x: Double): Double = Math.toDegrees(asin(x))
    fun dAcos(x: Double): Double = Math.toDegrees(acos(x))
    fun dAtan(x: Double): Double = Math.toDegrees(atan(x))
    fun dAtan2(y: Double, x: Double): Double = Math.toDegrees(atan2(y, x))
    fun dAcot(x: Double): Double = Math.toDegrees(atan(1.0 / x))

    fun fixAngle(a: Double): Double {
        var ang = a - 360.0 * floor(a / 360.0)
        if (ang < 0) ang += 360.0
        return ang
    }

    fun fixHour(a: Double): Double {
        var ang = a - 24.0 * floor(a / 24.0)
        if (ang < 0) ang += 24.0
        return ang
    }
}
