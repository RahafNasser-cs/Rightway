package com.rahafcs.co.rightway.ui

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
import com.rahafcs.co.rightway.data.SubscriptionStatus
import com.rahafcs.co.rightway.databinding.FragmentSignUpBinding
import com.rahafcs.co.rightway.utility.toast

const val REQUEST_CODE_SIGNING = 0

class SignUpFragment : Fragment() {
    private var binding: FragmentSignUpBinding? = null
    lateinit var sharedPreferences: SharedPreferences
    // private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Registration"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            signUpFragment = this@SignUpFragment
        }
    }

    private fun getUserInfo(userId: String) {
        val firstName = binding?.firstNameEditText?.text.toString()
        val lastName = binding?.lastNameEditText?.text.toString()
        val subscriptionStatus = if (binding?.trainee?.isChecked == true) {
            SubscriptionStatus.TRAINEE
        } else {
            SubscriptionStatus.TRAINER
        }
        // createUserInfo(userId, firstName, lastName, subscriptionStatus)
        addToSharedPreference(userId, firstName, subscriptionStatus)
    }

    private fun addToSharedPreference(
        userId: String,
        firstName: String,
        subscriptionStatus: SubscriptionStatus
    ) {
        sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        val editor = sharedPreferences.edit()
        editor.apply {
            putString(USERID, userId)
            putString(FIRSTNAME, firstName)
            putString(SUPERSCRIPTION, subscriptionStatus.toString())
            putBoolean(SIGN_IN, true)
            putBoolean(SIGN_UP, true)
            apply()
        }
    }

    private fun createUserInfo(
        userId: String,
        firstName: String,
        lastName: String,
        subscriptionStatus: SubscriptionStatus
    ) {
        // viewModel.userInfo(User(userId, firstName, lastName, subscriptionStatus))
    }

    fun goToSignInPage() {
        requireContext().toast("sign in page")
        findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        // TODO
    }

    private fun signUpWithEmailAndPassword(task: Task<AuthResult>) {
        val firebaseUser = task.result?.user
        requireContext().toast("hello ${firebaseUser?.email}")
        firebaseUser?.let {
            getUserInfo(it.uid)
            goToWelcomePage()
        }
        // save user info 
        // TODO
    }

    private fun goToWelcomePage() {
        findNavController().navigate(R.id.action_signUpFragment_to_welcomeFragment)
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
                requireContext().toast("hello ${it.result?.user?.email}")
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
                // viewModel.setRegistrationStatus(RegistrationStatus.LOADING)
                signUpWithEmailAndPassword(it)
            }
        }.addOnFailureListener {
            // viewModel.setRegistrationStatus(RegistrationStatus.FAILURE)
            requireContext().toast("${it.message}")
        }
    }

    fun registration() {
        if (!isValidFirstName()) {
            requireContext().toast("Enter a first name")
        } else if (!isValidLastName()) {
            requireContext().toast("Enter a last name")
        } else if (!isValidEmail()) {
            requireContext().toast("Enter a valid email")
        } else if (!isValidPassword()) {
            requireContext().toast("Enter a password")
        } else {
            // viewModel.setRegistrationStatus(RegistrationStatus.LOADING)
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

    companion object {
        const val USERID = "userId"
        const val FIRSTNAME = "firstName"
        const val SUPERSCRIPTION = "SubscriptionStatus"
        const val GENDER = "gender"
        const val HEIGHT = "height"
        const val WEIGHT = "weight"
        const val AGE = "age"
        const val SIGN_IN = "signIn"
        const val ACTIVITY_LEVEL = "activityLevel"
        const val SIGN_UP = "signUp"
    }
}
