package com.rahafcs.co.rightway.network

import com.rahafcs.co.rightway.data.Workout
import com.rahafcs.co.rightway.utility.Constant.RAPID_API_KEY
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import javax.inject.Inject

private const val BASE_URL = "https://exercisedb.p.rapidapi.com"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private fun loggingInterceptor(): HttpLoggingInterceptor {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return loggingInterceptor
}

val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor()).build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()

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

class WorkoutApiServiceImpl @Inject constructor(private val workoutApiService: WorkoutApiService) :
    WorkoutApiService {
    override suspend fun getAllWorkout(): List<Workout> = workoutApiService.getAllWorkout()

    override suspend fun getWorkoutsByEquipment(equipment: String): List<Workout> =
        workoutApiService.getWorkoutsByEquipment(equipment)
}

object WorkoutApi {
    val retrofitService: WorkoutApiService by lazy {
        retrofit.create(WorkoutApiService::class.java)
    }
}
