package com.rahafcs.co.rightway.data

import com.rahafcs.co.rightway.data.source.CoachRemoteDataSource
import kotlinx.coroutines.flow.Flow

class CoachRepository(private val coachRemoteDataSource: CoachRemoteDataSource) {
    fun readCoachesInfo(): Flow<List<Coach>> = coachRemoteDataSource.readCoachesInfo()
    fun saveCoachInfo(coach: Coach) = coachRemoteDataSource.saveCoachInfo(coach)
    fun readCoachInfo(): Flow<Coach> = coachRemoteDataSource.readCoachInfo()
    fun addListOfCoachesEmail(email: String) = coachRemoteDataSource.addListOfCoachesEmail(email)
    fun removeListOfCoachesEmail(email: String) =
        coachRemoteDataSource.removeListOfCoachesEmail(email)

    fun checkIsCoach(email: String) = coachRemoteDataSource.checkIsCoach(email)
    fun reloadCoachEmailList(): Flow<List<String>> = coachRemoteDataSource.reloadCoachEmailList()
}
