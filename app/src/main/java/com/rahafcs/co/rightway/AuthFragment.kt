package com.rahafcs.co.rightway

import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.rahafcs.co.rightway.databinding.FragmentAuthBinding

class AuthFragment : Fragment() {
    lateinit var binding: FragmentAuthBinding

    fun goToSignInPage() {
        // TODO
    }

    private fun signUpWithEmailAndPassword(task: Task<AuthResult>) {
        val firebaseUser = task.result?.user
        // save user info 
        // TODO
    }

    fun goToGetStartedPage() {
        // TODO
    }

    fun signUpWithGoogle() {
        // TODO
    }

    fun register() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            binding.emailEditText.text.toString(),
            binding.passwordEditText.text.toString()
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                signUpWithEmailAndPassword(it)
            } else {
                // error 
                // TODO
            }
        }
    }

    fun isValidFirstName(): Boolean {
        return binding.firstNameEditText.text.toString().isNotEmpty()
    }

    fun isValidLastName(): Boolean {
        return binding.lastNameEditText.text.toString().isNotEmpty()
    }

    fun isValidEmail(): Boolean {
        val email = binding.emailEditText.text.toString().trim()
        return if (email.isEmpty()) {
            false
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    fun isValidPassword(): Boolean {
        return binding.passwordEditText.text.toString().isNotEmpty()
    }
}
