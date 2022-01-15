package com.rahafcs.co.rightway.ui.settings.trainee

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentUserInfoBinding
import com.rahafcs.co.rightway.ui.auth.SignUpFragment.Companion.AGE
import com.rahafcs.co.rightway.ui.auth.SignUpFragment.Companion.GENDER
import com.rahafcs.co.rightway.ui.auth.SignUpFragment.Companion.HEIGHT
import com.rahafcs.co.rightway.ui.auth.SignUpFragment.Companion.WEIGHT
import com.rahafcs.co.rightway.utility.toast

class UserInfoFragment : Fragment() {

    private var _binding: FragmentUserInfoBinding? = null
    val binding: FragmentUserInfoBinding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Profile"
        binding.nextBtn.setOnClickListener {
            if (checkInputValidation()) {
                saveUserInfo()
                goToActivityPage()
            }
        }
    }

    private fun saveUserInfo() =
        addToSharedPreference(getGender(), getAge(), getWeight(), getHeight())

    private fun getHeight() =
        if (binding.cmOption.isChecked) "${binding.heightEditText.text} cm" else "${binding.weightEditText.text} ft"

    private fun getWeight() =
        if (binding.kgOption.isChecked) "${binding.weightEditText.text} kg" else "${binding.weightEditText.text} lb"

    private fun getGender() = if (binding.femaleOption.isChecked) "Female" else "Male"

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
        return binding.ageEditText.text.toString().isNotEmpty()
    }

    private fun getAge() = binding.ageEditText.text.toString()

    private fun weightValidation(): Boolean {
        return binding.weightEditText.text.toString().isNotEmpty()
    }

    private fun heightValidation(): Boolean {
        return binding.heightEditText.text.toString().isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
