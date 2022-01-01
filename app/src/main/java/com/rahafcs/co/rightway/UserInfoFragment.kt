package com.rahafcs.co.rightway

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rahafcs.co.rightway.databinding.FragmentUserInfoBinding
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.AGE
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.GENDER
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.HEIGHT
import com.rahafcs.co.rightway.ui.SignUpFragment.Companion.WEIGHT
import com.rahafcs.co.rightway.utility.toast

class UserInfoFragment : Fragment() {

    private var binding: FragmentUserInfoBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.nextBtn?.setOnClickListener {
            if (checkInputValidation()) {
                goToActivityPage()
            }
        }
    }

    private fun saveUserInfo() {
        val gender = if (binding?.femaleOption?.isChecked!!) "Female" else "Male"
        val age = binding?.ageEditText?.text.toString()
        val weight =
            if (binding?.kgOption?.isChecked!!) "${binding?.weightEditText?.text} kg" else "${binding?.weightEditText?.text} lb"
        val height =
            if (binding?.cmOption?.isChecked!!) "${binding?.heightEditText?.text} cm" else "${binding?.weightEditText?.text} ft"
        addToSharedPreference(gender, age, weight, height)
    }

    private fun checkInputValidation(): Boolean {
        return if (!ageValidation()) {
            requireContext().toast("Enter a age")
            false
        } else if (!weightValidation()) {
            requireContext().toast("Enter a weight")
            false
        } else if (!heightValidation()) {
            requireContext().toast("Enter a height")
            false
        } else {
            saveUserInfo()
            true
        }
    }

    private fun addToSharedPreference(gender: String, age: String, weight: String, height: String) {
        val sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        sharedPreferences.edit().apply {
            putString(GENDER, gender)
            putString(AGE, age)
            putString(WEIGHT, weight)
            putString(HEIGHT, height)
            apply()
        }
    }

    private fun goToActivityPage() {
        findNavController().navigate(R.id.action_userInfoFragment_to_activityFragment)
    }

    private fun ageValidation(): Boolean {
        return binding?.ageEditText?.text.toString().isNotEmpty()
    }

    private fun weightValidation(): Boolean {
        return binding?.weightEditText?.text.toString().isNotEmpty()
    }

    private fun heightValidation(): Boolean {
        return binding?.heightEditText?.text.toString().isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
