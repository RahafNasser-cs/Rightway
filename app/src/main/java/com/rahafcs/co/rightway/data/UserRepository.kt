package com.rahafcs.co.rightway.data

import com.rahafcs.co.rightway.data.source.UserRemoteDataSource
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun addUserInfo(userInfo: User) = userRemoteDataSource.saveUserInfo(userInfo)

    suspend fun addUserWorkout(workoutsInfoUiState: WorkoutsInfoUiState) =
        userRemoteDataSource.addUserWorkout(workoutsInfoUiState)

    suspend fun deleteWorkout(workoutsInfoUiState: WorkoutsInfoUiState) =
        userRemoteDataSource.deleteWorkout(workoutsInfoUiState)

    suspend fun isSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState) =
        userRemoteDataSource.isSavedWorkout(workoutsInfoUiState)

    suspend fun readUserInfo(): Flow<User> = userRemoteDataSource.readUserInfo()
}


