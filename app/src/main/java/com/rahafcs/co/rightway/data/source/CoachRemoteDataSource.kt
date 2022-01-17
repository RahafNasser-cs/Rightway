package com.rahafcs.co.rightway.data.source

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rahafcs.co.rightway.data.CoachEmail
import com.rahafcs.co.rightway.data.User
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

const val TAG = "CoachRemoteDataSource"

class CoachRemoteDataSource {
    private val db = FirebaseFirestore.getInstance()
    private val collection = "coach"
    // private var listOfCoachesEmail = mutableListOf<String>()

    init {
        // reloadListOfCoachesEmailFromFirestore()
    }

    fun addListOfCoachesEmail(email: String) {
        // listOfCoachesEmail.add(email)
        // Log.e(TAG, "addListOfCoachesEmail: $listOfCoachesEmail")
        // updateListOfCoachesEmail()
    }

    private fun reloadCoachEmailListTest() {
        db.collection("coaches").document("email").addSnapshotListener { value, error ->
            val listOfEmail = value?.toObject(CoachEmail::class.java)!!

            if (listOfEmail.coachesEmail.isNotEmpty()) {
                //  listOfCoachesEmail = listOfEmail.coachesEmail.toMutableList()
            }
        }
    }

    fun removeListOfCoachesEmail(email: String) {
//        if (listOfCoachesEmail.isNotEmpty()) {
//            // listOfCoachesEmail.remove(email)
//            Log.e(TAG, "removeListOfCoachesEmail: $listOfCoachesEmail")
//        }
        // updateListOfCoachesEmail()
    }

//    fun checkIsCoach(email: String) =
//        listOfCoachesEmail.contains(email)

//    private fun updateListOfCoachesEmail() {
//        // update("coachesEmail", listOfCoachesEmail)
//        db.collection("coaches").document("email").set(CoachEmail(listOfCoachesEmail))
//            .addOnSuccessListener {
//                Log.e(TAG, "updateListOfCoachesEmail: $listOfCoachesEmail")
//            }.addOnFailureListener {
//                Log.e(
//                    TAG,
//                    "updateListOfCoachesEmail: addOnFailureListener"
//                )
//            }
//    }

    // not use
    private fun reloadListOfCoachesEmailFromFirestore() {
        db.collection("coaches").document("email").addSnapshotListener { value, error ->
            value?.apply {
                val coachEmail = value.toObject(CoachEmail::class.java)
                coachEmail?.apply {
                    Log.e(TAG, "reloadListOfSavedWorkoutsFromFirestore: coachEmail --> $coachEmail")
                    // listOfCoachesEmail = coachEmail.coachesEmail.toMutableList()
//                    Log.e(
//                        TAG,
//                        "reloadListOfSavedWorkoutsFromFirestore: listOfCoachesEmail--> $listOfCoachesEmail"
//                    )
                }
            }
        }
    }

    // not use
//    suspend fun reloadCoachEmailList(): Flow<List<String>> = callbackFlow {
//        db.collection("coaches").document("email").addSnapshotListener { value, error ->
//            Log.e(TAG, "reloadCoachEmailList: value $value -- error $error")
//            value?.let {
//                val listOfEmail = value.toObject(CoachEmail::class.java)
//                listOfEmail?.let {
//                    trySend(listOfEmail.coachesEmail)
//                }
//            }
//            // trySend(listOf<String>())
//        }
//        awaitClose { close() }
//    }

    // not use
//    fun saveCoachInfo(coach: Coach) {
//        db.collection(collection)
//            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
//            .set(coach)
//            .addOnSuccessListener { Log.e(TAG, "saveCoachInfo: $coach") }
//    }

    fun saveCoachInfo(userInfo: User) {
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

    // not use
//    fun readCoachesInfo(): Flow<List<Coach>> = callbackFlow {
//        db.collection(collection).get().addOnCompleteListener {
//            if (it.isSuccessful) {
//                val coachesList = mutableListOf<Coach>()
//                for (coach in it.result.documents) {
//                    coachesList.add(coach.toObject(Coach::class.java)!!)
//                }
//                Log.e(TAG, "readCoachesInfo: addOnCompleteListener $coachesList")
//                trySend(coachesList.toList())
//            }
//        }.addOnFailureListener {
//            trySend(mutableListOf())
//            Log.e(TAG, "readCoachesInfo: in addOnFailureListener")
//        }
//        awaitClose { cancel() }
//    }

//    fun readCoachInfo(): Flow<Coach> = callbackFlow {
//        db.collection(collection).document(FirebaseAuth.getInstance().currentUser?.uid!!)
//            .addSnapshotListener { value, error ->
//                Log.e(TAG, "readCoachInfo: value-> $value error-> $error")
//                value?.let { it ->
//                    val result = it.toObject(Coach::class.java)
//                    result?.let { coach ->
//                        trySend(coach)
//                    }
//                }
//            }
//        awaitClose { cancel() }
//    }

    suspend fun getCoachList(): Flow<List<User>> = callbackFlow {
        Log.e(TAG, "getTrainer: inter")
        db.collection("users").whereEqualTo("subscriptionStatus", "Trainer").get()
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

    suspend fun readCoachInfo(): Flow<User> = callbackFlow {
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
}
