package com.rahafcs.co.rightway.ui.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import com.rahafcs.co.rightway.data.SubscriptionStatus
import com.rahafcs.co.rightway.databinding.FragmentSignUpBinding
import com.rahafcs.co.rightway.utility.Constant.FIRST_NAME
import com.rahafcs.co.rightway.utility.Constant.LAST_NAME
import com.rahafcs.co.rightway.utility.Constant.SIGN_IN
import com.rahafcs.co.rightway.utility.Constant.SIGN_UP
import com.rahafcs.co.rightway.utility.Constant.SUPERSCRIPTION
import com.rahafcs.co.rightway.utility.Constant.USERID
import com.rahafcs.co.rightway.utility.toast
import com.rahafcs.co.rightway.utility.upToTop

const val REQUEST_CODE_SIGNING = 0

class SignUpFragment : Fragment() {
    private var binding: FragmentSignUpBinding? = null
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Registration"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            signUpWithEmailPasswordBtn.setOnClickListener { registration() }
            signInLinkBtn.setOnClickListener { goToSignInPage() }
            signUpWithGoogleBtn.setOnClickListener { signUpWithGoogle() }
            backArrow.setOnClickListener { this@SignUpFragment.upToTop() }
        }
    }

    private fun getUserInfo(userId: String) {
        var firstName = binding?.firstNameEditText?.text.toString()
        if (firstName.isEmpty()) { // if signup with google 
            firstName = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        }
        val lastName = binding?.lastNameEditText?.text.toString()
        val subscriptionStatus = if (binding?.trainee?.isChecked == true) {
            SubscriptionStatus.TRAINEE.toString()
        } else {
            Log.e(
                "SignUpFragment",
                "getUserInfo: add email ${FirebaseAuth.getInstance().currentUser?.email!!}",
            )
            SubscriptionStatus.TRAINER.toString()
        }
        // createUserInfo(userId, firstName, lastName, subscriptionStatus)
        addToSharedPreference(userId, firstName, lastName, subscriptionStatus)
    }

    private fun addToSharedPreference(
        userId: String,
        firstName: String,
        lastName: String,
        subscriptionStatus: String,
    ) {
        sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        sharedPreferences.edit().apply {
            putString(USERID, userId)
            putString(FIRST_NAME, firstName)
            putString(LAST_NAME, lastName)
            putString(SUPERSCRIPTION, subscriptionStatus)
            putBoolean(SIGN_IN, true)
            putBoolean(SIGN_UP, true)
            apply()
        }
        Log.e("SiginUp", "addToSharedPreference: userId $userId  firstName $firstName")
    }

    private fun goToSignInPage() {
        findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
    }

    private fun signUpWithEmailAndPassword(task: Task<AuthResult>) {
        val firebaseUser = task.result?.user
        firebaseUser?.let {
            getUserInfo(it.uid)
            goToWelcomePage()
            Log.e("siginup", "signUpWithEmailAndPassword: uid ${firebaseUser.uid}")
        }
    }

    private fun goToWelcomePage() {
        findNavController().navigate(R.id.action_signUpFragment_to_welcomeFragment)
    }

    private fun signUpWithGoogle() {
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
                getUserInfo(it.result.user?.uid!!)
                goToWelcomePage()
            }
        }.addOnFailureListener {
            requireContext().toast("${it.message}")
        }
    }

    private fun register() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            binding?.emailEditText?.text.toString(),
            binding?.passwordEditText?.text.toString()
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                signUpWithEmailAndPassword(it)
            }
        }.addOnFailureListener {
            requireContext().toast("${it.message}")
            binding?.signUpWithEmailPasswordBtn?.isEnabled = true
        }
    }

    private fun registration() {
        if (!isValidFirstName()) {
            requireContext().toast("Enter a first name")
        } else if (!isValidLastName()) {
            requireContext().toast("Enter a last name")
        } else if (!isValidEmail()) {
            requireContext().toast("Enter a valid email")
        } else if (!isValidPassword()) {
            requireContext().toast("Enter a password")
        } else {
            binding?.signUpWithEmailPasswordBtn?.isEnabled = false
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
