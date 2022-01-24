package com.rahafcs.co.rightway.ui.auth

import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.rahafcs.co.rightway.data.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun signInWithGoogle() {}

    // Sign in with email and password.
    fun signInWithEmailAndPassword(email: String, password: String) =
        authRepository.signInWithEmailAndPassword(email, password)

    // Sign out.
    fun signOut() = authRepository.signOut()

    // Register with email and password.
    fun registerWithEmailAndPassword(email: String, password: String) =
        authRepository.registerWithEmailAndPassword(email, password)

    // Sign in with google.
    fun signInWithGoogleAuthFirebase(account: GoogleSignInAccount) =
        authRepository.signInWithGoogleAuthFirebase(account)
}
