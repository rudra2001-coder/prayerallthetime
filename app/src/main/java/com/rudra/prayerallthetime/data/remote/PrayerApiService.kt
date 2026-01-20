package com.rudra.prayerallthetime.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PrayerApiService {
    @GET("timings/{date}")
    suspend fun getPrayerTimes(
        @Path("date") date: String, // dd-MM-yyyy
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("method") method: Int? = null
    ): PrayerApiResponse
}

data class PrayerApiResponse(
    val code: Int,
    val status: String,
    val data: PrayerData
)

data class PrayerData(
    val timings: Timings,
    val date: DateInfo,
    val meta: Meta
)

data class Timings(
    val Fajr: String,
    val Sunrise: String,
    val Dhuhr: String,
    val Asr: String,
    val Maghrib: String,
    val Isha: String
)

data class DateInfo(
    val readable: String,
    val timestamp: String,
    val hijri: HijriInfo
)

data class HijriInfo(
    val date: String,
    val month: HijriMonth
)

data class HijriMonth(
    val en: String,
    val ar: String
)

data class Meta(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val method: MethodInfo
)

data class MethodInfo(
    val id: Int,
    val name: String
)
