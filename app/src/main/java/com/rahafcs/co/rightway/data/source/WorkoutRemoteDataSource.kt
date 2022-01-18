package com.rahafcs.co.rightway.data.source

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.rahafcs.co.rightway.data.Workout
import com.rahafcs.co.rightway.network.WorkoutApiService
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class WorkoutRemoteDataSource(
    private val api: WorkoutApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : WorkoutDataSource {
    val TAG = WorkoutRemoteDataSource::class.java.name
    val db = FirebaseFirestore.getInstance()

    override suspend fun getAllWorkouts(isRefresh: Boolean): List<WorkoutsInfoUiState> {
        if (isRefresh) {
            try {
                val result = getAllWorkoutsFromApi()
                val list = result.map {
                    WorkoutsInfoUiState(
                        gifUrl = it.gifUrl,
                        name = it.name,
                        equipment = it.equipment,
                        target = it.target,
                        bodyPart = it.bodyPart
                    )
                }
                saveAllWorkouts(list)
                return list
            } catch (e: Exception) {
                Log.e(TAG, "getAllWorkouts: a")
            }
        }
        return getAllWorkoutsFromFirestore()
    }

    override suspend fun getAllWorkoutsFromApi(): List<Workout> = withContext(ioDispatcher) {
        api.getAllWorkout()
    }

    override suspend fun getWorkoutsByEquipmentFromApi(equipment: String): List<Workout> = withContext(ioDispatcher){
        api.getWorkoutsByEquipment(equipment)
    }

    // get workouts from Firestore if exit, if not exit get from api
    override suspend fun getAllWorkoutsFromFirestore(): List<WorkoutsInfoUiState> {
        var list = listOf<WorkoutsInfoUiState>()
        withContext(ioDispatcher) {
            try {
                db.collection("workouts").document("listOfAllWorkouts").get().addOnSuccessListener {
                    list = it.toObject(WorkoutsInfoUiState::class.java) as List<WorkoutsInfoUiState>
                }.addOnFailureListener { list = listOf() }
                return@withContext list
            } catch (e: Exception) {
                Log.e(TAG, "getAllWorkoutsFromFirestore: $e")
            }
        }
        if (list.isEmpty()) {
            try {
                val result = getAllWorkoutsFromApi()
                list = result.map {
                    WorkoutsInfoUiState(
                        gifUrl = it.gifUrl,
                        name = it.name,
                        equipment = it.equipment,
                        target = it.target,
                        bodyPart = it.bodyPart
                    )
                }
                saveAllWorkouts(list) // save all workout in Firestore
                return list // return workout list to viewModel
            } catch (e: Exception) {
                Log.e(TAG, "getAllWorkoutsFromFirestore: $e")
            }
        }
        return listOf() // to avoid error
    }

    override fun saveAllWorkouts(listOfAllWorkouts: List<WorkoutsInfoUiState>) {
        db.collection("workouts").document("listOfAllWorkouts").set(listOfAllWorkouts)
    }
}
