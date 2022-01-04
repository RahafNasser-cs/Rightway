package com.rahafcs.co.rightway.data.source

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rahafcs.co.rightway.data.User
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

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

    fun addUserWorkout(workoutsInfoUiState: List<WorkoutsInfoUiState>) {
        // val savedWorkoutsInfoUiState = workoutsInfoUiState.copy(isSaved = true)
        user?.let {
            db.collection("users").document(it.uid).update("savedWorkouts", workoutsInfoUiState)
            Log.e(TAG, "addUserWorkout: $workoutsInfoUiState")
            getOldWorkoutList()

//            val test = db.collection("").whereEqualTo("name", workoutsInfoUiState[0].name)
//            db.collection("").whereEqualTo("","").get().addOnSuccessListener { docs ->
//                for (domc in docs) {
//
//                }
//            }
        }
    }

    fun getOldWorkoutList(): List<WorkoutsInfoUiState> {
        var oldList = listOf<WorkoutsInfoUiState>()
//        db.collection("users").whereEqualTo("","").get().addOnSuccessListener { docs ->
//            for (domc in docs) {
//
//            }
//        }

        db.collection("users").document(user?.uid!!).get().addOnSuccessListener { docs ->
            oldList = docs.toObject(User::class.java)?.savedWorkouts ?: listOf()
            Log.e("", "old saved workout ---> $oldList")
        }
        return oldList
    }

//    fun updateListWorkout(workoutsInfoUiState: WorkoutsInfoUiState): List<WorkoutsInfoUiState> {
//        var savedListWorkouts = listOf<WorkoutsInfoUiState>()
//        user?.uid?.let {
//            db.collection("users").document(it).addSnapshotListener { value, error ->
//                savedListWorkouts = value?.get("llll") as ArrayList<WorkoutsInfoUiState>
//            }
//        }
//        return savedListWorkouts
//    }

    suspend fun readUserInfo(): Flow<User> = callbackFlow {
        val firebaseDb = FirebaseFirestore.getInstance()
        val direc = firebaseDb.collection("users").document(
            user?.uid!!
        ).collection("info")
        direc.addSnapshotListener { value, error ->
            Log.d(TAG, "readUserInfo: ${value?.documents}- $error")
            for (item in value?.documents!!) {
                Log.d(TAG, "readUserInfo: a ${item.toObject(User::class.java)}")
                val userInfo = item?.toObject(User::class.java)!!
                trySend(userInfo)
                Log.d(TAG, "readUserInfo: $userInfo")
            }
        }

        awaitClose { cancel() }
    }
}
