package com.rahafcs.co.rightway.data.source

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.rahafcs.co.rightway.data.User

class UserRemoteDataSource {
    private val db = FirebaseFirestore.getInstance()

    fun saveUserInfo(userInfo: User) {
        db.collection("users").add(userInfo)
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }
    fun readUserInfo() {
        db.collection("users").get().addOnSuccessListener { Log.d("TAG", "readUserInfo: ${it.metadata} ") }
    }
}
