package com.rahafcs.co.rightway.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentHomeBinding

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
    }

    fun signOut() {
        message("${FirebaseAuth.getInstance().currentUser?.email}")
        FirebaseAuth.getInstance().signOut()
        findNavController().navigate(R.id.welcomeFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun message(str: String) {
        Toast.makeText(requireContext(), str, Toast.LENGTH_SHORT).show()
    }
}
