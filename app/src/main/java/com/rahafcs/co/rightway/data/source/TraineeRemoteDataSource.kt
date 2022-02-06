package com.rahafcs.co.rightway.data.source

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rahafcs.co.rightway.data.User
import com.rahafcs.co.rightway.ui.workout.WorkoutsInfoUiState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

// // Implementation of a trainee data source as db in Firestore.

class TraineeRemoteDataSource : UserDataSource {
    private val db = FirebaseFirestore.getInstance()
    private val TAG = "TraineeRemoteDataSource"
    private val collection = "users"
    private lateinit var listOfSavedWorkouts: MutableList<WorkoutsInfoUiState>

    init {
        reloadListOfSavedWorkoutsToUpdateLocalList()
    }

    // To save trainee info
    override fun saveUserInfo(userInfo: User) {
        FirebaseAuth.getInstance().currentUser?.let { firebaseUser ->
            db.collection(collection).document(firebaseUser.uid).set(userInfo)
                .addOnSuccessListener {}
                .addOnFailureListener {}
        }
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

    // Check if workoutsInfoUiState is exit in list of saved workouts
    override fun checkIsSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState) =
        listOfSavedWorkouts.contains(workoutsInfoUiState)

    // To get trainee info 
    override suspend fun readUserInfo(): Flow<User> = callbackFlow {
        FirebaseAuth.getInstance().currentUser?.let {
            db.collection(collection).document(it.uid).addSnapshotListener { value, error ->
                val userInfo = value?.toObject(User::class.java)
                userInfo?.let {
                    trySend(userInfo)
                }
            }
        }
        awaitClose { cancel() }
    }

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

    override suspend fun getCoachList(): Flow<List<User>> {
        TODO("Not yet implemented")
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
}
