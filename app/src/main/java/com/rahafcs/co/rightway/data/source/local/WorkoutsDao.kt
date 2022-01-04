package com.rahafcs.co.rightway.data.source.local

import androidx.room.*
import com.rahafcs.co.rightway.data.WorkoutListForDatabase

@Dao
interface WorkoutsDao {
    @Query("select * from workoutlistfordatabase")
    suspend fun getAllWorkouts(): List<WorkoutListForDatabase>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWorkout(workout: WorkoutListForDatabase)

    @Query("select * from workoutlistfordatabase where saved=:isSaved")
    fun getSavedWorkouts(isSaved: Boolean = true): List<WorkoutListForDatabase>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveWorkout(workout: WorkoutListForDatabase) // assign saved =  true

    @Delete
    suspend fun deleteWorkout(workout: WorkoutListForDatabase)
}
