package com.rahafcs.co.rightway.ui.trainee

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentUserInfoBinding
import com.rahafcs.co.rightway.utility.Constant.AGE
import com.rahafcs.co.rightway.utility.Constant.GENDER
import com.rahafcs.co.rightway.utility.Constant.HEIGHT
import com.rahafcs.co.rightway.utility.Constant.WEIGHT
import com.rahafcs.co.rightway.utility.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        binding.nextBtn.setOnClickListener {
            if (checkInputValidation()) {
                saveUserInfo()
                goToActivityPage()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        onBackPressedDispatcher()
    }

    // Handel back press.
    private fun onBackPressedDispatcher() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )
    }

    // To save user "trainee" info.
    private fun saveUserInfo() =
        addToSharedPreference(getGender(), getAge(), getWeight(), getHeight())

    // To get user info from views "edittext".
    private fun getHeight() =
        if (binding.cmOption.isChecked) "${binding.heightEditText.text} ${getString(R.string.cm)}"
        else "${binding.weightEditText.text} ${getString(R.string.ft)}"

    private fun getWeight() =
        if (binding.kgOption.isChecked) "${binding.weightEditText.text} ${getString(R.string.kg)}"
        else "${binding.weightEditText.text} ${getString(R.string.lb)}"

    private fun getGender() = if (binding.femaleOption.isChecked) getString(R.string.female)
    else getString(R.string.male)

    private fun getAge() = binding.ageEditText.text.toString()

    // Check user input validation.
    private fun checkInputValidation(): Boolean {
        return if (!ageValidation()) {
            requireContext().toast(getString(R.string.enter_age))
            false
        } else if (!weightValidation()) {
            requireContext().toast(getString(R.string.enter_weight))
            false
        } else if (!heightValidation()) {
            requireContext().toast(getString(R.string.enter_height))
            false
        } else {
            true
        }
    }

    // To save user "trainee" info into sharedPreferences.
    private fun addToSharedPreference(gender: String, age: String, weight: String, height: String) {
        val sharedPreferences =
            activity?.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)!!
        sharedPreferences.edit().apply {
            putString(GENDER, gender)
            putString(AGE, age)
            putString(WEIGHT, weight)
            putString(HEIGHT, height)
            apply()
        }
    }

    // Go to next page.
    private fun goToActivityPage() =
        findNavController().navigate(R.id.action_userInfoFragment_to_activityFragment)

    // Check age validation.
    private fun ageValidation() =
        binding.ageEditText.text.toString().isNotEmpty()

    // Check weight validation.
    private fun weightValidation() =
        binding.weightEditText.text.toString().isNotEmpty()

    // Check height validation.
    private fun heightValidation() =
        binding.heightEditText.text.toString().isNotEmpty()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
