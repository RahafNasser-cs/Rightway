package com.rahafcs.co.rightway.data

import com.rahafcs.co.rightway.data.source.WorkoutDataSource
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState

// Default implementation of WorkoutsRepository. Single entry point for managing workouts data.

class DefaultWorkoutsRepository(
    private val workoutRemoteDataSource: WorkoutDataSource,
) : WorkoutsRepository {
    // To get all workouts.
    override suspend fun getAllWorkouts(): List<Workout> =
        workoutRemoteDataSource.getAllWorkoutsFromRapidApi()

    // To refresh workouts. Get all workouts from API --> RapidApi.
    override suspend fun getAllWorkouts(forceUpdate: Boolean): Result<List<WorkoutsInfoUiState>> =
        TODO("Not yet implemented")

    // To get workouts by equipment.
    override suspend fun getWorkoutsByEquipment(equipment: String): List<Workout> =
        workoutRemoteDataSource.getWorkoutsByEquipmentFromRapidApi(equipment)
}
