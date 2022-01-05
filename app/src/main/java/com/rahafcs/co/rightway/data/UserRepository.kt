package com.rahafcs.co.rightway.data

import com.rahafcs.co.rightway.data.source.UserRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun addUserInfo(userInfo: User) = userRemoteDataSource.saveUserInfo(userInfo)

//    fun addUserWorkout(workoutsInfoUiState: WorkoutsInfoUiState) =
//        userRemoteDataSource.addUserWorkout(workoutsInfoUiState)
//
//    fun deleteWorkout(workoutsInfoUiState: WorkoutsInfoUiState) =
//        userRemoteDataSource.deleteWorkout(workoutsInfoUiState)
//
//    fun isSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState) =
//        userRemoteDataSource.isSavedWorkout(workoutsInfoUiState)

    suspend fun readUserInfo(): Flow<User> = userRemoteDataSource.readUserInfo()
}


