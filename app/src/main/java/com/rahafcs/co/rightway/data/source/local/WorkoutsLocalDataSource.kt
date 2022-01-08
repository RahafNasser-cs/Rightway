// package com.rahafcs.co.rightway.data.source.local
//
// import com.rahafcs.co.rightway.data.Workout
// import com.rahafcs.co.rightway.data.source.WorkoutDataSource
// import kotlinx.coroutines.CoroutineDispatcher
// import kotlinx.coroutines.Dispatchers
//
// class WorkoutsLocalDataSource(
//    private val workoutsDao: WorkoutsDao,
//    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
// ) : WorkoutDataSource {
//    override suspend fun getAllWorkouts(): List<Workout> {
//        TODO("Not yet implemented")
//    }
//
// //    override suspend fun getAllWorkouts(): Result<List<Workout>> = withContext(dispatcher) {
// //        return@withContext try {
// //            Result.Success(workoutsDao.getAllWorkouts())
// //        } catch (e: Exception) {
// //            Result.Failure(e)
// //        }
// //
// //    }
// }
