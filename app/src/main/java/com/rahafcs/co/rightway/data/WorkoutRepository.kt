package com.rahafcs.co.rightway.data

import com.rahafcs.co.rightway.data.source.WorkoutRemoteDataSource

class WorkoutRepository(private val workoutRemoteDataSource: WorkoutRemoteDataSource) {
    suspend fun getAllWorkouts(): List<Workout> = workoutRemoteDataSource.getAllWorkouts()
}
