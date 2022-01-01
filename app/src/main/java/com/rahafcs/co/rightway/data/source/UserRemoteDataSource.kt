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
            db.collection("users").document(userInfo.firstName).collection("info").add(userInfo)
                .addOnSuccessListener {
                    Log.d(TAG, "saveUserInfo: $userInfo")
                }
                .addOnFailureListener { }
        }
    }

    fun addUserWorkout(workoutsInfoUiState: WorkoutsInfoUiState, userName: String) {
        user?.let {
            db.collection("users").document(it.uid).collection("listWorkout")
                .add(workoutsInfoUiState)
                .addOnSuccessListener {
                    Log.d(
                        TAG,
                        "addListWorkout: $workoutsInfoUiState"
                    )
                }
        }
    }

    suspend fun readUserInfo(userName: String): Flow<User> = callbackFlow {
        val firebaseDb = FirebaseFirestore.getInstance()
        val direc = firebaseDb.collection("users").document(
            userName
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
