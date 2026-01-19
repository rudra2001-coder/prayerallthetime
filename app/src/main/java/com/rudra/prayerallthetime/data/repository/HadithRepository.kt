package com.rudra.prayerallthetime.data.repository

import com.rudra.prayerallthetime.data.BookInfo
import com.rudra.prayerallthetime.data.HadithApiService
import com.rudra.prayerallthetime.data.HadithItem
import com.rudra.prayerallthetime.data.HadithRetrofitInstance
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HadithRepository @Inject constructor() {

    private val api: HadithApiService = HadithRetrofitInstance.api
    private val apiKey = "\$2y\$10\$L7YfU0N0fW7G/nN98Xh9ieGj/g0k6m0Z0X0X0X0X0X0X0X0X0X0X0" // Placeholder or from BuildConfig

    suspend fun getRandomHadith(): HadithItem? {
        return try {
            val response = api.getHadiths(apiKey = apiKey, collection = "sahih-bukhari", page = (1..100).random())
            if (response.status == 200 && response.data.data.isNotEmpty()) {
                response.data.data.random()
            } else {
                getFallbackHadith()
            }
        } catch (e: Exception) {
            getFallbackHadith()
        }
    }

    private fun getFallbackHadith(): HadithItem {
        return HadithItem(
            id = 1,
            hadithNumber = "1",
            englishNarrator = "Umar bin Al-Khattab",
            hadithEnglish = "Actions are but by intentions and every man shall have only which he intended.",
            hadithArabic = "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ",
            bookSlug = "sahih-bukhari",
            volume = "1",
            book = BookInfo(bookName = "Sahih Bukhari", writerName = "Imam Bukhari")
        )
    }
}
