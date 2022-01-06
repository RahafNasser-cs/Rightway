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
        user?.let {
            db.collection("users").document(it.uid).set(userInfo)
                .addOnSuccessListener {
                    Log.d(TAG, "saveUserInfo: $userInfo")
                }
                .addOnFailureListener { }
        }
    }

    // add workout to list of saved workouts
    suspend fun addUserWorkout(workoutsInfoUiState: WorkoutsInfoUiState) {
        if (isSavedWorkout(workoutsInfoUiState)) { // check if already exit
            return
        }
        val savedWorkoutList = getOldWorkoutList().toMutableList()
        savedWorkoutList.add(workoutsInfoUiState)
        user?.let {
            db.collection("users").document(it.uid).update("savedWorkouts", savedWorkoutList)
                .addOnSuccessListener {
                    Log.e(TAG, "addUserWorkout: $savedWorkoutList")
                }
        }
    }

    // delete workout from list of saved workouts
    suspend fun deleteWorkout(workoutsInfoUiState: WorkoutsInfoUiState) {
        val savedWorkoutList = getOldWorkoutList().toMutableList()
        Log.e(TAG, "deleteWorkout: savedWorkoutList $savedWorkoutList")
        if (savedWorkoutList.contains(workoutsInfoUiState)) {
            savedWorkoutList.remove(workoutsInfoUiState)
            Log.e(TAG, "deleteWorkout: $workoutsInfoUiState")
        }
        user?.let {
            db.collection("users").document(it.uid).update("savedWorkouts", savedWorkoutList)
                .addOnSuccessListener {
                    Log.e(TAG, "addUserWorkout: $savedWorkoutList")
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
    suspend fun getOldWorkoutList(): List<WorkoutsInfoUiState> = withContext(Dispatchers.IO) {
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
        val firebaseDb = FirebaseFirestore.getInstance()
        val direc = firebaseDb.collection("users").document(
            user?.uid!!
        )
        direc.addSnapshotListener { value, error ->
            Log.d(TAG, "readUserInfo: $value- $error")
            // for (item in value?.documents!!) {
            Log.d(TAG, "readUserInfo: a ${value?.toObject(User::class.java)}")
            val userInfo = value?.toObject(User::class.java)!!
            trySend(userInfo)
            Log.d(TAG, "readUserInfo: $userInfo")
            // }
        }

        awaitClose { cancel() }
    }
}
