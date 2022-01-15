package com.rahafcs.co.rightway.ui.settings.coach

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rahafcs.co.rightway.databinding.FragmentCoachInfoBinding
import com.rahafcs.co.rightway.ui.auth.SignUpFragment
import com.rahafcs.co.rightway.utility.toast

class CoachInfoFragment : Fragment() {
    private var _binding: FragmentCoachInfoBinding? = null
    val binding: FragmentCoachInfoBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCoachInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            doneBtn.setOnClickListener {
                if (checkInputValidation()) {
                    saveCoachInfo()
                    goToHomePage()
                }
            }
        }
    }

    private fun addToSharedPreference(email: String, experience: String, phone: String, priceRange: String) {
        val sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        sharedPreferences.edit().apply {
            putString(SignUpFragment.COACH_EMAIL, email)
            putString(SignUpFragment.COACH_EXPERIENCE, experience)
            putString(SignUpFragment.COACH_PHONE, phone)
            putString(SignUpFragment.COACH_PRICE_RANGE, priceRange)
            apply()
        }
    }

    private fun goToHomePage() {
        TODO("Not yet implemented")
    }

    private fun checkInputValidation(): Boolean {
        return if (!experienceValidation()) {
            requireContext().toast("")
            false
        } else if (!phoneValidation()) {
            requireContext().toast("")
            false
        } else if (!priceValidation()) {
            requireContext().toast("")
            false
        } else if (!emailValidation()) {
            requireContext().toast("")
            false
        } else {
            true
        }
    }

    private fun priceValidation(): Boolean {
        return binding.priceRangeEditText.text.toString().isNotEmpty()
    }
    private fun emailValidation(): Boolean {
        return binding.emailEditText.text.toString().isNotEmpty()
    }

    private fun phoneValidation(): Boolean {
        return binding.phoneEditText.text.toString().isNotEmpty()
    }

    private fun experienceValidation(): Boolean {
        return binding.experienceEditText.text.toString().isNotEmpty()
    }

    private fun getExperience(): String {
        return binding.experienceEditText.text.toString()
    }

    private fun getPhone(): String {
        return binding.phoneEditText.text.toString()
    }

    private fun getPriceRange(): String {
        return binding.priceRangeEditText.text.toString()
    }
    private fun getEmail(): String {
        return binding.emailEditText.text.toString()
    }

    private fun saveCoachInfo() {
//        addToSharedPreference(getEmail(), getExperience(),)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
