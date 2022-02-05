package com.rahafcs.co.rightway.ui.auth

import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.rahafcs.co.rightway.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
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

    // Provide google sign in client.
    fun googleSignInClient(): GoogleSignInClient = authRepository.googleSignInClient()
}
