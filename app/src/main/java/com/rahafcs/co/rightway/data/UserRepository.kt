package com.rahafcs.co.rightway.data

import com.rahafcs.co.rightway.data.source.UserRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class UserRepository(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun addUserInfo(userInfo: User) {
        userRemoteDataSource.saveUserInfo(userInfo)
    }
}
