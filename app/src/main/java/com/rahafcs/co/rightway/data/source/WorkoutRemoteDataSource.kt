package com.rahafcs.co.rightway.data.source

import com.google.firebase.firestore.FirebaseFirestore
import com.rahafcs.co.rightway.data.Workout
import com.rahafcs.co.rightway.network.WorkoutApiService
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception

// Implementation of a workouts data source as db in Firestore.
class WorkoutRemoteDataSource(
    private val api: WorkoutApiService,
    private val ioDispatcher: CoroutineDispatcher,
) : WorkoutDataSource {
    private val TAG = WorkoutRemoteDataSource::class.java.name
    private val db = FirebaseFirestore.getInstance()
    private val collection = "workouts"
    private val document = "listOfAllWorkouts"
//    private val ioDispatcher = Dispatchers.IO

    // To refresh workouts. Get all workouts from API --> RapidApi
    override suspend fun getAllWorkouts(isRefresh: Boolean): List<WorkoutsInfoUiState> {
        if (isRefresh) {
            try {
                val result = getAllWorkoutsFromRapidApi()
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
            }
        }
        return getAllWorkoutsFromFirestore()
    }

    // To get all workouts from RapidApi
    override suspend fun getAllWorkoutsFromRapidApi(): List<Workout> = withContext(ioDispatcher) {
        api.getAllWorkout()
    }

    // To get workouts by equipment type from RapidApi
    override suspend fun getWorkoutsByEquipmentFromRapidApi(equipment: String): List<Workout> =
        withContext(ioDispatcher) {
            api.getWorkoutsByEquipment(equipment)
        }

    // To get all workouts from Firestore if exit, if not exit get from RapidApi
    override suspend fun getAllWorkoutsFromFirestore(): List<WorkoutsInfoUiState> {
        var list = listOf<WorkoutsInfoUiState>()
        withContext(ioDispatcher) {
            try {
                db.collection(collection).document(document).get().addOnSuccessListener {
                    list = it.toObject(WorkoutsInfoUiState::class.java) as List<WorkoutsInfoUiState>
                }.addOnFailureListener { list = listOf() }
                return@withContext list
            } catch (e: Exception) {
            }
        }
        if (list.isEmpty()) {
            try {
                val result = getAllWorkoutsFromRapidApi()
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
            }
        }
        return listOf() // to avoid error
    }

    // To save all workouts from RapidApi into Firestore
    override fun saveAllWorkouts(listOfAllWorkouts: List<WorkoutsInfoUiState>) {
        db.collection(collection).document(document).set(listOfAllWorkouts)
    }
}
