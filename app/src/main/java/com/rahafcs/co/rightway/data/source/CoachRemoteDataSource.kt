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
    private lateinit var listOfCoachesEmail: MutableList<String>

    fun addListOfCoachesEmail(email: String) {
        listOfCoachesEmail.add(email)
        Log.e(TAG, "addListOfCoachesEmail: $listOfCoachesEmail")
        updateListOfCoachesEmail()
    }

    fun removeListOfCoachesEmail(email: String) {
        if (listOfCoachesEmail.isNotEmpty()) {
            listOfCoachesEmail.remove(email)
            Log.e(TAG, "removeListOfCoachesEmail: $listOfCoachesEmail")
        }
        updateListOfCoachesEmail()
    }

    fun checkIsCoach(email: String) =
        listOfCoachesEmail.contains(email)

    fun updateListOfCoachesEmail() {
        FirebaseAuth.getInstance().currentUser?.let {
            db.collection("coachesEmail").add(listOfCoachesEmail)
                .addOnSuccessListener {
                    Log.e(TAG, "updateListOfCoachesEmail: $listOfCoachesEmail")
                }
        }
    }

    fun reloadCoachEmailList(): Flow<List<String>> = callbackFlow {
        db.collection("coachesEmail").addSnapshotListener { value, error ->
            val listOfEmail = mutableListOf<String>()
            for (email in value?.documents!!) {
                listOfEmail.add(email.toString())
            }
            trySend(listOfEmail)
        }
        awaitClose { close() }
    }

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

    fun readCoachInfo(): Flow<Coach> = callbackFlow {
        db.collection(collection).document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .addSnapshotListener { value, error ->
                Log.e(TAG, "readCoachInfo: value-> $value error-> $error")
                value?.let { it ->
                    val result = it.toObject(Coach::class.java)
                    result?.let { coach ->
                        trySend(coach)
                    }
                }
            }
        awaitClose { cancel() }
    }
}
