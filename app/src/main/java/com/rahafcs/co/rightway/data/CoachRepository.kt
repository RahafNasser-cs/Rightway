package com.rahafcs.co.rightway.data

import com.rahafcs.co.rightway.data.source.CoachRemoteDataSource
import kotlinx.coroutines.flow.Flow

class CoachRepository(private val coachRemoteDataSource: CoachRemoteDataSource) {
    fun readCoachesInfo(): Flow<List<Coach>> = coachRemoteDataSource.readCoachesInfo()
    fun saveCoachInfo(coach: Coach) = coachRemoteDataSource.saveCoachInfo(coach)
}
