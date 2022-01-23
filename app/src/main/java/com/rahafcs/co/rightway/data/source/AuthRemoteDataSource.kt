package com.rahafcs.co.rightway.data.source

import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.rahafcs.co.rightway.utility.ServiceLocator
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthRemoteDataSource {

    // Sign in with email and password.
    fun signInWithEmailAndPassword(email: String, password: String): Flow<Any> = callbackFlow {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(it)
                }
            }.addOnFailureListener {
                trySend(it.message!!)
            }
        awaitClose { cancel() }
    }

    // Sign out.
    fun signOut(): Flow<Any> = callbackFlow {
        AuthUI.getInstance()
            .signOut(ServiceLocator.ProgramListService.application).addOnCompleteListener {
                if (it.isSuccessful) {
                    FirebaseAuth.getInstance().signOut()
                    trySend(it)
                }
            }.addOnFailureListener {
                trySend(it)
            }
        awaitClose { cancel() }
    }

    // Register with email and password.
    fun registerWithEmailAndPassword(email: String, password: String): Flow<Any> = callbackFlow {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(it)
                }
            }.addOnFailureListener {
                trySend(it)
            }
        awaitClose { cancel() }
    }

    // Register with google.
    fun registerWithGoogleAuthFirebase(account: GoogleSignInAccount): Flow<Any> = callbackFlow {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credentials).addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(it)
            }
        }.addOnFailureListener {
            trySend(it)
        }
        awaitClose { cancel() }
    }
}
