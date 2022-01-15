package com.rahafcs.co.rightway.ui.settings.trainee

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.data.User
import com.rahafcs.co.rightway.databinding.FragmentActivityBinding
import com.rahafcs.co.rightway.ui.auth.SignUpFragment
import com.rahafcs.co.rightway.ui.auth.SignUpFragment.Companion.ACTIVITY_LEVEL
import com.rahafcs.co.rightway.ui.auth.SignUpFragment.Companion.AGE
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.utility.capitalizeFormatIfFirstLatterCapital
import com.rahafcs.co.rightway.utility.upToTop
import com.rahafcs.co.rightway.viewmodels.SignUpViewModel
import com.rahafcs.co.rightway.viewmodels.ViewModelFactory

class ActivityFragment : Fragment() {
    private var binding: FragmentActivityBinding? = null
    lateinit var sharedPreferences: SharedPreferences
    val viewModel: SignUpViewModel by activityViewModels {
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
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentActivityBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Profile"
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            doneBtn.setOnClickListener {
                setActivityLevel()
                saveUserInfo()
                goToHomePage()
            }
            backArrow.setOnClickListener { this@ActivityFragment.upToTop() }
        }
    }

    private fun goToHomePage() {
        findNavController().navigate(R.id.action_activityFragment_to_viewPagerFragment2)
    }

    private fun getActivityLevel(): String {
        return when (binding?.activityOptions?.checkedRadioButtonId) {
            R.id.option_0 -> requireContext().getString(R.string.option_0)
            R.id.option_1 -> requireContext().getString(R.string.option_1)
            R.id.option_2 -> requireContext().getString(R.string.option_2)
            else -> requireContext().getString(R.string.option_3)
        }
    }

    private fun setActivityLevel() {
        // send user level to viewModel TODO()
        addToSharedPreference(getActivityLevel())
    }

    private fun addToSharedPreference(
        activityLevel: String,
    ) {
        sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        sharedPreferences.edit().apply {
            putString(ACTIVITY_LEVEL, activityLevel)
            apply()
        }
    }

    private fun saveUserInfo() {
        val sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        Log.e(
            "TAG",
            "saveUserInfo: ${sharedPreferences.getBoolean(SignUpFragment.SIGN_UP, false)}",
        )
        if (sharedPreferences.getBoolean(SignUpFragment.SIGN_UP, false)) {
            Log.e("TAG", "saveUserInfo: in if")
            viewModel.userInfo(getUserInfo())
            val editor = sharedPreferences.edit()
            editor.putBoolean(SignUpFragment.SIGN_UP, false)
            editor.apply()
        }
    }

    private fun getUserInfo(): User {
        sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        return User(
            firstName = sharedPreferences.getString(SignUpFragment.FIRST_NAME, "")!!,
            lastName = sharedPreferences.getString(SignUpFragment.LAST_NAME, "")!!,
            subscriptionStatus = sharedPreferences.getString(SignUpFragment.SUPERSCRIPTION, "")!!
                .capitalizeFormatIfFirstLatterCapital(),
            weight = sharedPreferences.getString(SignUpFragment.WEIGHT, "")!!,
            height = sharedPreferences.getString(SignUpFragment.HEIGHT, "")!!,
            gender = sharedPreferences.getString(SignUpFragment.GENDER, "")!!,
            activity = sharedPreferences.getString(ACTIVITY_LEVEL, "")!!,
            age = sharedPreferences.getString(AGE, "")!!
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
