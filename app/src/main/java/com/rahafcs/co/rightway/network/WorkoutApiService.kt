package com.rahafcs.co.rightway.network

import com.rahafcs.co.rightway.data.Workout
import com.rahafcs.co.rightway.utility.Constant.RAPID_API_KEY
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface WorkoutApiService {
    @Headers(
        "x-rapidapi-host: exercisedb.p.rapidapi.com",
        "x-rapidapi-key: $RAPID_API_KEY"
    )
    @GET("/exercises")
    suspend fun getAllWorkout(): List<Workout>

    @Headers(
        "x-rapidapi-host: exercisedb.p.rapidapi.com",
        "x-rapidapi-key: $RAPID_API_KEY"
    )
    @GET("/exercises/equipment/{equipment}")
    suspend fun getWorkoutsByEquipment(@Path("equipment") equipment: String): List<Workout>
}
