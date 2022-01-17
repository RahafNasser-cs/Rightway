package com.rahafcs.co.rightway.ui.settings.coach

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.data.User
import com.rahafcs.co.rightway.databinding.FragmentCoachInfoBinding
import com.rahafcs.co.rightway.utility.Constant.COACH_EMAIL
import com.rahafcs.co.rightway.utility.Constant.COACH_EXPERIENCE
import com.rahafcs.co.rightway.utility.Constant.COACH_PHONE
import com.rahafcs.co.rightway.utility.Constant.COACH_PRICE_RANGE
import com.rahafcs.co.rightway.utility.Constant.FIRST_NAME
import com.rahafcs.co.rightway.utility.Constant.LAST_NAME
import com.rahafcs.co.rightway.utility.Constant.SUPERSCRIPTION
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.utility.capitalizeFormatIfFirstLatterCapital
import com.rahafcs.co.rightway.utility.toast
import com.rahafcs.co.rightway.viewmodels.SignUpViewModel
import com.rahafcs.co.rightway.viewmodels.ViewModelFactory

class CoachInfoFragment : Fragment() {
    private var _binding: FragmentCoachInfoBinding? = null
    val binding: FragmentCoachInfoBinding get() = _binding!!
    private val viewModel: CoachViewModel by activityViewModels {
        ViewModelFactory(
            ServiceLocator.provideWorkoutRepository(),
            ServiceLocator.provideUserRepository(),
            ServiceLocator.provideCoachRepository()
        )
    }
    val signUpViewModel: SignUpViewModel by activityViewModels {
        ViewModelFactory(
            ServiceLocator.provideWorkoutRepository(),
            ServiceLocator.provideUserRepository(),
            ServiceLocator.provideCoachRepository()
        )
    }

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

    private fun addToSharedPreference(
        email: String,
        experience: String,
        phone: String,
        priceRange: String,
    ) {
        val sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        sharedPreferences.edit().apply {
            putString(COACH_EMAIL, email)
            putString(COACH_EXPERIENCE, experience)
            putString(COACH_PHONE, phone)
            putString(COACH_PRICE_RANGE, priceRange)
            apply()
        }
    }

    private fun goToHomePage() {
        findNavController().navigate(R.id.action_coachInfoFragment_to_viewPagerFragment2)
    }

    private fun checkInputValidation(): Boolean {
        return if (!experienceValidation()) {
            requireContext().toast("Enter a experience")
            false
        } else if (!phoneValidation()) {
            requireContext().toast("Enter a phone number")
            false
        } else if (!priceValidation()) {
            requireContext().toast("Enter a range price")
            false
        } else if (!emailValidation()) {
            requireContext().toast("Enter a valid Email")
            false
        } else {
            true
        }
    }

    private fun priceValidation(): Boolean {
        return binding.priceRangeEditText.text.toString().isNotEmpty()
    }

    private fun emailValidation(): Boolean {
        val email = binding.emailEditText.text.toString()
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
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

    private fun getName() =
        activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
            .getString(FIRST_NAME, "")!!

    private fun saveCoachInfo() {
        addToSharedPreference(getEmail(), getExperience(), getPhone(), getPriceRange())
        viewModel.saveCoachInfo(getCoachInfo())
//        signUpViewModel.userInfo(getCoachInfo())
    }

    private fun getCoachInfo(): User {
        val sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        return User(
            firstName = sharedPreferences.getString(FIRST_NAME, "")!!,
            lastName = sharedPreferences.getString(LAST_NAME, "")!!,
            subscriptionStatus = sharedPreferences.getString(SUPERSCRIPTION, "")!!
                .capitalizeFormatIfFirstLatterCapital(),
            experience = sharedPreferences.getString(COACH_EXPERIENCE, "")!!,
            email = sharedPreferences.getString(COACH_EMAIL, "")!!,
            price = sharedPreferences.getString(COACH_PRICE_RANGE, "")!!,
            phoneNumber = sharedPreferences.getString(COACH_PHONE, "")!!
        )
    }
//    private fun getCoachInfo() =
//        Coach(getName(), getExperience(), getEmail(), getPhone(), getPriceRange())

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
