package com.rahafcs.co.rightway.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentAgeBinding

class AgeFragment : Fragment() {
    private var binding: FragmentAgeBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAgeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            ageFragment = this@AgeFragment
        }
    }

    fun goToActivityPage() {
        if (binding?.ageEditText?.text.toString().isNotEmpty()) {
            userAge()
            findNavController().navigate(R.id.action_ageFragment_to_activityFragment)
        }
    }

    private fun userAge() {
        // send user age to viewModel TODO()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
