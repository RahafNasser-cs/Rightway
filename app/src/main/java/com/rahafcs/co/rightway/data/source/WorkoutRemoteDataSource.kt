package com.rahafcs.co.rightway.data.source

import com.rahafcs.co.rightway.data.WorkoutResponse

interface WorkoutRemoteDataSource {
    suspend fun getAllWorkout(): WorkoutResponse
}
