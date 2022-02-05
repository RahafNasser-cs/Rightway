package com.rahafcs.co.rightway.data

import com.rahafcs.co.rightway.data.source.UserRemoteDataSource
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Default implementation of UserRepository. Single entry point for managing users data.

class DefaultUserRepository @Inject constructor(
//    private val coachRemoteDataSource: CoachRemoteDataSource,
//    private val traineeRemoteDataSource: TraineeRemoteDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
) : UserRepository {
    // To save user info.
    override fun saveUserInfo(user: User) = userRemoteDataSource.saveUserInfo(user)

    // To get user info.
    override suspend fun readUserInfo(): Flow<User> = userRemoteDataSource.readUserInfo()

    // To get user type --> Trainer "Coach" or Trainee.
    override fun getUserType(): Flow<String> = userRemoteDataSource.getUserType()

    // To add a new workout to local list of saved workouts.
    override fun addListOfSavedWorkoutsLocal(workoutsInfoUiState: WorkoutsInfoUiState) =
        userRemoteDataSource.addListOfSavedWorkoutsLocal(workoutsInfoUiState)

    // To remove workout from local list of saved workouts.
    override fun removeListOfSavedWorkoutsLocal(workoutsInfoUiState: WorkoutsInfoUiState) =
        userRemoteDataSource.removeListOfSavedWorkoutsLocal(workoutsInfoUiState)

    // Check if  workoutsInfoUiState exists in list of saved workouts.
    override fun checkIsSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState): Boolean =
        userRemoteDataSource.checkIsSavedWorkout(workoutsInfoUiState)

    // To get list of saved workout from Firestore.
    override suspend fun reloadListOfSavedWorkouts(): Flow<List<WorkoutsInfoUiState>> =
        userRemoteDataSource.reloadListOfSavedWorkouts()

    // To get list of coaches.
    override suspend fun getCoachList(): Flow<List<User>> = userRemoteDataSource.getCoachList()
}
