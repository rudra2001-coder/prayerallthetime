package com.rudra.prayerallthetime.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HadithApiService {
    @GET("hadiths")
    suspend fun getHadiths(
        @Query("apiKey") apiKey: String,
        @Query("collection") collection: String? = null,
        @Query("book") book: String? = null,
        @Query("page") page: Int = 1
    ): HadithResponse
}

data class HadithResponse(
    val status: Int,
    val message: String,
    val data: HadithData
)

data class HadithData(
    val current_page: Int,
    val data: List<HadithItem>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val next_page_url: String?,
    val path: String,
    val per_page: Int,
    val prev_page_url: String?,
    val to: Int,
    val total: Int
)

data class HadithItem(
    val id: Int,
    val hadithNumber: String,
    val englishNarrator: String?,
    val hadithEnglish: String?,
    val hadithArabic: String?,
    val bookSlug: String,
    val volume: String?,
    val book: BookInfo
)

data class BookInfo(
    val bookName: String,
    val writerName: String
)

object HadithRetrofitInstance {
    private const val BASE_URL = "https://hadithapi.com/api/"

    val api: HadithApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HadithApiService::class.java)
    }
}
