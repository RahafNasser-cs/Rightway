package com.rahafcs.co.rightway.data

import com.rahafcs.co.rightway.data.source.CoachRemoteDataSource
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import kotlinx.coroutines.flow.Flow

// Implementation of UserRepository as CoachRepository. Single entry point for managing coaches data.

class CoachRepository(
    private val coachRemoteDataSource: CoachRemoteDataSource,
) : UserRepository {

    // To save user info.
    override fun saveUserInfo(user: User) = coachRemoteDataSource.saveUserInfo(user)

    // To get user info.
    override suspend fun readUserInfo(): Flow<User> = coachRemoteDataSource.readUserInfo()

    // To get user type --> Trainer "Coach" or Trainee.
    override fun getUserType(): Flow<String> = coachRemoteDataSource.getUserType()

    // To add a new workout to local list of saved workouts.
    override fun addListOfSavedWorkoutsLocal(workoutsInfoUiState: WorkoutsInfoUiState) =
        coachRemoteDataSource.addListOfSavedWorkoutsLocal(workoutsInfoUiState)

    // To remove workout from local list of saved workouts.
    override fun removeListOfSavedWorkoutsLocal(workoutsInfoUiState: WorkoutsInfoUiState) =
        coachRemoteDataSource.removeListOfSavedWorkoutsLocal(workoutsInfoUiState)

    // Check if workoutsInfoUiState is exit in list of saved workouts.
    override fun checkIsSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState): Boolean =
        coachRemoteDataSource.checkIsSavedWorkout(workoutsInfoUiState)

    // To get list of saved workout from Firestore.
    override suspend fun reloadListOfSavedWorkouts(): Flow<List<WorkoutsInfoUiState>> =
        coachRemoteDataSource.reloadListOfSavedWorkouts()

    // To get list of coaches.
    override suspend fun getCoachList(): Flow<List<User>> = coachRemoteDataSource.getCoachList()
}
