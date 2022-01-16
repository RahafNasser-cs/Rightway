package com.rahafcs.co.rightway.data

import com.rahafcs.co.rightway.data.source.UserRemoteDataSource
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    fun addUserInfo(userInfo: User) = userRemoteDataSource.saveUserInfo(userInfo)

    fun addUserWorkout(listOfSavedWorkouts: List<WorkoutsInfoUiState>) =
        userRemoteDataSource.addUserWorkout(listOfSavedWorkouts)

    fun deleteWorkout(listOfSavedWorkouts: List<WorkoutsInfoUiState>) =
        userRemoteDataSource.deleteWorkout(listOfSavedWorkouts)

    fun addListOfSavedWorkoutsLocal(workoutsInfoUiState: WorkoutsInfoUiState) =
        userRemoteDataSource.addListOfSavedWorkoutsLocal(workoutsInfoUiState)

    fun removeListOfSavedWorkoutsLocal(workoutsInfoUiState: WorkoutsInfoUiState) =
        userRemoteDataSource.removeListOfSavedWorkoutsLocal(workoutsInfoUiState)

    fun checkIsSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState) =
        userRemoteDataSource.checkIsSavedWorkout(workoutsInfoUiState)

    suspend fun isSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState) =
        userRemoteDataSource.isSavedWorkout(workoutsInfoUiState)

    suspend fun reloadListOfSavedWorkouts(): Flow<List<WorkoutsInfoUiState>> =
        userRemoteDataSource.reloadListOfSavedWorkouts()

    suspend fun readUserInfo(): Flow<User> = userRemoteDataSource.readUserInfo()
    fun getUserStatus(): Flow<String> = userRemoteDataSource.getUserStatus()
    suspend fun getTrainer(): Flow<List<User>> = userRemoteDataSource.getTrainer()
}
