package com.rahafcs.co.rightway.ui.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.data.SubscriptionStatus
import com.rahafcs.co.rightway.databinding.FragmentSignUpBinding
import com.rahafcs.co.rightway.utility.Constant
import com.rahafcs.co.rightway.utility.Constant.EMAIL
import com.rahafcs.co.rightway.utility.Constant.FIRST_NAME
import com.rahafcs.co.rightway.utility.Constant.LAST_NAME
import com.rahafcs.co.rightway.utility.Constant.REMEMBER_ME
import com.rahafcs.co.rightway.utility.Constant.SIGN_IN
import com.rahafcs.co.rightway.utility.Constant.SIGN_UP
import com.rahafcs.co.rightway.utility.Constant.SUPERSCRIPTION
import com.rahafcs.co.rightway.utility.Constant.USERID
import com.rahafcs.co.rightway.utility.toast
import com.rahafcs.co.rightway.utility.upToTop
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.Exception

const val REQUEST_CODE_SIGNING = 0

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var binding: FragmentSignUpBinding? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val authViewModel by activityViewModels<AuthViewModel>()
    private lateinit var userType: String

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
            signUpWithGoogleBtn.setOnClickListener { showDialogUserTypeToSignInWithGoogle() }
            backArrow.setOnClickListener { this@SignUpFragment.upToTop() }
        }
    }

    override fun onResume() {
        super.onResume()
        onBackPressedDispatcher()
    }

    // Handel back press.
    private fun onBackPressedDispatcher() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )
    }

    // Choose user type --> trainer "coach" or trainee.
    private fun showDialogUserTypeToSignInWithGoogle() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.user_type_dialog))
            .setMessage(getString(R.string.user_type_dialog))
            .setPositiveButton(getString(R.string.trainer)) { _, _ ->
                userType = getString(R.string.trainer)
                signUpWithGoogle()
            }
            .setNegativeButton(getString(R.string.trainee)) { _, _ ->
                userType = getString(R.string.trainee)
                signUpWithGoogle()
            }.show()
    }

    // Get user info from views. 
    private fun getUserInfo(userId: String, userType: String = "") {
        var firstName = binding?.firstNameEditText?.text.toString()
        if (firstName.isEmpty()) { // if signup with google 
            firstName = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        }
        val lastName = binding?.lastNameEditText?.text.toString()

        val subscriptionStatus = if (userType.isEmpty()) {
            if (binding?.trainee?.isChecked == true) {
                SubscriptionStatus.TRAINEE.toString()
            } else {
                SubscriptionStatus.TRAINER.toString()
            }
        } else userType
        addToSharedPreference(userId, firstName, lastName, subscriptionStatus)
    }

    // Save user info in sharedPreferences.
    private fun addToSharedPreference(
        userId: String,
        firstName: String,
        lastName: String,
        subscriptionStatus: String,
    ) {
        sharedPreferences =
            activity?.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)!!
        sharedPreferences.edit().apply {
            putString(USERID, userId)
            putString(FIRST_NAME, firstName)
            putString(LAST_NAME, lastName)
            putString(SUPERSCRIPTION, subscriptionStatus)
            putBoolean(SIGN_IN, true)
            putBoolean(SIGN_UP, true)
            putBoolean(REMEMBER_ME, false)
            putString(EMAIL, "")
            putString(Constant.LANGUAGE_CODE, "")
            apply()
        }
    }

    // Go Tto sign in page.
    private fun goToSignInPage() =
        findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)

    // Sign up with email and password success.
    private fun signUpWithEmailAndPasswordSuccess(task: Task<AuthResult>) {
        val firebaseUser = task.result?.user
        firebaseUser?.let {
            getUserInfo(it.uid)
            goToWelcomePage()
        }
    }

    // Go to welcome page.
    private fun goToWelcomePage() =
        findNavController().navigate(R.id.action_signUpFragment_to_welcomeFragment)

    // Sign up with google.
    private fun signUpWithGoogle() =
        authViewModel.googleSignInClient().signInIntent.also {
            startActivityForResult(it, REQUEST_CODE_SIGNING)
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == REQUEST_CODE_SIGNING) {
                val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
                account?.let { googleAuthFirebase(it) }
            }
        } catch (e: Exception) {
        }
    }

    // Complete sign in with google process.
    private fun googleAuthFirebase(account: GoogleSignInAccount) {
        lifecycleScope.launch {
            authViewModel.signInWithGoogleAuthFirebase(account).collect {
                if (it is Task<*>) {
                    val task = it as Task<AuthResult>
                    if (task.isSuccessful) {
                        getUserInfo(task.result.user?.uid!!, userType)
                        goToWelcomePage()
                    }
                } else {
                    requireContext().toast("$it")
                }
            }
        }
    }

    // complete register with email and password.
    private fun register() {
        lifecycleScope.launch {
            authViewModel.registerWithEmailAndPassword(
                binding?.emailEditText?.text.toString(),
                binding?.passwordEditText?.text.toString()
            ).collect {
                if (it is Task<*>) {
                    val task = it as Task<AuthResult>
                    if (task.isSuccessful) {
                        signUpWithEmailAndPasswordSuccess(task)
                    }
                } else {
                    requireContext().toast("$it")
                    binding?.signUpWithEmailPasswordBtn?.isEnabled = true
                }
            }
        }
    }

    // Registration with email and password.
    private fun registration() {
        if (!isValidFirstName()) {
            requireContext().toast(getString(R.string.enter_first_name))
        } else if (!isValidLastName()) {
            requireContext().toast(getString(R.string.enter_last_name))
        } else if (!isValidEmail()) {
            requireContext().toast(getString(R.string.enter_email))
        } else if (!isValidPassword()) {
            requireContext().toast(getString(R.string.enter_password))
        } else {
            binding?.signUpWithEmailPasswordBtn?.isEnabled = false
            register()
        }
    }

    // First name validation.
    private fun isValidFirstName() =
        binding?.firstNameEditText?.text.toString().isNotEmpty()

    // Last name validation.
    private fun isValidLastName() =
        binding?.lastNameEditText?.text.toString().isNotEmpty()

    // Email validation.
    private fun isValidEmail(): Boolean {
        val email = binding?.emailEditText?.text.toString().trim()
        return if (email.isEmpty()) {
            false
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    // Password validation.
    private fun isValidPassword(): Boolean {
        return binding?.passwordEditText?.text.toString().isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
