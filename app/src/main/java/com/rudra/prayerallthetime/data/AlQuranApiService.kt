package com.rudra.prayerallthetime.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface AlQuranApiService {
    @GET("ayah/{ayah}/editions/quran-uthmani,en.sahih")
    suspend fun getAyatOfTheDay(@Path("ayah") ayahNumber: Int): AyatResponseList
}

data class AyatResponseList(
    val code: Int,
    val status: String,
    val data: List<AyatData>
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
