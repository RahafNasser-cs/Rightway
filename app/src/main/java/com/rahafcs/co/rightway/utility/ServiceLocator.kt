package com.rahafcs.co.rightway.utility

import com.rahafcs.co.rightway.data.WorkoutRepository
import com.rahafcs.co.rightway.data.source.WorkoutRemoteDataSource
import com.rahafcs.co.rightway.network.WorkoutApi
import com.rahafcs.co.rightway.network.WorkoutApiService

object ServiceLocator {
    fun provideWorkoutApi(): WorkoutApiService = WorkoutApi.retrofitService

    fun provideWorkoutRemoteDataSource(): WorkoutRemoteDataSource = WorkoutRemoteDataSource(
        provideWorkoutApi()
    )

    fun provideWorkoutRepository(): WorkoutRepository = WorkoutRepository(
        provideWorkoutRemoteDataSource()
    )
}
