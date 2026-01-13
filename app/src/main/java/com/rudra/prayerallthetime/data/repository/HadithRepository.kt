package com.rudra.prayerallthetime.data.repository

import com.rudra.prayerallthetime.data.HadithApiService
import com.rudra.prayerallthetime.data.HadithItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HadithRepository @Inject constructor(
    private val apiService: HadithApiService
) {
    // You'll need to get an API key from hadithapi.com
    private val apiKey = "\$2y\$10\$YOUR_API_KEY_HERE" 

    suspend fun getRandomHadith(): HadithItem? {
        return try {
            val response = apiService.getHadiths(apiKey = apiKey, collection = "sahih-bukhari", page = (1..100).random())
            if (response.status == 200 && response.data.data.isNotEmpty()) {
                response.data.data.random()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
