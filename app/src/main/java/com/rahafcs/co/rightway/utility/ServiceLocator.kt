package com.rahafcs.co.rightway.utility

import com.rahafcs.co.rightway.data.CoachRepository
import com.rahafcs.co.rightway.data.DefaultWorkoutsRepository
import com.rahafcs.co.rightway.data.TraineeRepository
import com.rahafcs.co.rightway.data.source.CoachRemoteDataSource
import com.rahafcs.co.rightway.data.DefaultUserRepository
import com.rahafcs.co.rightway.data.source.TraineeRemoteDataSource
import com.rahafcs.co.rightway.data.source.WorkoutRemoteDataSource
import com.rahafcs.co.rightway.data.source.UserRemoteDataSource
import com.rahafcs.co.rightway.network.WorkoutApi
import com.rahafcs.co.rightway.network.WorkoutApiService

object ServiceLocator {
    private fun provideWorkoutApi(): WorkoutApiService = WorkoutApi.retrofitService

    private fun provideWorkoutRemoteDataSource(): WorkoutRemoteDataSource =
        WorkoutRemoteDataSource(provideWorkoutApi())

    fun provideWorkoutRepository(): DefaultWorkoutsRepository =
        DefaultWorkoutsRepository(provideWorkoutRemoteDataSource())

    private fun provideTraineeRemoteDataSource(): TraineeRemoteDataSource =
        TraineeRemoteDataSource()

    fun provideTraineeRepository(): TraineeRepository =
        TraineeRepository(provideTraineeRemoteDataSource())

    private fun provideCoachRemoteDataSource(): CoachRemoteDataSource = CoachRemoteDataSource()

    fun provideCoachRepository(): CoachRepository = CoachRepository(provideCoachRemoteDataSource())

    private fun provideUserRemoteDataSource(): UserRemoteDataSource = UserRemoteDataSource()

    fun provideDefaultUserRepository(): DefaultUserRepository = DefaultUserRepository(
        provideUserRemoteDataSource()
    )
//
//    private fun provideDao(workoutsDao: WorkoutsDao): WorkoutsDao = workoutsDao
//    private fun provideWorkoutLocalDataSource(): WorkoutsLocalDataSource = WorkoutsLocalDataSource(
//        provideDao())
}
