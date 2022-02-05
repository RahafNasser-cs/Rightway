package com.rahafcs.co.rightway.di

import com.rahafcs.co.rightway.data.DefaultWorkoutsRepository
import com.rahafcs.co.rightway.data.WorkoutsRepository
import com.rahafcs.co.rightway.data.source.WorkoutDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideWorkoutRepository(workoutRemoteDataSource: WorkoutDataSource): WorkoutsRepository =
        DefaultWorkoutsRepository(workoutRemoteDataSource)
}
