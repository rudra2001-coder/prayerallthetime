package com.rudra.prayerallthetime.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface AlQuranApiService {
    @GET("ayah/{ayah}/editions/quran-uthmani,en.sahih")
    suspend fun getAyatOfTheDay(@Path("ayah") ayahNumber: Int): AyatResponseList

    @GET("surah")
    suspend fun getSurahList(): SurahListResponse

    @GET("surah/{number}/editions/quran-uthmani,bn.bengali,ar.alafasy")
    suspend fun getSurahDetails(@Path("number") surahNumber: Int): SurahDetailsResponse
}

data class SurahListResponse(
    val code: Int,
    val status: String,
    val data: List<SurahSummary>
)

data class SurahSummary(
    val number: Int,
    val name: String,
    val englishName: String,
    val englishNameTranslation: String,
    val numberOfAyahs: Int,
    val revelationType: String
)

data class SurahDetailsResponse(
    val code: Int,
    val status: String,
    val data: List<SurahRemoteData>
)

data class SurahRemoteData(
    val number: Int,
    val name: String,
    val englishName: String,
    val englishNameTranslation: String,
    val revelationType: String,
    val ayahs: List<AyahRemoteModel>
)

data class AyatResponseList(
    val code: Int,
    val status: String,
    val data: List<AyahRemoteModel>
)

data class AyahRemoteModel(
    val number: Int,
    val text: String,
    val audio: String? = null,
    val numberInSurah: Int? = null,
    val edition: EditionInfo? = null,
    val surah: SurahSummary? = null
)

data class EditionInfo(
    val identifier: String,
    val language: String,
    val name: String,
    val englishName: String,
    val format: String,
    val type: String
)

object RetrofitInstance {
    private const val BASE_URL = "https://api.alquran.cloud/v1/"

    val api: AlQuranApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AlQuranApiService::class.java)
    }
}
