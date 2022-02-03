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

// Implementation of a user(trainer "coach" or trainee) data source as db in Firestore.

class UserRemoteDataSource : UserDataSource {
    private val db = FirebaseFirestore.getInstance()
    private val TAG = "UserRemoteDataSource"
    private val collection = "users"
    private lateinit var listOfSavedWorkouts: MutableList<WorkoutsInfoUiState>

    init {
        reloadListOfSavedWorkoutsToUpdateLocalList()
    }

    // To save user info
    override fun saveUserInfo(user: User) {
        FirebaseAuth.getInstance().currentUser?.let { firebaseUser ->
            db.collection(collection).document(firebaseUser.uid).set(user)
                .addOnSuccessListener {
                    Log.e(TAG, "saveUserInfo: uid -- ${firebaseUser.uid}",)
                }
                .addOnFailureListener {}
        }
    }

    // To get user info
    override suspend fun readUserInfo(): Flow<User> = callbackFlow {
        FirebaseAuth.getInstance().currentUser?.let { firebaseUser ->
            db.collection(collection).document(firebaseUser.uid).addSnapshotListener { value, error ->
                val userInfo = value?.toObject(User::class.java)
                userInfo?.let {
                    Log.e(TAG, "readUserInfo: uid -- ${firebaseUser.uid}", )
                    trySend(userInfo)
                }
            }
        }
        awaitClose { cancel() }
    }

    // To get user type --> Trainer "Coach" or Trainee
    override fun getUserType(): Flow<String> = callbackFlow {
        db.collection(collection).document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .addSnapshotListener { value, error ->
                value?.let { it ->
                    val userStatus = it.toObject(User::class.java)?.subscriptionStatus
                    userStatus?.let { userType ->
                        trySend(userType)
                    }
                }
            }
        awaitClose { cancel() }
    }

    // To add a new workout to local list of saved workouts
    override fun addListOfSavedWorkoutsLocal(workoutsInfoUiState: WorkoutsInfoUiState) {
        listOfSavedWorkouts.add(workoutsInfoUiState)
        updateListOfSavedWorkouts()
    }

    // To remove workout from local list of saved workouts
    override fun removeListOfSavedWorkoutsLocal(workoutsInfoUiState: WorkoutsInfoUiState) {
        if (listOfSavedWorkouts.isNotEmpty()) {
            listOfSavedWorkouts.remove(workoutsInfoUiState)
            updateListOfSavedWorkouts()
        }
    }

    // Update list of saved workouts in Firestore
    override fun updateListOfSavedWorkouts() {
        FirebaseAuth.getInstance().currentUser?.let {
            db.collection(collection).document(it.uid).update("savedWorkouts", listOfSavedWorkouts)
                .addOnSuccessListener {}
                .addOnFailureListener {}
        }
    }

    // Check if  workoutsInfoUiState exists in list of saved workouts.
    override fun checkIsSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState) =
        listOfSavedWorkouts.contains(workoutsInfoUiState)

    // To get list of saved workout from Firestore
    override suspend fun reloadListOfSavedWorkouts(): Flow<List<WorkoutsInfoUiState>> =
        callbackFlow {
            FirebaseAuth.getInstance().currentUser?.let {
                db.collection(collection).document(it.uid).addSnapshotListener { value, error ->
                    value?.apply {
                        val workoutsList = value.toObject(User::class.java)?.savedWorkouts
                        if (workoutsList != null) {
                            trySend(workoutsList)
                            listOfSavedWorkouts = workoutsList.toMutableList()
                        }
                    }
                }
            }
            awaitClose { cancel() }
        }

    // To get list of coaches from Firestore
    override suspend fun getCoachList(): Flow<List<User>> = callbackFlow {
        db.collection(collection).whereEqualTo("subscriptionStatus", "Trainer").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val listOfTrainee = mutableListOf<User>()
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

    // To update local list of saved workouts --> listOfSavedWorkouts
    private fun reloadListOfSavedWorkoutsToUpdateLocalList() {
        FirebaseAuth.getInstance().currentUser?.let {
            db.collection(collection).document(it.uid).addSnapshotListener { value, error ->
                value?.apply {
                    val workoutsList = value.toObject(User::class.java)?.savedWorkouts
                    if (workoutsList != null) {
                        listOfSavedWorkouts = workoutsList.toMutableList()
                    }
                }
            }
        }
    }
}
