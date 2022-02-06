package com.rahafcs.co.rightway.data.source

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rahafcs.co.rightway.data.User
import com.rahafcs.co.rightway.ui.workout.WorkoutsInfoUiState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

// Implementation of a trainer "coach" data source as db in Firestore.

const val TAG = "CoachRemoteDataSource"

class CoachRemoteDataSource : UserDataSource {
    private val db = FirebaseFirestore.getInstance()
    private val collection = "users"

    // To save coach info
    override fun saveUserInfo(user: User) {
        FirebaseAuth.getInstance().currentUser?.let { firebaseUser ->
            db.collection(collection).document(firebaseUser.uid).set(user)
                .addOnSuccessListener {}
                .addOnFailureListener {}
        }
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

    // To get coach info
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

    override fun addListOfSavedWorkoutsLocal(workoutsInfoUiState: WorkoutsInfoUiState) {
        TODO("Not yet implemented")
    }

    override fun removeListOfSavedWorkoutsLocal(workoutsInfoUiState: WorkoutsInfoUiState) {
        TODO("Not yet implemented")
    }

    override fun updateListOfSavedWorkouts() {
        TODO("Not yet implemented")
    }

    override fun checkIsSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun reloadListOfSavedWorkouts(): Flow<List<WorkoutsInfoUiState>> {
        TODO("Not yet implemented")
    }
}
