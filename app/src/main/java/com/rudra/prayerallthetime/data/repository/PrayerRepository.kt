package com.rudra.prayerallthetime.data.repository

import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.Madhab
import com.batoulapps.adhan.Prayer
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.Qibla
import com.batoulapps.adhan.SunnahTimes
import com.batoulapps.adhan.data.DateComponents
import com.rudra.prayerallthetime.data.local.PrayerDao
import com.rudra.prayerallthetime.data.local.PrayerTimeEntity
import com.rudra.prayerallthetime.data.remote.PrayerApiService
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerData
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerDetails
import com.rudra.prayerallthetime.util.NetworkUtils
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrayerRepository @Inject constructor(
    private val prayerDao: PrayerDao,
    private val prayerApiService: PrayerApiService,
    private val networkUtils: NetworkUtils
) {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val apiDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    private val timeFormat = java.text.SimpleDateFormat("hh:mm a", Locale.getDefault())

    suspend fun getPrayerTimes(latitude: Double, longitude: Double, date: LocalDate): PrayerData {
        val dateStr = date.format(dateFormatter)
        
        // 1. Try Local DB
        val cachedEntity = prayerDao.getPrayerTimesByDate(dateStr)
        val entity = cachedEntity ?: fetchAndCachePrayerTimes(latitude, longitude, date)

        // Always use Adhan library for calculations (countdown, progress, next prayer)
        // to ensure they are calculated based on current system time.
        val coordinates = Coordinates(latitude, longitude)
        val dateComponents = DateComponents.from(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))
        val params = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters
        params.madhab = Madhab.HANAFI
        
        val prayerTimes = PrayerTimes(coordinates, dateComponents, params)
        val qibla = Qibla(coordinates)
        val sunnahTimes = SunnahTimes(prayerTimes)

        val nextPrayerEnum = prayerTimes.nextPrayer()
        val nextPrayerTime = prayerTimes.timeForPrayer(nextPrayerEnum)
        val nextPrayerName = if (nextPrayerEnum != Prayer.NONE) nextPrayerEnum.name else "Fajr"
        
        val allPrayers = listOf(
            PrayerDetails("Fajr", "الفجر", entity?.fajr ?: timeFormat.format(prayerTimes.fajr)),
            PrayerDetails("Sunrise", "الشروق", entity?.sunrise ?: timeFormat.format(prayerTimes.sunrise)),
            PrayerDetails("Dhuhr", "الظهر", entity?.dhuhr ?: timeFormat.format(prayerTimes.dhuhr)),
            PrayerDetails("Asr", "العصر", entity?.asr ?: timeFormat.format(prayerTimes.asr)),
            PrayerDetails("Maghrib", "المغرب", entity?.maghrib ?: timeFormat.format(prayerTimes.maghrib)),
            PrayerDetails("Isha", "العشاء", entity?.isha ?: timeFormat.format(prayerTimes.isha))
        )

        return PrayerData(
            nextPrayer = PrayerDetails(
                name = nextPrayerName,
                arabicName = getArabicName(nextPrayerName),
                time = entity?.let { getPrayerTimeByName(it, nextPrayerName) } ?: (if (nextPrayerTime != null) timeFormat.format(nextPrayerTime) else "--:--")
            ),
            allPrayers = allPrayers,
            countdown = calculateCountdown(nextPrayerTime),
            nextPrayerMillis = nextPrayerTime?.time ?: 0L,
            sunrise = entity?.sunrise ?: timeFormat.format(prayerTimes.sunrise),
            hijriDate = entity?.hijriDate ?: "Calculation Pending", 
            gregorianDate = date.toString(),
            city = entity?.city ?: "Current Location",
            qiblaDirection = "${qibla.direction.toInt()}°",
            prayerProgress = calculateProgress(prayerTimes),
            tahajjudTime = timeFormat.format(sunnahTimes.lastThirdOfTheNight)
        )
    }

    private suspend fun fetchAndCachePrayerTimes(latitude: Double, longitude: Double, date: LocalDate): PrayerTimeEntity? {
        val dateStr = date.format(dateFormatter)
        
        if (networkUtils.isOnline()) {
            try {
                val apiDateStr = date.format(apiDateFormatter)
                val response = prayerApiService.getPrayerTimes(apiDateStr, latitude, longitude)
                if (response.code == 200) {
                    val timings = response.data.timings
                    val entity = PrayerTimeEntity(
                        date = dateStr,
                        fajr = timings.Fajr,
                        sunrise = timings.Sunrise,
                        dhuhr = timings.Dhuhr,
                        asr = timings.Asr,
                        maghrib = timings.Maghrib,
                        isha = timings.Isha,
                        hijriDate = "${response.data.date.hijri.date} ${response.data.date.hijri.month.en}",
                        city = "Auto Detect",
                        latitude = latitude,
                        longitude = longitude,
                        method = response.data.meta.method.id,
                        isFromApi = true
                    )
                    prayerDao.insertPrayerTimes(entity)
                    return entity
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return calculateOffline(latitude, longitude, date)
    }

    private fun calculateOffline(latitude: Double, longitude: Double, date: LocalDate): PrayerTimeEntity {
        val coordinates = Coordinates(latitude, longitude)
        val dateComponents = DateComponents.from(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))
        val params = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters
        params.madhab = Madhab.HANAFI
        
        val prayerTimes = PrayerTimes(coordinates, dateComponents, params)
        
        return PrayerTimeEntity(
            date = date.format(dateFormatter),
            fajr = timeFormat.format(prayerTimes.fajr),
            sunrise = timeFormat.format(prayerTimes.sunrise),
            dhuhr = timeFormat.format(prayerTimes.dhuhr),
            asr = timeFormat.format(prayerTimes.asr),
            maghrib = timeFormat.format(prayerTimes.maghrib),
            isha = timeFormat.format(prayerTimes.isha),
            hijriDate = "Offline Hijri",
            city = "Offline Calc",
            latitude = latitude,
            longitude = longitude,
            isFromApi = false
        )
    }

    private fun getPrayerTimeByName(entity: PrayerTimeEntity, name: String): String {
        return when (name.uppercase()) {
            "FAJR" -> entity.fajr
            "SUNRISE" -> entity.sunrise
            "DHUHR" -> entity.dhuhr
            "ASR" -> entity.asr
            "MAGHRIB" -> entity.maghrib
            "ISHA" -> entity.isha
            else -> "--:--"
        }
    }

    private fun getArabicName(englishName: String): String {
        return when (englishName.uppercase()) {
            "FAJR" -> "الفجر"
            "DHUHR" -> "الظهر"
            "ASR" -> "العصر"
            "MAGHRIB" -> "المغرب"
            "ISHA" -> "العشاء"
            else -> ""
        }
    }

    private fun calculateCountdown(targetDate: Date?): String {
        if (targetDate == null) return "00:00:00"
        val diff = targetDate.time - System.currentTimeMillis()
        if (diff <= 0) return "00:00:00"
        
        val hours = diff / (1000 * 60 * 60)
        val minutes = (diff / (1000 * 60)) % 60
        val seconds = (diff / 1000) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun calculateProgress(times: PrayerTimes): Float {
        val now = Date().time
        val current = times.currentPrayer()
        val next = times.nextPrayer()
        
        val startTime = times.timeForPrayer(current)?.time ?: (now - 3600000)
        val endTime = times.timeForPrayer(next)?.time ?: (now + 3600000)
        
        if (now >= endTime) return 1f
        if (now <= startTime) return 0f
        
        return (now - startTime).toFloat() / (endTime - startTime).toFloat()
    }
}
