package com.rahafcs.co.rightway.ui.trainee

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.data.User
import com.rahafcs.co.rightway.databinding.FragmentActivityBinding
import com.rahafcs.co.rightway.utility.Constant.ACTIVITY_LEVEL
import com.rahafcs.co.rightway.utility.Constant.AGE
import com.rahafcs.co.rightway.utility.Constant.FIRST_NAME
import com.rahafcs.co.rightway.utility.Constant.GENDER
import com.rahafcs.co.rightway.utility.Constant.HEIGHT
import com.rahafcs.co.rightway.utility.Constant.LAST_NAME
import com.rahafcs.co.rightway.utility.Constant.SIGN_UP
import com.rahafcs.co.rightway.utility.Constant.SUPERSCRIPTION
import com.rahafcs.co.rightway.utility.Constant.WEIGHT
import com.rahafcs.co.rightway.utility.capitalizeFormatIfFirstLatterCapital
import com.rahafcs.co.rightway.utility.upToTop
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityFragment : Fragment() {
    private var binding: FragmentActivityBinding? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val traineeViewModel: TraineeViewModel by activityViewModels()
//    {
//        ViewModelFactory(
//            ServiceLocator.provideWorkoutRepository(),
//            ServiceLocator.provideDefaultUserRepository(),
// //            ServiceLocator.provideAuthRepository()
//        )
//    }

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

    // Go to home page.
    private fun goToHomePage() =
        findNavController().navigate(R.id.action_activityFragment_to_viewPagerFragment2)

    // Get activity level from views.
    private fun getActivityLevel() =
        when (binding?.activityOptions?.checkedRadioButtonId) {
            R.id.option_0 -> getString(R.string.option_0)
            R.id.option_1 -> getString(R.string.option_1)
            R.id.option_2 -> getString(R.string.option_2)
            else -> getString(R.string.option_3)
        }

    // Set activity level into sharedPreferences.
    private fun setActivityLevel() =
        addToSharedPreference(getActivityLevel())

    // Save activity level in sharedPreferences.
    private fun addToSharedPreference(activityLevel: String) {
        sharedPreferences =
            activity?.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)!!
        sharedPreferences.edit().apply {
            putString(ACTIVITY_LEVEL, activityLevel)
            apply()
        }
    }

    // Save new user info in Firestore.
    private fun saveUserInfo() {
        sharedPreferences =
            activity?.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)!!
        if (sharedPreferences.getBoolean(SIGN_UP, false)) {
            traineeViewModel.userInfo(getUserInfo())
            val editor = sharedPreferences.edit()
            editor.putBoolean(SIGN_UP, false)
            editor.apply()
        }
    }

    // Get all user info from sharedPreferences.
    private fun getUserInfo(): User {
        sharedPreferences =
            activity?.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)!!
        return User(
            firstName = sharedPreferences.getString(FIRST_NAME, "")!!,
            lastName = sharedPreferences.getString(LAST_NAME, "")!!,
            subscriptionStatus = sharedPreferences.getString(SUPERSCRIPTION, "")!!
                .capitalizeFormatIfFirstLatterCapital(),
            weight = sharedPreferences.getString(WEIGHT, "")!!,
            height = sharedPreferences.getString(HEIGHT, "")!!,
            gender = sharedPreferences.getString(GENDER, "")!!,
            activity = sharedPreferences.getString(ACTIVITY_LEVEL, "")!!,
            age = sharedPreferences.getString(AGE, "")!!
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
