package com.rahafcs.co.rightway.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {
    var binding: FragmentRegistrationBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Welcome"
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            registrationFragment = this@RegistrationFragment
        }
    }

    fun goToSignUpPage() {
        findNavController().navigate(R.id.action_welcomeFragment_to_signUpFragment)
    }

    fun goToSignInPage() {
        findNavController().navigate(R.id.action_welcomeFragment_to_signInFragment)
    }

    override fun onStart() {
        super.onStart()
        val account = FirebaseAuth.getInstance().currentUser
        if (account != null) {
            findNavController().navigate(R.id.action_registrationFragment_to_viewPagerFragment2)
        }
    }
}
