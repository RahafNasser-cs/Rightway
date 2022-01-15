package com.rahafcs.co.rightway.data.source

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rahafcs.co.rightway.data.Coach
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

const val TAG = "CoachRemoteDataSource"

class CoachRemoteDataSource {
    private val db = FirebaseFirestore.getInstance()
    private val collection = "coach"

    fun saveCoachInfo(coach: Coach) {
        db.collection(collection)
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .set(coach)
            .addOnSuccessListener { Log.e(TAG, "saveCoachInfo: $coach") }
    }

    fun readCoachesInfo(): Flow<List<Coach>> = callbackFlow {
        db.collection(collection).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val coachesList = mutableListOf<Coach>()
                for (coach in it.result.documents) {
                    coachesList.add(coach.toObject(Coach::class.java)!!)
                }
                Log.e(TAG, "readCoachesInfo: addOnCompleteListener $coachesList")
                trySend(coachesList.toList())
            }
        }.addOnFailureListener {
            trySend(mutableListOf())
            Log.e(TAG, "readCoachesInfo: in addOnFailureListener")
        }
         awaitClose { cancel() }
    }
}
