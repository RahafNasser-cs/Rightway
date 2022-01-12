package com.rahafcs.co.rightway.data.source

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rahafcs.co.rightway.data.User
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext

class UserRemoteDataSource {
    private val db = FirebaseFirestore.getInstance()
    private val TAG = "UserRemoteDataSource"
    private val user = FirebaseAuth.getInstance().currentUser
    val id = FirebaseAuth.getInstance().currentUser

    fun saveUserInfo(userInfo: User) {
        user?.let { FirebaseUsar ->
            db.collection("users").document(FirebaseUsar.uid).set(userInfo)
                .addOnSuccessListener {
                    Log.d(TAG, "saveUserInfo: $it")
                    Log.d(TAG, "saveUserInfo: $userInfo")
                    Log.d(TAG, "saveUserInfo: ${FirebaseUsar.email}")
                    Log.e(TAG, "saveUserInfo: user id ${FirebaseUsar.uid}")
                }
                .addOnFailureListener { }
        }
    }

    // add workout to list of saved workouts
    fun addUserWorkout(listOfSavedWorkouts: List<WorkoutsInfoUiState>) {
        user?.let {
            db.collection("users").document(it.uid).update("savedWorkouts", listOfSavedWorkouts)
                .addOnSuccessListener {
                    Log.e(TAG, "addUserWorkout: $listOfSavedWorkouts")
                }
        }
    }

    // delete workout from list of saved workouts
    fun deleteWorkout(listOfSavedWorkouts: List<WorkoutsInfoUiState>) {
        user?.let {
            db.collection("users").document(it.uid).update("savedWorkouts", listOfSavedWorkouts)
                .addOnSuccessListener {
                    Log.e(TAG, "addUserWorkout: $listOfSavedWorkouts")
                }
        }
    }

    // to check if original match saved workout list 
    suspend fun isSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState): Boolean {
        val savedWorkoutList = getOldWorkoutList().toMutableList()
        savedWorkoutList.forEach {
            if (it.gifUrl == workoutsInfoUiState.gifUrl) {
                Log.e(TAG, "isSaveWorkout: a return true")
                return true
            }
        }
        Log.e(TAG, "isSaveWorkout: a return false")
        return false
    }

    // To return list of saved workouts
    private suspend fun getOldWorkoutList(): List<WorkoutsInfoUiState> =
        withContext(Dispatchers.IO) {
            var oldList = listOf<WorkoutsInfoUiState>()
            db.collection("users").document(user?.uid!!).get().addOnCompleteListener { task ->
                Log.e(
                    "",
                    "old saved workout ---> ${task.result.toObject(User::class.java)?.savedWorkouts ?: listOf()}"
                )
                oldList = task.result.toObject(User::class.java)?.savedWorkouts ?: listOf()
            }
            oldList
        }

    suspend fun readUserInfo(): Flow<User> = callbackFlow {
        user?.let {
            db.collection("users").document(it.uid).addSnapshotListener { value, error ->
                Log.d(TAG, "readUserInfo: name ${value?.get("name")}- $error")
                Log.d(TAG, "readUserInfo: a ${value?.toObject(User::class.java)}")
                val userInfo = value?.toObject(User::class.java)
                userInfo?.let {
                    trySend(userInfo)
                }
                Log.d(TAG, "readUserInfo: $userInfo")
                Log.e(TAG, "readUserInfo: uid --> ${user.uid}")
            }
        }
        awaitClose { cancel() }
    }

    suspend fun reloadListOfSavedWorkouts(): Flow<List<WorkoutsInfoUiState>> = callbackFlow {
        user?.let {
            db.collection("users").document(it.uid).addSnapshotListener { value, error ->
                Log.e(
                    TAG,
                    "reloadListOfSavedWorkouts: ${value?.toObject(User::class.java)} - $error"
                )
                value?.apply {
                    val workoutsList = value.toObject(User::class.java)?.savedWorkouts
                    if (workoutsList != null) {
                        trySend(workoutsList)
                        Log.e(TAG, "reloadListOfSavedWorkouts trysend: $workoutsList")
                    }
                }
            }
        }
        awaitClose { cancel() }
    }
}
