package com.rudra.prayerallthetime.data.repository

import com.rudra.prayerallthetime.data.BookInfo
import com.rudra.prayerallthetime.data.HadithApiService
import com.rudra.prayerallthetime.data.HadithItem
import com.rudra.prayerallthetime.data.HadithRetrofitInstance
import com.rudra.prayerallthetime.data.local.HadithDao
import com.rudra.prayerallthetime.data.local.HadithEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HadithRepository @Inject constructor(
    private val hadithDao: HadithDao
) {

    private val api: HadithApiService = HadithRetrofitInstance.api
    private val apiKey = "\$2y\$10\$L7YfU0N0fW7G/nN98Xh9ieGj/g0k6m0Z0X0X0X0X0X0X0X0X0X0X0"

    suspend fun getRandomHadith(): HadithEntity? {
        // 1. Try local DB first (Offline-first)
        val localHadith = hadithDao.getRandomHadith()
        if (localHadith != null) return localHadith

        // 2. If empty, fetch from remote and cache
        return try {
            val response = api.getHadiths(apiKey = apiKey, collection = "sahih-bukhari", page = (1..100).random())
            if (response.status == 200 && response.data.data.isNotEmpty()) {
                val remoteHadith = response.data.data.random()
                val entity = remoteHadith.toEntity()
                hadithDao.insertHadith(entity)
                entity
            } else {
                getFallbackHadith()
            }
        } catch (e: Exception) {
            getFallbackHadith()
        }
    }

    fun searchHadiths(query: String): Flow<List<HadithEntity>> {
        return hadithDao.searchHadiths("%$query%")
    }

    private fun getFallbackHadith(): HadithEntity {
        return HadithEntity(
            id = 1,
            hadithNumber = "1",
            englishNarrator = "Umar bin Al-Khattab",
            hadithEnglish = "Actions are but by intentions and every man shall have only which he intended.",
            hadithArabic = "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ",
            bookSlug = "sahih-bukhari",
            bookName = "Sahih Bukhari",
            writerName = "Imam Bukhari",
            status = "Sahih"
        )
    }

    private fun HadithItem.toEntity(): HadithEntity {
        return HadithEntity(
            id = this.id,
            hadithNumber = this.hadithNumber,
            englishNarrator = this.englishNarrator,
            hadithEnglish = this.hadithEnglish,
            hadithArabic = this.hadithArabic,
            bookSlug = this.bookSlug,
            bookName = this.book.bookName,
            writerName = this.book.writerName,
            status = "Sahih" // Default status from trusted collections
        )
    }
}
