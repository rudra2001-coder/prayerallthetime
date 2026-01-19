package com.rudra.prayerallthetime.core.prayer

import com.rudra.prayerallthetime.core.config.CalculationMethod
import com.rudra.prayerallthetime.core.config.Madhab
import com.rudra.prayerallthetime.core.utils.MathUtils.dAcos
import com.rudra.prayerallthetime.core.utils.MathUtils.dAcot
import com.rudra.prayerallthetime.core.utils.MathUtils.dAsin
import com.rudra.prayerallthetime.core.utils.MathUtils.dAtan2
import com.rudra.prayerallthetime.core.utils.MathUtils.dCos
import com.rudra.prayerallthetime.core.utils.MathUtils.dSin
import com.rudra.prayerallthetime.core.utils.MathUtils.dTan
import com.rudra.prayerallthetime.core.utils.MathUtils.fixAngle
import com.rudra.prayerallthetime.core.utils.MathUtils.fixHour
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.*

data class PrayerTimes(
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String
)

class PrayerTimesCalculator(
    private val method: CalculationMethod = CalculationMethod.BANGLADESH,
    private val madhab: Madhab = Madhab.SHAFI
) {
    private var lat: Double = 0.0
    private var lng: Double = 0.0
    private var timezone: Double = 0.0
    private var J: Double = 0.0

    fun calculatePrayerTimes(
        date: LocalDate,
        latitude: Double,
        longitude: Double,
        timeZone: Double
    ): PrayerTimes {
        this.lat = latitude
        this.lng = longitude
        this.timezone = timeZone
        this.J = julianDate(date.year, date.monthValue, date.dayOfMonth) - longitude / (15.0 * 24.0)

        val dhuhr = computeDhuhr()
        val sunrise = computeTime(180.0 - hourAngle(0.833), dhuhr)
        val sunset = computeTime(hourAngle(0.833), dhuhr)
        val fajr = computeTime(180.0 - hourAngle(method.fajrAngle), dhuhr)
        val maghrib = sunset // Typically sunset
        
        val isha = if (method.ishaAngle == 0.0) {
            maghrib + method.ishaInterval / 60.0
        } else {
            computeTime(hourAngle(method.ishaAngle), dhuhr)
        }

        val asr = computeAsr(madhab.shadowFactor.toDouble(), dhuhr)

        return PrayerTimes(
            fajr = formatTime(fajr),
            sunrise = formatTime(sunrise),
            dhuhr = formatTime(dhuhr),
            asr = formatTime(asr),
            maghrib = formatTime(maghrib),
            isha = formatTime(isha)
        )
    }

    private fun julianDate(year: Int, month: Int, day: Int): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
    }

    private fun sunPosition(jd: Double): Pair<Double, Double> {
        val d = jd - 2451545.0
        val g = fixAngle(357.529 + 0.98560028 * d)
        val q = fixAngle(280.459 + 0.98564736 * d)
        val l = fixAngle(q + 1.915 * dSin(g) + 0.020 * dSin(2 * g))
        
        val e = 23.439 - 0.00000036 * d
        val ra = dAtan2(dCos(e) * dSin(l), dCos(l)) / 15.0
        val decl = dAsin(dSin(e) * dSin(l))
        val eqt = q / 15.0 - ra
        return Pair(decl, eqt)
    }

    private fun computeDhuhr(): Double {
        val (_, eqt) = sunPosition(J)
        return fixHour(12.0 + timezone - lng / 15.0 - eqt)
    }

    private fun computeTime(angle: Double, dhuhr: Double): Double {
        val (decl, _) = sunPosition(J)
        val v = 1.0 / 15.0 * dAcos((-dSin(angle) - dSin(lat) * dSin(decl)) / (dCos(lat) * dCos(decl)))
        return dhuhr + (if (angle > 90) -v else v)
    }

    private fun computeAsr(step: Double, dhuhr: Double): Double {
        val (decl, _) = sunPosition(J)
        val g = dAcot(step + dTan(abs(lat - decl)))
        return computeTime(g, dhuhr)
    }

    private fun hourAngle(angle: Double): Double {
        val (decl, _) = sunPosition(J)
        val v = dAcos((-dSin(angle) - dSin(lat) * dSin(decl)) / (dCos(lat) * dCos(decl)))
        return v
    }

    private fun formatTime(time: Double): String {
        val h = floor(fixHour(time + 0.5 / 60.0))
        val m = floor((fixHour(time + 0.5 / 60.0) - h) * 60.0)
        val suffix = if (h >= 12) "PM" else "AM"
        val hour12 = if (h > 12) h - 12 else if (h == 0.0) 12.0 else h
        return String.format(Locale.getDefault(), "%02d:%02d %s", hour12.toInt(), m.toInt(), suffix)
    }
}
