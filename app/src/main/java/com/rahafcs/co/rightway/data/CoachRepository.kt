package com.rahafcs.co.rightway.data

import com.rahafcs.co.rightway.data.source.CoachRemoteDataSource
import kotlinx.coroutines.flow.Flow

class CoachRepository(private val coachRemoteDataSource: CoachRemoteDataSource) {

    fun saveCoachInfo(coach: User) = coachRemoteDataSource.saveCoachInfo(coach)

    suspend fun readCoachInfo(): Flow<User> = coachRemoteDataSource.readCoachInfo()

    suspend fun getCoachList(): Flow<List<User>> = coachRemoteDataSource.getCoachList()

    fun getUserType(): Flow<String> = coachRemoteDataSource.getUserType()
}
