package com.rahafcs.co.rightway.data

import com.rahafcs.co.rightway.data.source.TraineeRemoteDataSource
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

// Implementation of UserRepository as TraineeRepository. Single entry point for managing trainee data.

class TraineeRepository(
    private val traineeRemoteDataSource: TraineeRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UserRepository {

    // To save user info. 
    override fun saveUserInfo(user: User) = traineeRemoteDataSource.saveUserInfo(user)

    // To get user info.
    override suspend fun readUserInfo(): Flow<User> = traineeRemoteDataSource.readUserInfo()

    // To get user type --> Trainer "Coach" or Trainee.
    override fun getUserType(): Flow<String> = traineeRemoteDataSource.getUserType()

    // To add a new workout to local list of saved workouts.
    override fun addListOfSavedWorkoutsLocal(workoutsInfoUiState: WorkoutsInfoUiState) =
        traineeRemoteDataSource.addListOfSavedWorkoutsLocal(workoutsInfoUiState)

    // To remove workout from local list of saved workouts.
    override fun removeListOfSavedWorkoutsLocal(workoutsInfoUiState: WorkoutsInfoUiState) =
        traineeRemoteDataSource.removeListOfSavedWorkoutsLocal(workoutsInfoUiState)

    // Check if workoutsInfoUiState is exit in list of saved workouts.
    override fun checkIsSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState): Boolean =
        traineeRemoteDataSource.checkIsSavedWorkout(workoutsInfoUiState)

    // To get list of saved workout from Firestore.
    override suspend fun reloadListOfSavedWorkouts(): Flow<List<WorkoutsInfoUiState>> =
        traineeRemoteDataSource.reloadListOfSavedWorkouts()

    // To get list of coaches.
    override suspend fun getCoachList(): Flow<List<User>> = traineeRemoteDataSource.getCoachList()
}
