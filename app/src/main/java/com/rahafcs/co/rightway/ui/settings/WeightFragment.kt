package com.rahafcs.co.rightway.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentWeightBinding
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.WEIGHT
import com.rahafcs.co.rightway.utility.toast

class WeightFragment : Fragment() {
    private var binding: FragmentWeightBinding? = null
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWeightBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            weightFragment = this@WeightFragment
        }
    }

    private fun goToAgePage() {
        findNavController().navigate(R.id.action_weightFragment_to_ageFragment)
    }

    fun userWeightByPound() {
        // send user weight to viewModel TODO()
        val weight = binding?.weightEditText?.text.toString()
        if (weight.isNotEmpty()) {
            addToSharedPreference(weight + "Pound")
            goToAgePage()
        } else {
            requireContext().toast("Enter a weight")
        }
    }

    fun userWeightByKilogram() {
        // send user weight to viewModel TODO()
        val weight = binding?.weightEditText?.text.toString()
        if (weight.isNotEmpty()) {
            addToSharedPreference(weight + "kg")
            goToAgePage()
        } else {
            requireContext().toast("Enter a weight")
        }
    }

    private fun addToSharedPreference(
        weight: String,
    ) {
        sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        val editor = sharedPreferences.edit()
        editor.apply {
            putString(WEIGHT, weight)
            apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
