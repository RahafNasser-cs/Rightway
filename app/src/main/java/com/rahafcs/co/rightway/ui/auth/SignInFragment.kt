package com.rahafcs.co.rightway.ui.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentSignInBinding
import com.rahafcs.co.rightway.ui.auth.SignUpFragment.Companion.USERID
import com.rahafcs.co.rightway.utility.toast
import com.rahafcs.co.rightway.utility.upToTop

class SignInFragment : Fragment() {
    var binding: FragmentSignInBinding? = null
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Sign in"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            signInBtn.setOnClickListener { signInWithEmailAndPassword() }
            backArrow.setOnClickListener { this@SignInFragment.upToTop() }
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
                requireContext().toast("hello ${it.result?.user?.email}")
            }
        }.addOnFailureListener {
            requireContext().toast("${it.message}")
        }
    }

    fun signInWithEmailAndPassword() {
        if (!isValidEmail()) {
            requireContext().toast("Enter a valid email")
        } else if (!isValidPassword()) {
            requireContext().toast("Enter a valid password")
        } else {
            binding?.signInBtn?.isEnabled = false
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
            }.addOnFailureListener {
                requireContext().toast("${it.message}")
                binding?.signInBtn?.isEnabled = true
            }
    }

    private fun signInOnSuccess(it: Task<AuthResult>) {
        val firebaseUser = it.result.user
        firebaseUser?.let {
            addToSharedPreference(it.uid)
            goToHomePage()
        }
        requireContext().toast("Hello ${firebaseUser?.email}")
    }

    private fun goToHomePage() {
        findNavController().navigate(R.id.action_signInFragment_to_viewPagerFragment2)
    }

    private fun addToSharedPreference(userId: String) {
        sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        val editor = sharedPreferences.edit()
        editor.apply {
            putString(USERID, userId)
            apply()
        }
    }

    private fun isValidEmail(): Boolean {
        return binding?.passwordEditText?.text.toString().isNotEmpty()
    }

    private fun isValidPassword(): Boolean {
        return binding?.emailEditText?.text.toString().isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
