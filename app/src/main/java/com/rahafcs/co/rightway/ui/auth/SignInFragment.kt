package com.rahafcs.co.rightway.ui.auth

import android.app.Application
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
import com.google.firebase.auth.AuthResult
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.ViewModelFactory
import com.rahafcs.co.rightway.databinding.FragmentSignInBinding
import com.rahafcs.co.rightway.utility.Constant.EMAIL
import com.rahafcs.co.rightway.utility.Constant.REMEMBER_ME
import com.rahafcs.co.rightway.utility.Constant.USERID
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.utility.toast
import com.rahafcs.co.rightway.utility.upToTop
import kotlinx.coroutines.launch
import java.lang.Exception

class SignInFragment : Fragment() {
    var binding: FragmentSignInBinding? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val authViewModel by activityViewModels<AuthViewModel> {
        ViewModelFactory(
            ServiceLocator.provideWorkoutRepository(),
            ServiceLocator.provideDefaultUserRepository(),
            ServiceLocator.provideAuthRepository()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ServiceLocator.ProgramListService.application = context?.applicationContext as Application
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
            signInBtn.setOnClickListener {
                signInWithEmailAndPassword()
            }
            signInWithGoogleBtn.setOnClickListener { signInWithGoogle() }
            backArrow.setOnClickListener { this@SignInFragment.upToTop() }
        }
        showUserEmail()
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

    // To clear user email from sharedPreferences.
    private fun clearRememberUserEmail() {
        sharedPreferences =
            activity?.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)!!
        sharedPreferences.edit().apply {
            putBoolean(REMEMBER_ME, false)
            putString(EMAIL, "")
            apply()
        }
    }

    // To show user email from sharedPreferences.
    private fun showUserEmail() {
        sharedPreferences =
            activity?.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)!!
        if (sharedPreferences.getBoolean(REMEMBER_ME, false)) {
            val email = sharedPreferences.getString(EMAIL, "")
            binding?.emailEditText?.setText(email)
            binding?.rememberMeCheckbox?.isChecked = true
        }
    }

    // To save user email in sharedPreferences.
    private fun rememberUserEmail() {
        sharedPreferences =
            activity?.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)!!
        sharedPreferences.edit().apply {
            putBoolean(REMEMBER_ME, true)
            putString(EMAIL, binding?.emailEditText?.text.toString())
            apply()
        }
    }

    // Sign in with google.
    private fun signInWithGoogle() {
        val googleSignInClient =
            GoogleSignIn.getClient(requireContext(), ServiceLocator.provideGoogleSignInOptions())
        googleSignInClient.signInIntent.also {
            startActivityForResult(it, REQUEST_CODE_SIGNING)
        }
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
                        signInOnSuccess(task)
                    }
                } else {
                    requireContext().toast("$it")
                }
            }
        }
    }

    // Sign in with email and password.
    private fun signInWithEmailAndPassword() {
        if (!isValidEmail()) {
            requireContext().toast(getString(R.string.enter_email))
        } else if (!isValidPassword()) {
            requireContext().toast(getString(R.string.enter_password))
        } else {
            binding?.signInBtn?.isEnabled = false
            signIn()
        }
    }

    // Complete sign in with email and password process.
    private fun signIn() {
        lifecycleScope.launch {
            authViewModel.signInWithEmailAndPassword(
                binding?.emailEditText?.text.toString(),
                binding?.passwordEditText?.text.toString()
            ).collect {
                if (it is Task<*>) {
                    val task = it as Task<AuthResult>
                    if (task.isSuccessful) {
                        signInOnSuccess(task)
                    }
                } else {
                    requireContext().toast("$it")
                    binding?.signInBtn?.isEnabled = true
                }
            }
        }
    }

    // If sign in success, save user id into sharedPreference.
    private fun signInOnSuccess(it: Task<AuthResult>) {
        if (binding?.rememberMeCheckbox?.isChecked!!) {
            rememberUserEmail()
        } else {
            clearRememberUserEmail()
        }
        val firebaseUser = it.result.user
        firebaseUser?.let {
            addToSharedPreference(it.uid)
            goToHomePage()
        }
    }

    // Go to home page.
    private fun goToHomePage() =
        findNavController().navigate(R.id.action_signInFragment_to_viewPagerFragment2)

    // Save user id into sharedPreference.
    private fun addToSharedPreference(userId: String) {
        sharedPreferences =
            activity?.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)!!
        sharedPreferences.edit().apply {
            putString(USERID, userId)
            apply()
        }
    }

    // Check email validation.
    private fun isValidEmail(): Boolean {
        return binding?.passwordEditText?.text.toString().isNotEmpty()
    }

    // Check password validation.
    private fun isValidPassword(): Boolean {
        return binding?.emailEditText?.text.toString().isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
