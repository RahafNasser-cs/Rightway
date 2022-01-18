package com.rahafcs.co.rightway.data

import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState

interface WorkoutsRepository {
    suspend fun getAllWorkouts(): List<Workout>
    suspend fun getAllWorkouts(forceUpdate: Boolean = false): Result<List<WorkoutsInfoUiState>>
    suspend fun getWorkoutsByEquipment(equipment: String): List<Workout>
}
