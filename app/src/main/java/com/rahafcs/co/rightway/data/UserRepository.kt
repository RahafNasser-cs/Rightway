package com.rahafcs.co.rightway.data

import com.rahafcs.co.rightway.ui.workout.WorkoutsInfoUiState
import kotlinx.coroutines.flow.Flow

// Interface to the data layer.

interface UserRepository {
    // To save user info.
    fun saveUserInfo(user: User)

    // To get user info. 
    suspend fun readUserInfo(): Flow<User>

    // To get user type --> Trainer "Coach" or Trainee.
    fun getUserType(): Flow<String>

    // To add a new workout to local list of saved workouts.
    fun addListOfSavedWorkoutsLocal(workoutsInfoUiState: WorkoutsInfoUiState)

    // To remove workout from local list of saved workouts.
    fun removeListOfSavedWorkoutsLocal(workoutsInfoUiState: WorkoutsInfoUiState)

    // Check if workoutsInfoUiState is exit in list of saved workouts.
    fun checkIsSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState): Boolean

    // To get list of saved workout from Firestore.
    suspend fun reloadListOfSavedWorkouts(): Flow<List<WorkoutsInfoUiState>>

    // To get list of coaches.
    suspend fun getCoachList(): Flow<List<User>>
}
