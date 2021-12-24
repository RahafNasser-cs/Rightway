package com.rahafcs.co.rightway.data.source

import com.google.firebase.firestore.FirebaseFirestore
import com.rahafcs.co.rightway.data.User

class UserRemoteDataSource {
    private val db = FirebaseFirestore.getInstance()

    fun saveUserInfo(userInfo: User) {
        db.collection("users").add(userInfo)
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }
}
