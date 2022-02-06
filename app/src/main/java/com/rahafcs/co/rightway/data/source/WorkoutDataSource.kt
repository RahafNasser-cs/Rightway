package com.rahafcs.co.rightway.data.source

import com.rahafcs.co.rightway.data.Workout
import com.rahafcs.co.rightway.ui.workout.WorkoutsInfoUiState

// Main entry point to accessing workout data source. 

interface WorkoutDataSource {

    // To refresh workouts. Get all workouts from API --> RapidApi.
    suspend fun getAllWorkouts(isRefresh: Boolean = false): List<WorkoutsInfoUiState>

    // To get all workouts from RapidApi.
    suspend fun getAllWorkoutsFromRapidApi(): List<Workout>

    // To get workouts by equipment type from RapidApi.
    suspend fun getWorkoutsByEquipmentFromRapidApi(equipment: String): List<Workout>

    // To get all workouts from Firestore.
    suspend fun getAllWorkoutsFromFirestore(): List<WorkoutsInfoUiState>

    // To save all workouts from RapidApi into Firestore.
    fun saveAllWorkouts(listOfAllWorkouts: List<WorkoutsInfoUiState>)
}
