package com.rahafcs.co.rightway.data

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.rahafcs.co.rightway.data.source.AuthRemoteDataSource

class AuthRepository(private val authRemoteDataSource: AuthRemoteDataSource) {
    // Sign in with email and password.
    fun signInWithEmailAndPassword(email: String, password: String) =
        authRemoteDataSource.signInWithEmailAndPassword(email, password)

    // Sign out.
    fun signOut() = authRemoteDataSource.signOut()

    // Register with email and password.
    fun registerWithEmailAndPassword(email: String, password: String) =
        authRemoteDataSource.registerWithEmailAndPassword(email, password)

    // Register with google.
    fun registerWithGoogleAuthFirebase(account: GoogleSignInAccount) =
        authRemoteDataSource.registerWithGoogleAuthFirebase(account)
}
