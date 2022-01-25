package com.rahafcs.co.rightway.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {
    var binding: FragmentRegistrationBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            signInBtn.setOnClickListener { goToSignInPage() }
            signUpBtn.setOnClickListener { goToSignUpPage() }
        }
    }

    // Go to sign up page.
    private fun goToSignUpPage() =
        findNavController().navigate(R.id.action_welcomeFragment_to_signUpFragment)

    // Go to sign in page.
    private fun goToSignInPage() =
        findNavController().navigate(R.id.action_welcomeFragment_to_signInFragment)

    // Check if user is sign in. 
    override fun onStart() {
        super.onStart()
        val account = FirebaseAuth.getInstance().currentUser
        if (account != null) {
            findNavController().navigate(R.id.action_registrationFragment_to_viewPagerFragment2)
        }
    }
}
