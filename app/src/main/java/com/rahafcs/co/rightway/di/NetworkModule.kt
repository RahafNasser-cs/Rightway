package com.rahafcs.co.rightway.di

import com.rahafcs.co.rightway.network.WorkoutApiService
import com.rahafcs.co.rightway.network.okHttpClient
import com.rahafcs.co.rightway.utility.Constant.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideWorkoutApiService(): WorkoutApiService {
        return Retrofit.Builder()
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(
                        KotlinJsonAdapterFactory()
                    ).build()
                )
            )
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build().create()
    }
}
