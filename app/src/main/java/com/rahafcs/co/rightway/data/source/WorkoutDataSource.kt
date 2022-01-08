package com.rahafcs.co.rightway.data.source

import com.rahafcs.co.rightway.data.Workout
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState

interface WorkoutDataSource {
    suspend fun getAllWorkouts(isRefresh: Boolean = false): List<WorkoutsInfoUiState>
    suspend fun getAllWorkoutsFromApi(): List<Workout>
    suspend fun getAllWorkoutsFromFirestore(): List<WorkoutsInfoUiState>

    fun saveAllWorkouts(listOfAllWorkouts: List<WorkoutsInfoUiState>)
}
