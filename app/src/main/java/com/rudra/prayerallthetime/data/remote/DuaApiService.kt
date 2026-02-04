package com.rudra.prayerallthetime.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DuaApiService {
    @GET("duas")
    suspend fun getDuas(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): DuaListResponse

    @GET("duas/category/{category}")
    suspend fun getDuasByCategory(
        @Path("category") category: String,
        @Query("page") page: Int = 1
    ): DuaListResponse

    @GET("duas/daily")
    suspend fun getDailyDua(): DuaRemoteEntity
}

data class DuaListResponse(
    val code: Int,
    val status: String,
    val data: List<DuaRemoteEntity>,
    val pagination: PaginationInfo
)

data class DuaRemoteEntity(
    val id: Int,
    val title: String,
    val arabic: String,
    val transliteration: String?,
    val translation: String,
    val translationBn: String?,
    val reference: String?,
    val category: String
)

data class PaginationInfo(
    val currentPage: Int,
    val totalPages: Int,
    val hasNextPage: Boolean
)
