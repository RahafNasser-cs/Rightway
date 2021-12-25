package com.rahafcs.co.rightway.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentActivityBinding

class ActivityFragment : Fragment() {
    private var binding: FragmentActivityBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentActivityBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            activityFragment = this@ActivityFragment
        }
    }

    fun goToHomePage() {
        findNavController().navigate(R.id.action_activityFragment_to_homeFragment)
    }

    fun setActivityLevel(level: Int) {
        // send user level to viewModel TODO()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
