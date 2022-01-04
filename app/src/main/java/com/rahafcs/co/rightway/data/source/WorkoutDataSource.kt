package com.rahafcs.co.rightway.data.source

import com.rahafcs.co.rightway.data.Workout

interface WorkoutDataSource {
    suspend fun getAllWorkouts(): List<Workout>
}
