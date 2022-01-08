package com.rahafcs.co.rightway.network

import com.rahafcs.co.rightway.data.Workout
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://exercisedb.p.rapidapi.com"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private fun loggingInterceptor(): HttpLoggingInterceptor {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return loggingInterceptor
}

val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor()).build()
// val client = OkHttpClient()
//    .networkInterceptors()
//    .add(
//        Interceptor {
//            it.proceed(
//                it.request()
//                    .newBuilder()
//                    .addHeader("x-rapidapi-host", "exercisedb.p.rapidapi.com")
//                    .addHeader("x-rapidapi-key", "ccb06bc4c0mshb7a29f7814116c6p14a25ajsn3cfb231e6ceb")
//                    .build()
//            )
//        }
//    )

// val request = Request.Builder()
//    .url("https://exercisedb.p.rapidapi.com/exercises")
//    .get()
//    .addHeader("x-rapidapi-host", "exercisedb.p.rapidapi.com")
//    .addHeader("x-rapidapi-key", "ccb06bc4c0mshb7a29f7814116c6p14a25ajsn3cfb231e6ceb")
//    .build()

// val client = OkHttpClient()
//
// val request = Request.Builder()
//    .addHeader("x-rapidapi-host", "exercisedb.p.rapidapi.com")
//    .addHeader("x-rapidapi-key", "ccb06bc4c0mshb7a29f7814116c6p14a25ajsn3cfb231e6ceb")
//    .build()
//
// val response = client.newCall(request).execute()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()

interface WorkoutApiService {

    @Headers(
        "x-rapidapi-host: exercisedb.p.rapidapi.com",
        "x-rapidapi-key: ccb06bc4c0mshb7a29f7814116c6p14a25ajsn3cfb231e6ceb"
    )
    @GET("/exercises")
    suspend fun getAllWorkout(): List<Workout>

    @Headers(
        "x-rapidapi-host: exercisedb.p.rapidapi.com",
        "x-rapidapi-key: ccb06bc4c0mshb7a29f7814116c6p14a25ajsn3cfb231e6ceb"
    )
    @GET("/exercises/equipment/{equipment}")
    suspend fun getWorkoutsByEquipment(@Path("equipment")equipment: String): List<Workout>
}

object WorkoutApi {
    val retrofitService: WorkoutApiService by lazy {
        retrofit.create(WorkoutApiService::class.java)
    }
}
