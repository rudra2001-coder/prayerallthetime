package com.rudra.prayerallthetime.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {
    @GET("interpreter")
    suspend fun getNearbyMosques(
        @Query("data") query: String
    ): OverpassResponse
}

data class OverpassResponse(
    val elements: List<Element>
)

data class Element(
    val lat: Double,
    val lon: Double,
    val tags: Tags?
)

data class Tags(
    val name: String?,
    val amenity: String?,
    val religion: String?
)
