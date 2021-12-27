package com.rahafcs.co.rightway.data.source

import com.rahafcs.co.rightway.data.WorkoutResponse

interface WorkoutDataSource {
    suspend fun getAllWorkouts(): WorkoutResponse
}
