package com.rahafcs.co.rightway.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentHomeBinding
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.FIRSTNAME
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.SUPERSCRIPTION
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.USERID
import com.rahafcs.co.rightway.utility.toast

class HomeFragment : Fragment() {
    var binding: FragmentHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Home"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            homeFragment = this@HomeFragment
        }
        showUserInfo()
    }

    fun signOut() {
        requireContext().toast("${FirebaseAuth.getInstance().currentUser?.email}")
        FirebaseAuth.getInstance().signOut()
        findNavController().navigate(R.id.registrationFragment)
    }

    private fun showUserInfo() {
        val sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        val message = "User Name: ${
        sharedPreferences.getString(
            FIRSTNAME,
            "defName"
        )
        }\nUser id: ${
        sharedPreferences.getString(
            USERID,
            "defUserId"
        )
        }\nUser status: ${sharedPreferences.getString(SUPERSCRIPTION, "defStatus")}"
        binding?.userInfoTextview?.text = message
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
