package com.rudra.prayerallthetime.di

import com.rudra.prayerallthetime.data.AlQuranApiService
import com.rudra.prayerallthetime.data.HadithApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://hadithapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideHadithApiService(retrofit: Retrofit): HadithApiService {
        return retrofit.create(HadithApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAlQuranApiService(): AlQuranApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.alquran.cloud/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AlQuranApiService::class.java)
    }
}
