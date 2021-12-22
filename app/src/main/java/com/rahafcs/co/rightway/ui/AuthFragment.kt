package com.rahafcs.co.rightway.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentAuthBinding

const val REQUEST_CODE_SIGNING = 0

class AuthFragment : Fragment() {
    private var binding: FragmentAuthBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            authFragment = this@AuthFragment
        }
        (activity as AppCompatActivity).supportActionBar?.title = "Registration"
    }

    fun goToSignInPage() {
        Toast.makeText(requireContext(), "sign in page", Toast.LENGTH_SHORT).show()
        // TODO
    }

    private fun signUpWithEmailAndPassword(task: Task<AuthResult>) {
        val firebaseUser = task.result?.user
        Toast.makeText(requireContext(), "hello ${firebaseUser?.email}", Toast.LENGTH_SHORT).show()
        // save user info 
        // TODO
    }

    fun goToGetStartedPage() {
        // TODO
    }

    fun signUpWithGoogle() {
        val options = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(requireContext(), options)
        googleSignInClient.signInIntent.also {
            startActivityForResult(it, REQUEST_CODE_SIGNING)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SIGNING) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            account?.let { googleAuthFirebase(it) }
        }
    }

    private fun googleAuthFirebase(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credentials).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(
                    requireContext(),
                    "hello ${it.result?.user?.email}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }.addOnFailureListener {
            Toast.makeText(
                requireContext(),
                "${it.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun register() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            binding?.emailEditText?.text.toString(),
            binding?.passwordEditText?.text.toString()
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                signUpWithEmailAndPassword(it)
            } else {
                // error 
                Toast.makeText(requireContext(), "Error, try again", Toast.LENGTH_SHORT).show()
                // TODO
            }
        }
    }

    fun registration() {
        if (!isValidFirstName()) {
            Toast.makeText(requireContext(), "Enter a first name", Toast.LENGTH_SHORT).show()
        } else if (!isValidLastName()) {
            Toast.makeText(requireContext(), "Enter a last name", Toast.LENGTH_SHORT).show()
        } else if (!isValidEmail()) {
            Toast.makeText(requireContext(), "Enter a valid email", Toast.LENGTH_SHORT).show()
        } else if (!isValidPassword()) {
            Toast.makeText(requireContext(), "Enter a password", Toast.LENGTH_SHORT).show()
        } else {
            register()
        }
    }

    private fun isValidFirstName(): Boolean {
        return binding?.firstNameEditText?.text.toString().isNotEmpty()
    }

    private fun isValidLastName(): Boolean {
        return binding?.lastNameEditText?.text.toString().isNotEmpty()
    }

    private fun isValidEmail(): Boolean {
        val email = binding?.emailEditText?.text.toString().trim()
        return if (email.isEmpty()) {
            false
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    private fun isValidPassword(): Boolean {
        return binding?.passwordEditText?.text.toString().isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
