package com.rudra.prayerallthetime.data.repository

import android.location.Location
import com.rudra.prayerallthetime.data.model.Mosque
import com.rudra.prayerallthetime.data.remote.PlacesApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MosqueRepository @Inject constructor(
    private val placesApiService: PlacesApiService
) {
    suspend fun getNearbyMosques(lat: Double, lon: Double, radius: Int = 5000): List<Mosque> {
        val query = """
            [out:json];
            node["amenity"="place_of_worship"]["religion"="muslim"](around:$radius,$lat,$lon);
            out body;
        """.trimIndent()

        return try {
            val response = placesApiService.getNearbyMosques(query)
            response.elements.map { element ->
                val mosqueLoc = Location("").apply {
                    latitude = element.lat
                    longitude = element.lon
                }
                val userLoc = Location("").apply {
                    latitude = lat
                    longitude = lon
                }
                
                Mosque(
                    name = element.tags?.name ?: "Unnamed Mosque",
                    address = "Nearby, Gurgaon", // Overpass doesn't always provide full address in body
                    latitude = element.lat,
                    longitude = element.lon,
                    distance = userLoc.distanceTo(mosqueLoc) / 1000 // In KM
                )
            }.sortedBy { it.distance }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
