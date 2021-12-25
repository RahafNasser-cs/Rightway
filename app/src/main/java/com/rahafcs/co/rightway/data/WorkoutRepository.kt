package com.rahafcs.co.rightway.data

import com.rahafcs.co.rightway.data.source.WorkoutRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class WorkoutRepository(
    private val workoutRemoteDataSource: WorkoutRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
)
