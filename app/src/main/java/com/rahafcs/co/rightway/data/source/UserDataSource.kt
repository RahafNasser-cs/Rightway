package com.rahafcs.co.rightway.data.source

import com.rahafcs.co.rightway.data.User
import com.rahafcs.co.rightway.ui.workout.WorkoutsInfoUiState
import kotlinx.coroutines.flow.Flow

// Main entry point to accessing user data source.

interface UserDataSource {
    // To save user info 
    fun saveUserInfo(user: User)

    // To get user info 
    suspend fun readUserInfo(): Flow<User>

    // To get user type --> Trainer "Coach" or Trainee
    fun getUserType(): Flow<String>

    // To add a new workout to local list of saved workouts
    fun addListOfSavedWorkoutsLocal(workoutsInfoUiState: WorkoutsInfoUiState)

    // To remove workout from local list of saved workouts
    fun removeListOfSavedWorkoutsLocal(workoutsInfoUiState: WorkoutsInfoUiState)

    // Update list of saved workouts 
    fun updateListOfSavedWorkouts()

    // Check if workoutsInfoUiState is exit in list of saved workouts
    fun checkIsSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState): Boolean

    // To get list of saved workout from Firestore
    suspend fun reloadListOfSavedWorkouts(): Flow<List<WorkoutsInfoUiState>>

    // To get List of coaches
    suspend fun getCoachList(): Flow<List<User>>
}
