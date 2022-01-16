package com.rahafcs.co.rightway.data

import com.rahafcs.co.rightway.data.source.CoachRemoteDataSource
import kotlinx.coroutines.flow.Flow

class CoachRepository(private val coachRemoteDataSource: CoachRemoteDataSource) {
    fun readCoachesInfo(): Flow<List<Coach>> = coachRemoteDataSource.readCoachesInfo()
    fun saveCoachInfo(coach: User) = coachRemoteDataSource.saveCoachInfo(coach)
    suspend fun readCoachInfo(): Flow<User> = coachRemoteDataSource.readCoachInfo()
    fun addListOfCoachesEmail(email: String) = coachRemoteDataSource.addListOfCoachesEmail(email)
    fun removeListOfCoachesEmail(email: String) =
        coachRemoteDataSource.removeListOfCoachesEmail(email)

    fun checkIsCoach(email: String) = coachRemoteDataSource.checkIsCoach(email)
    suspend fun reloadCoachEmailList(): Flow<List<String>> = coachRemoteDataSource.reloadCoachEmailList()
    suspend fun getTrainer(): Flow<List<User>> = coachRemoteDataSource.getTrainer()
}
