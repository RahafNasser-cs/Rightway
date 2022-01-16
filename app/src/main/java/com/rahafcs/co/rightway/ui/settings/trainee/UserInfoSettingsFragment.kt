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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.data.User
import com.rahafcs.co.rightway.databinding.FragmentUserInfoSettingsBinding
import com.rahafcs.co.rightway.ui.auth.SignUpFragment
import com.rahafcs.co.rightway.ui.auth.SignUpFragment.Companion.SIGN_IN
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.viewmodels.SignUpViewModel
import com.rahafcs.co.rightway.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

class UserInfoSettingsFragment : Fragment() {
    var binding: FragmentUserInfoSettingsBinding? = null
    var isEditMode = false
    lateinit var sharedPreferences: SharedPreferences
    val viewModel: SignUpViewModel by activityViewModels {
        ViewModelFactory(
            ServiceLocator.provideWorkoutRepository(),
            ServiceLocator.provideUserRepository(),
            ServiceLocator.provideCoachRepository()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Home"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserInfoSettingsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            logoutImg.setOnClickListener {
                signOutConfirmDialog()
            }
            homeImg.setOnClickListener { goToHomePage() }
            editImg.setOnClickListener {
                if (!isEditMode) {
                    isEditMode = true
                    it.visibility = View.GONE
                    binding?.closeImg?.visibility = View.VISIBLE
                    hideUserInfoTextView()
                    readUserInfo()
                }
            }
        }
        readUserInfo()
    }

    private fun signOutConfirmDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Sign Out")
            .setMessage("Are you sure you want to sign out?")
            .setNegativeButton("Cancel") { _, _ -> }
            .setPositiveButton("Sign out") { _, _ ->
                signOut()
            }.show()
    }

    private fun readUserInfo() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.readUserInfo().collect {
                    Log.d("UserInfoSettingsFragment", "readUserInfo: $it")
                    if (isEditMode) {
                        showEditUserInfo(it)
                    }
                    showUserInfo(it)
                }
            }
        }
    }

    private fun signOut() {
        val sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        val editor = sharedPreferences.edit()

        // requireContext().toast("${FirebaseAuth.getInstance().currentUser?.email}")
        Log.e("TAG", "signOut: before ${FirebaseAuth.getInstance().currentUser?.uid!!}")
        AuthUI.getInstance()
            .signOut(requireContext()).addOnSuccessListener {
                FirebaseAuth.getInstance().signOut()
                editor.putBoolean(SIGN_IN, false)
                editor.apply()
                findNavController().navigate(R.id.registrationFragment)
            }.addOnFailureListener {
            }
    }

    private fun showUserInfo(userInfo: User) {
        binding?.apply {
            userNameTextview.text = userInfo.firstName
            userHeight.text = userInfo.height
            userWeight.text = userInfo.weight
            userAge.text = userInfo.age
            userGender.text = userInfo.gender
            userActivityLeve.text = userInfo.activity
            subscriptionStatus.text = userInfo.subscriptionStatus
        }
    }

    private fun showEditUserInfo(userInfo: User) {
        makeEditTextVisible()
        binding?.apply {
            representUserInfoIntoEditText(userInfo) // show info into editText
            closeImg.setOnClickListener {
                if (isEditMode) {
                    isEditMode = false
                    it.visibility = View.GONE
                    binding?.editImg?.visibility = View.VISIBLE
                    hideEditUserInfo()
                    showUserInfoTextView()
                }
            }
            saveBtn.setOnClickListener {
                if (isEditMode) {
                    isEditMode = false
                    binding?.closeImg?.visibility = View.GONE
                    binding?.editImg?.visibility = View.VISIBLE
                    saveUserInfo(getUpdatedUserInfo(userInfo))
                    hideEditUserInfo()
                    showUserInfoTextView()
                }
            }
        }
    }

    private fun makeEditTextVisible() {
        binding?.apply {
            userHeightInputLayout.visibility = View.VISIBLE
            userWeightInputLayout.visibility = View.VISIBLE
            userAgeInputLayout.visibility = View.VISIBLE
            activityOptions.visibility = View.VISIBLE
            genderOption.visibility = View.VISIBLE
            userNameInputLayout.visibility = View.VISIBLE
            // userSubscriptionStatusOptions.visibility = View.VISIBLE
            heightOption.visibility = View.VISIBLE
            weightOption.visibility = View.VISIBLE
            saveBtn.visibility = View.VISIBLE
        }
    }

    private fun representUserInfoIntoEditText(userInfo: User) {
        binding?.apply {
            userHeightEditText.setText(userInfo.height.substring(0, userInfo.height.length - 2))
            userWeightEditText.setText(userInfo.weight.substring(0, userInfo.weight.length - 2))
            userAgeEditText.setText(userInfo.age)
            userNameEditText.setText(userInfo.firstName)
            when (userInfo.gender) {
                "Female" -> femaleOption.isChecked = true
                else -> maleOption.isChecked = true
            }
            when (userInfo.activity) {
                requireContext().getString(R.string.option_0) -> option0.isChecked = true
                requireContext().getString(R.string.option_1) -> option1.isChecked = true
                requireContext().getString(R.string.option_2) -> option2.isChecked = true
                else -> option3.isChecked = true
            }
//            when (userInfo.subscriptionStatus) {
//                requireContext().getString(R.string.trainee) -> traineeOption.isChecked = true
//                else -> trainerOption.isChecked = true
//            }
            when (userInfo.height.substring(userInfo.height.length - 2, userInfo.height.length)) {
                "cm" -> cmOption.isChecked = true
                else -> ftOption.isChecked = true
            }
            when (userInfo.weight.substring(userInfo.weight.length - 2, userInfo.weight.length)) {
                "kg" -> kgOption.isChecked = true
                else -> lbOption.isChecked = true
            }
        }
    }

    private fun hideEditUserInfo() {
        binding?.apply {
            userHeightInputLayout.visibility = View.GONE
            userWeightInputLayout.visibility = View.GONE
            userAgeInputLayout.visibility = View.GONE
            activityOptions.visibility = View.GONE
            genderOption.visibility = View.GONE
            userNameInputLayout.visibility = View.GONE
            // userSubscriptionStatusOptions.visibility = View.GONE
            heightOption.visibility = View.GONE
            weightOption.visibility = View.GONE
            saveBtn.visibility = View.GONE
        }
    }

    private fun hideUserInfoTextView() {
        binding?.apply {
            userHeight.visibility = View.GONE
            userWeight.visibility = View.GONE
            userAge.visibility = View.GONE
            userActivityLeve.visibility = View.GONE
            userGender.visibility = View.GONE
            userNameTextview.visibility = View.GONE
            // subscriptionStatus.visibility = View.GONE
        }
    }

    private fun showUserInfoTextView() {
        binding?.apply {
            userHeight.visibility = View.VISIBLE
            userWeight.visibility = View.VISIBLE
            userAge.visibility = View.VISIBLE
            userActivityLeve.visibility = View.VISIBLE
            userGender.visibility = View.VISIBLE
            userNameTextview.visibility = View.VISIBLE
            subscriptionStatus.visibility = View.VISIBLE
        }
    }

    private fun saveUserInfo(userInfo: User) {
        viewModel.userInfo(userInfo)
    }

    private fun getUpdatedUserInfo(oldUserInfo: User) =
        oldUserInfo.copy(
            firstName = getFirstName(),
            // subscriptionStatus = getSubscriptionStatus(),
            height = getHeight(),
            weight = getWeight(),
            age = getAge(),
            gender = getGender(),
            activity = getActivityLevel()
        )

    private fun getSubscriptionStatus() =
        if (binding?.traineeOption?.isChecked!!) {
            updateSharedPreference(requireContext().getString(R.string.trainee))
            requireContext().getString(R.string.trainee)
        } else {
            updateSharedPreference(requireContext().getString(R.string.trainer))
            requireContext().getString(R.string.trainer)
        }

    private fun updateSharedPreference(subscriptionStatus: String) {
        activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!.edit().putString(
            SignUpFragment.SUPERSCRIPTION, subscriptionStatus
        ).apply()
    }

    private fun getActivityLevel() = when (binding?.activityOptions?.checkedRadioButtonId) {
        R.id.option_0 -> requireContext().getString(R.string.option_0)
        R.id.option_1 -> requireContext().getString(R.string.option_1)
        R.id.option_2 -> requireContext().getString(R.string.option_2)
        else -> requireContext().getString(R.string.option_3)
    }

    private fun getGender() = if (binding?.femaleOption?.isChecked!!) "Female" else "Male"

    private fun getAge() = binding?.userAgeEditText?.text.toString()

    private fun getWeight() =
        if (binding?.kgOption?.isChecked!!) "${binding?.userWeightEditText?.text} kg"
        else "${binding?.userWeightEditText?.text} lb"

    private fun getHeight() =
        if (binding?.cmOption?.isChecked!!) "${binding?.userHeightEditText?.text?.toString()} cm"
        else "${binding?.userHeightEditText?.text} ft"

    private fun getFirstName() =
        binding?.userNameEditText?.text.toString()

    private fun goToHomePage() =
        findNavController().navigate(R.id.action_userInfoSettingsFragment2_to_viewPagerFragment2)

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
