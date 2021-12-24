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
import com.rahafcs.co.rightway.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {
    var binding: FragmentSignInBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Sign in"
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            signInFragment = this@SignInFragment
        }
    }

    fun signInWithGoogle() {
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
                message("hello ${it.result?.user?.email}")
            }
        }.addOnFailureListener {
            message("${it.message}")
        }
    }

    fun signInWithEmailAndPassword() {
        if (!isValidEmail()) {
            message("Enter a valid email")
        } else if (!isValidPassword()) {
            message("Enter a valid password")
        } else {
            signIn()
        }
    }

    private fun signIn() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            binding?.emailEditText?.text.toString(),
            binding?.passwordEditText?.text.toString()
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    signInOnSuccess(it)
                }
            }.addOnFailureListener { message("${it.message}") }
    }

    private fun signInOnSuccess(it: Task<AuthResult>) {
        val firebaseUser = it.result.user
        message("Hello ${firebaseUser?.email}")
    }

    private fun isValidEmail(): Boolean {
        return binding?.passwordEditText?.text.toString().isNotEmpty()
    }

    private fun isValidPassword(): Boolean {
        return binding?.emailEditText?.text.toString().isNotEmpty()
    }

    private fun message(str: String) {
        Toast.makeText(requireContext(), str, Toast.LENGTH_SHORT).show()
    }
}
