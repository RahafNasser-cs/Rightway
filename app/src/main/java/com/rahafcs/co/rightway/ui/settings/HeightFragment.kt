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
import com.rahafcs.co.rightway.databinding.FragmentHeightBinding
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.HEIGHT
import com.rahafcs.co.rightway.utility.toast

class HeightFragment : Fragment() {
    private var binding: FragmentHeightBinding? = null
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHeightBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            heightFragment = this@HeightFragment
        }
    }

    private fun goToWeightPage() {
        findNavController().navigate(R.id.action_heightFragment_to_weightFragment)
    }

    fun userHeightByFeet() {
        val height = binding?.heightEditText?.text.toString()
        if (height.isNotEmpty()) {
            // send to viewModel TODO()
            addToSharedPreference(height + "feet")
            goToWeightPage()
        } else {
            requireContext().toast("Enter a height")
        }
    }

    fun userHeightByCentimeter() {
        val height = binding?.heightEditText?.text.toString()
        if (height.isNotEmpty()) {
            // send to viewModel TODO()
            addToSharedPreference(height + "cm")
            goToWeightPage()
        } else {
            requireContext().toast("Enter a height")
        }
    }

    private fun addToSharedPreference(
        height: String,
    ) {
        sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        val editor = sharedPreferences.edit()
        editor.apply {
            putString(HEIGHT, height)
            apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
