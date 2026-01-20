package com.rudra.prayerallthetime.di

import com.rudra.prayerallthetime.data.AlQuranApiService
import com.rudra.prayerallthetime.data.HadithApiService
import com.rudra.prayerallthetime.data.remote.PlacesApiService
import com.rudra.prayerallthetime.data.remote.PrayerApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("HadithRetrofit")
    fun provideHadithRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://hadithapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideHadithApiService(@Named("HadithRetrofit") retrofit: Retrofit): HadithApiService {
        return retrofit.create(HadithApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("AladhanRetrofit")
    fun provideAladhanRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.aladhan.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePrayerApiService(@Named("AladhanRetrofit") retrofit: Retrofit): PrayerApiService {
        return retrofit.create(PrayerApiService::class.java)
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

    @Provides
    @Singleton
    @Named("OverpassRetrofit")
    fun provideOverpassRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://overpass-api.de/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePlacesApiService(@Named("OverpassRetrofit") retrofit: Retrofit): PlacesApiService {
        return retrofit.create(PlacesApiService::class.java)
    }
}
