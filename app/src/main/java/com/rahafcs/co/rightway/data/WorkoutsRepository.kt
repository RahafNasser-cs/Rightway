package com.rahafcs.co.rightway.data

import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState

// Interface to the data layer.

interface WorkoutsRepository {
    // To get all workouts.
    suspend fun getAllWorkouts(): List<Workout>

    // To refresh workouts.
    suspend fun getAllWorkouts(forceUpdate: Boolean = false): Result<List<WorkoutsInfoUiState>>

    // To get workouts by equipment.
    suspend fun getWorkoutsByEquipment(equipment: String): List<Workout>
}
