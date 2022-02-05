package com.rahafcs.co.rightway.di

import com.rahafcs.co.rightway.data.source.WorkoutDataSource
import com.rahafcs.co.rightway.data.source.WorkoutRemoteDataSource
import com.rahafcs.co.rightway.network.WorkoutApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Singleton
    @Provides
    fun provideWorkoutRemoteDataSource(workoutApiService: WorkoutApiService): WorkoutDataSource =
        WorkoutRemoteDataSource(workoutApiService)
}