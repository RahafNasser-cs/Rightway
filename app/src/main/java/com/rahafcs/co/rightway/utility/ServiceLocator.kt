package com.rahafcs.co.rightway.utility

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.data.*
import com.rahafcs.co.rightway.data.source.*

object ServiceLocator {
//    private fun provideWorkoutApi(): WorkoutApiService = WorkoutApi.retrofitService

//    private fun provideWorkoutRemoteDataSource(): WorkoutRemoteDataSource =
//        WorkoutRemoteDataSource(provideWorkoutApi())

//    fun provideWorkoutRepository(): DefaultWorkoutsRepository =
//        DefaultWorkoutsRepository(provideWorkoutRemoteDataSource())

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

//    private fun provideAuthRemoteDataSource(): AuthRemoteDataSource = AuthRemoteDataSource()
//
//    fun provideAuthRepository(): AuthRepository = AuthRepository(provideAuthRemoteDataSource())

    fun provideGoogleSignInOptions(): GoogleSignInOptions =
        GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(ProgramListService.application.getString(R.string.web_client))
            .requestEmail()
            .build()

    object ProgramListService {
        lateinit var application: Application
    }
//
//    private fun provideDao(workoutsDao: WorkoutsDao): WorkoutsDao = workoutsDao
//    private fun provideWorkoutLocalDataSource(): WorkoutsLocalDataSource = WorkoutsLocalDataSource(
//        provideDao())
}
