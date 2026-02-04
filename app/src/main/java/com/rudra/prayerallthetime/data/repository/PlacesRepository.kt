package com.rudra.prayerallthetime.data.repository

import com.rudra.prayerallthetime.data.remote.PlacesApiService
import com.rudra.prayerallthetime.data.remote.OverpassResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesRepository @Inject constructor(
    private val apiService: PlacesApiService
) {
    suspend fun getNearbyMosques(lat: Double, lon: Double, radius: Int = 5000): OverpassResponse {
        val query = """
            [out:json];
            node["amenity"="place_of_worship"]["religion"="muslim"](around:$radius,$lat,$lon);
            out body;
        """.trimIndent()
        return apiService.getNearbyMosques(query)
    }
}
