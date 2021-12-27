package com.rahafcs.co.rightway.data.source

import android.util.Log
import com.rahafcs.co.rightway.data.WorkoutResponse
import com.rahafcs.co.rightway.network.WorkoutApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorkoutRemoteDataSource(
    private val api: WorkoutApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : WorkoutDataSource {

    override suspend fun getAllWorkouts(): WorkoutResponse = withContext(dispatcher) {
        api.getAllWorkout()
    }
}
