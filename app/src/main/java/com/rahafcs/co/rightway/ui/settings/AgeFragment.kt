package com.rahafcs.co.rightway.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentAgeBinding
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.AGE
import com.rahafcs.co.rightway.utility.toast

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
        val age = binding?.ageEditText?.text.toString()
        if (age.isNotEmpty()) {
            userAge(age)
            //findNavController().navigate(R.id.action_ageFragment_to_activityFragment)
        } else {
            requireContext().toast("Enter a age")
        }
    }

    private fun userAge(age: String) {
        // send user age to viewModel TODO()
        addToSharedPreference(age)
    }

    private fun addToSharedPreference(
        age: String,
    ) {
        val sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        val editor = sharedPreferences.edit()
        editor.apply {
            putString(AGE, age)
            apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
