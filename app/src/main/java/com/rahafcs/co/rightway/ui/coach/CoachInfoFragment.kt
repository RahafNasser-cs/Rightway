package com.rahafcs.co.rightway.ui.coach

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.ViewModelFactory
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

class CoachInfoFragment : Fragment() {
    private var _binding: FragmentCoachInfoBinding? = null
    val binding: FragmentCoachInfoBinding get() = _binding!!
    private val coachViewModel: CoachViewModel by activityViewModels {
        ViewModelFactory(
            ServiceLocator.provideWorkoutRepository(),
            ServiceLocator.provideDefaultUserRepository(),
            ServiceLocator.provideAuthRepository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment.
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

    // To add user "coach" info in sharedPreferences.
    private fun addToSharedPreference(
        email: String,
        experience: String,
        phone: String,
        priceRange: String,
    ) {
        val sharedPreferences =
            activity?.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)!!
        sharedPreferences.edit().apply {
            putString(COACH_EMAIL, email)
            putString(COACH_EXPERIENCE, experience)
            putString(COACH_PHONE, phone)
            putString(COACH_PRICE_RANGE, priceRange)
            apply()
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

    // To go home page.
    private fun goToHomePage() =
        findNavController().navigate(R.id.action_coachInfoFragment_to_viewPagerFragment2)

    // Check user input validation.
    private fun checkInputValidation(): Boolean {
        return if (!experienceValidation()) {
            requireContext().toast(getString(R.string.enter_experience))
            false
        } else if (!phoneValidation()) {
            requireContext().toast(getString(R.string.enter_phone))
            false
        } else if (!priceValidation()) {
            requireContext().toast(getString(R.string.enter_price))
            false
        } else if (!emailValidation()) {
            requireContext().toast(getString(R.string.enter_email))
            false
        } else {
            true
        }
    }

    // Validation price input.
    private fun priceValidation() =
        binding.priceRangeEditText.text.toString().isNotEmpty()

    // Validation email input.
    private fun emailValidation(): Boolean {
        val email = binding.emailEditText.text.toString()
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Validation phone input.
    private fun phoneValidation() =
        binding.phoneEditText.text.toString().isNotEmpty()

    // Validation experience input.
    private fun experienceValidation() =
        binding.experienceEditText.text.toString().isNotEmpty()

    // Get experience from view.
    private fun getExperience() =
        binding.experienceEditText.text.toString()

    // Get phone from view.
    private fun getPhone() =
        binding.phoneEditText.text.toString()

    // Get price from view.
    private fun getPriceRange() =
        binding.priceRangeEditText.text.toString()

    // Get email from view.
    private fun getEmail() =
        binding.emailEditText.text.toString()

    // Save user "coach" info.
    private fun saveCoachInfo() {
        addToSharedPreference(getEmail(), getExperience(), getPhone(), getPriceRange())
        coachViewModel.saveCoachInfo(getCoachInfo())
    }

    // Get coach info. 
    private fun getCoachInfo(): User {
        val sharedPreferences =
            activity?.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)!!
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
