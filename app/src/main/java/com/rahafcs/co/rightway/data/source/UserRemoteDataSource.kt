package com.rahafcs.co.rightway.data.source

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rahafcs.co.rightway.data.User
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserRemoteDataSource {
    private val db = FirebaseFirestore.getInstance()
    private val TAG = "UserRemoteDataSource"
    val id = FirebaseAuth.getInstance().currentUser

    fun saveUserInfo(userInfo: User) {
        db.collection("users").document(userInfo.firstName).collection("info").add(userInfo)
            .addOnSuccessListener { Log.d(TAG, "saveUserInfo: $userInfo") }
            .addOnFailureListener { }
    }

    fun addUserWorkout(workoutsInfoUiState: WorkoutsInfoUiState, userName: String) {
        db.collection("users").document(userName).collection("listWorkout").add(workoutsInfoUiState)
            .addOnSuccessListener {
                Log.d(
                    TAG,
                    "addListWorkout: $workoutsInfoUiState"
                )
            }
    }

    fun readUserInfo(userName: String): Flow<User> = callbackFlow {
        db.collection("users").document(userName).collection("info").document(
            "\n" +
                "dSz1cbS8qD3nNiUkE1cn"
        ).get()
            .addOnSuccessListener {
                // val user = it.toObjects(User::class.java)
                it?.let {
                    Log.d(TAG, "readUserInfo: $it")
                }
                Log.d(TAG, "readUserInfo: error ")
                // offer(user)
            }.addOnFailureListener { Log.d(TAG, "readUserInfo: ${it.message}") }.addOnCompleteListener {
                it.let { Log.d(TAG, "readUserInfo: ${it.result.data}") }
            }
    }
}
