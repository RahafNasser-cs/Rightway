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
    private lateinit var listOfSavedWorkouts: MutableList<WorkoutsInfoUiState>

    init {
        reloadListOfSavedWorkoutsFromFirestore()
    }

    fun saveUserInfo(userInfo: User) {
        FirebaseAuth.getInstance().currentUser?.let { FirebaseUsar ->
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

    fun addListOfSavedWorkoutsLocal(workoutsInfoUiState: WorkoutsInfoUiState) {
        listOfSavedWorkouts.add(workoutsInfoUiState)
        Log.e(TAG, "addListOfSavedWorkoutsLocal: $listOfSavedWorkouts")
        updateListOfSavedWorkouts()
    }

    fun removeListOfSavedWorkoutsLocal(workoutsInfoUiState: WorkoutsInfoUiState) {
        if (listOfSavedWorkouts.isNotEmpty()) {
            listOfSavedWorkouts.remove(workoutsInfoUiState)
            Log.e(TAG, "removeListOfSavedWorkoutsLocal: : $listOfSavedWorkouts")
            updateListOfSavedWorkouts()
        }
    }

    private fun updateListOfSavedWorkouts() {
        FirebaseAuth.getInstance().currentUser?.let {
            db.collection("users").document(it.uid).update("savedWorkouts", listOfSavedWorkouts)
                .addOnSuccessListener {
                    Log.e(TAG, "addUserWorkout: $listOfSavedWorkouts")
                }
        }
    }

    fun checkIsSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState) =
        listOfSavedWorkouts.contains(workoutsInfoUiState)

    // add workout to list of saved workouts
    fun addUserWorkout(listOfSavedWorkouts: List<WorkoutsInfoUiState>) {
        FirebaseAuth.getInstance().currentUser?.let {
            db.collection("users").document(it.uid).update("savedWorkouts", listOfSavedWorkouts)
                .addOnSuccessListener {
                    Log.e(TAG, "addUserWorkout: $listOfSavedWorkouts")
                }
        }
    }

    // delete workout from list of saved workouts
    fun deleteWorkout(listOfSavedWorkouts: List<WorkoutsInfoUiState>) {
        FirebaseAuth.getInstance().currentUser?.let {
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
        FirebaseAuth.getInstance().currentUser?.let {
            db.collection("users").document(it.uid).addSnapshotListener { value, error ->
                Log.d(TAG, "readUserInfo: name ${value?.get("name")}- $error")
                Log.d(TAG, "readUserInfo: a ${value?.toObject(User::class.java)}")
                val userInfo = value?.toObject(User::class.java)
                userInfo?.let {
                    trySend(userInfo)
                }
                Log.d(TAG, "readUserInfo: $userInfo")
                Log.e(TAG, "readUserInfo: uid --> ${it.uid}")
            }
        }
        awaitClose { cancel() }
    }

    suspend fun reloadListOfSavedWorkouts(): Flow<List<WorkoutsInfoUiState>> = callbackFlow {
        FirebaseAuth.getInstance().currentUser?.let {
            db.collection("users").document(it.uid).addSnapshotListener { value, error ->
                Log.e(
                    TAG,
                    "reloadListOfSavedWorkouts: ${value?.toObject(User::class.java)} - $error"
                )
                value?.apply {
                    val workoutsList = value.toObject(User::class.java)?.savedWorkouts
                    if (workoutsList != null) {
                        trySend(workoutsList)
                        listOfSavedWorkouts = workoutsList.toMutableList()
                        Log.e(TAG, "reloadListOfSavedWorkouts trysend: $workoutsList")
                    }
                }
            }
        }
        awaitClose { cancel() }
    }

    private fun reloadListOfSavedWorkoutsFromFirestore() {
        FirebaseAuth.getInstance().currentUser?.let {
            db.collection("users").document(it.uid).addSnapshotListener { value, error ->
                Log.e(
                    TAG,
                    "reloadListOfSavedWorkoutsFromFirestore: ${value?.toObject(User::class.java)} - $error"
                )
                value?.apply {
                    val workoutsList = value.toObject(User::class.java)?.savedWorkouts
                    if (workoutsList != null) {
                        listOfSavedWorkouts = workoutsList.toMutableList()
                        Log.e(
                            TAG,
                            "reloadListOfSavedWorkoutsFromFirestore workoutsList: $workoutsList"
                        )
                    }
                }
            }
        }
    }

    suspend fun getTrainer(): Flow<List<User>> = callbackFlow {
        Log.e(TAG, "getTrainer: inter")
        db.collection("users").whereEqualTo("subscriptionStatus", "Trainee").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.e(TAG, "getTrainer: a value ${it.result.documents}")
                    var listOfTrainee = mutableListOf<User>()
                    it.result?.let {
                        for (trainee in it.documents) {
                            listOfTrainee.add(trainee.toObject(User::class.java)!!)
                        }
                        trySend(listOfTrainee)
                    }
                }
            }
        awaitClose { cancel() }
    }

    fun getUserStatus(): Flow<String> = callbackFlow {
        db.collection("users").document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .addSnapshotListener { value, error ->
                Log.e(TAG, "getUserStatus: a value $value -- error $error")
                value?.let { it ->
                    val userStatus = it.toObject(User::class.java)?.subscriptionStatus
                    userStatus?.let { userStatus ->
                        Log.e(TAG, "getUserStatus: $userStatus")
                        trySend(userStatus)
                    }
                }
            }
        awaitClose { cancel() }
    }
}
