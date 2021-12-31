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

    fun addUserWorkout(workoutsInfoUiState: WorkoutsInfoUiState, userName: String) =
        userRemoteDataSource.addUserWorkout(workoutsInfoUiState, userName)

    suspend fun readUserInfo(userName: String): Flow<User> = userRemoteDataSource.readUserInfo(userName)
}
