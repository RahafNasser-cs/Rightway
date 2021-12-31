package com.rahafcs.co.rightway.data

import com.rahafcs.co.rightway.data.source.WorkoutDataSource
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState

class DefaultWorkoutsRepository(
    private val workoutRemoteDataSource: WorkoutDataSource
) : WorkoutsRepository {
    suspend fun getAllWorkouts(): List<Workout> = workoutRemoteDataSource.getAllWorkouts()
    override suspend fun getAllWorkouts(forceUpdate: Boolean): Result<List<WorkoutsInfoUiState>> {
        TODO("Not yet implemented")
        // workoutsLocalDataSource.getAllWorkouts()
    }
}
