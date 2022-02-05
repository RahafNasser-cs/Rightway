package com.rahafcs.co.rightway.ui.trainee

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.data.User
import com.rahafcs.co.rightway.databinding.FragmentUserInfoSettingsBinding
import com.rahafcs.co.rightway.ui.auth.AuthViewModel
import com.rahafcs.co.rightway.utility.Constant.SIGN_IN
import com.rahafcs.co.rightway.utility.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserInfoSettingsFragment : Fragment() {
    var binding: FragmentUserInfoSettingsBinding? = null
    private var isEditMode = false
    private val traineeViewModel: TraineeViewModel by activityViewModels()
    private val authViewModel by activityViewModels<AuthViewModel>()

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

    // To confirm sign out process.
    private fun signOutConfirmDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.sign_out))
            .setMessage(getString(R.string.confirm_sign_out))
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.sign_out)) { _, _ ->
                signOut()
            }.show()
    }

    // To get user "trainee" info then show it in edittext or textview.
    private fun readUserInfo() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                traineeViewModel.readUserInfo().collect {
                    if (isEditMode) {
                        showEditUserInfo(it)
                    }
                    showUserInfo(it)
                }
            }
        }
    }

    // To sign out.
    private fun signOut() {
        val sharedPreferences =
            activity?.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)!!
        lifecycleScope.launch {
            authViewModel.signOut().collect {
                if (it is Task<*>) {
                    val task = it as Task<Void>
                    if (task.isSuccessful) {
                        sharedPreferences.edit().apply {
                            putBoolean(SIGN_IN, false)
                            apply()
                        }
                        findNavController().navigate(R.id.registrationFragment)
                    }
                } else {
                    requireContext().toast(getString(R.string.sign_out_error))
                }
            }
        }
    }

    // Show user "trainee" info in textview.
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

    // Show views "edittext" to enable edit info.
    private fun showEditUserInfo(userInfo: User) {
        makeEditTextVisible()
        binding?.apply {
            representUserInfoIntoEditText(userInfo) // show info into editText
            closeImg.setOnClickListener {
                if (isEditMode) {
                    cancelEditInfo()
                }
            }
            saveBtn.setOnClickListener {
                if (isEditMode) {
                    saveEditInfo(userInfo)
                }
            }
        }
    }

    // To complete edit process.
    private fun saveEditInfo(userInfo: User) {
        isEditMode = false
        binding?.closeImg?.visibility = View.GONE
        binding?.editImg?.visibility = View.VISIBLE
        saveUserInfo(getUpdatedUserInfo(userInfo))
        hideEditUserInfo()
        showUserInfoTextView()
    }

    // Cancel edit info.
    private fun cancelEditInfo() {
        isEditMode = false
        binding?.closeImg?.visibility = View.GONE
        binding?.editImg?.visibility = View.VISIBLE
        hideEditUserInfo()
        showUserInfoTextView()
    }

    // Make edittext visible to enable edit mode.
    private fun makeEditTextVisible() {
        binding?.apply {
            userHeightInputLayout.visibility = View.VISIBLE
            userWeightInputLayout.visibility = View.VISIBLE
            userAgeInputLayout.visibility = View.VISIBLE
            activityOptions.visibility = View.VISIBLE
            genderOption.visibility = View.VISIBLE
            userNameInputLayout.visibility = View.VISIBLE
            heightOption.visibility = View.VISIBLE
            weightOption.visibility = View.VISIBLE
            saveBtn.visibility = View.VISIBLE
        }
    }

    // represent old user info into edit text.
    private fun representUserInfoIntoEditText(userInfo: User) {
        binding?.apply {
            userHeightEditText.setText(userInfo.height.substring(0, userInfo.height.length - 2))
            userWeightEditText.setText(userInfo.weight.substring(0, userInfo.weight.length - 2))
            userAgeEditText.setText(userInfo.age)
            userNameEditText.setText(userInfo.firstName)
            when (userInfo.gender) {
                getString(R.string.female) -> femaleOption.isChecked = true
                else -> maleOption.isChecked = true
            }
            when (userInfo.activity) {
                getString(R.string.option_0) -> option0.isChecked = true
                getString(R.string.option_1) -> option1.isChecked = true
                getString(R.string.option_2) -> option2.isChecked = true
                else -> option3.isChecked = true
            }
            when (userInfo.height.substring(userInfo.height.length - 2, userInfo.height.length)) {
                getString(R.string.cm) -> cmOption.isChecked = true
                else -> ftOption.isChecked = true
            }
            when (userInfo.weight.substring(userInfo.weight.length - 2, userInfo.weight.length)) {
                getString(R.string.kg) -> kgOption.isChecked = true
                else -> lbOption.isChecked = true
            }
        }
    }

    // Make edittext gone to disable edit mode.
    private fun hideEditUserInfo() {
        binding?.apply {
            userHeightInputLayout.visibility = View.GONE
            userWeightInputLayout.visibility = View.GONE
            userAgeInputLayout.visibility = View.GONE
            activityOptions.visibility = View.GONE
            genderOption.visibility = View.GONE
            userNameInputLayout.visibility = View.GONE
            heightOption.visibility = View.GONE
            weightOption.visibility = View.GONE
            saveBtn.visibility = View.GONE
        }
    }

    // Make textview gone to enable edit mode.
    private fun hideUserInfoTextView() {
        binding?.apply {
            userHeight.visibility = View.GONE
            userWeight.visibility = View.GONE
            userAge.visibility = View.GONE
            userActivityLeve.visibility = View.GONE
            userGender.visibility = View.GONE
            userNameTextview.visibility = View.GONE
        }
    }

    // Show user "trainee" info -- make textview visible.
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

    // Save updated user "trainee" info.
    private fun saveUserInfo(userInfo: User) {
        traineeViewModel.userInfo(userInfo)
    }

    // Get updated info from views "edittext".
    private fun getUpdatedUserInfo(oldUserInfo: User) =
        oldUserInfo.copy(
            firstName = getFirstName(),
            height = getHeight(),
            weight = getWeight(),
            age = getAge(),
            gender = getGender(),
            activity = getActivityLevel()
        )

    // To get updated info from views. 
    private fun getActivityLevel() = when (binding?.activityOptions?.checkedRadioButtonId) {
        R.id.option_0 -> getString(R.string.option_0)
        R.id.option_1 -> getString(R.string.option_1)
        R.id.option_2 -> getString(R.string.option_2)
        else -> getString(R.string.option_3)
    }

    private fun getGender() =
        if (binding?.femaleOption?.isChecked!!) getString(R.string.female) else getString(R.string.male)

    private fun getAge() = binding?.userAgeEditText?.text.toString()
    private fun getWeight() =
        if (binding?.kgOption?.isChecked!!) "${binding?.userWeightEditText?.text} ${getString(R.string.kg)}"
        else "${binding?.userWeightEditText?.text} ${getString(R.string.lb)}"

    private fun getHeight() =
        if (binding?.cmOption?.isChecked!!) "${binding?.userHeightEditText?.text?.toString()} ${
        getString(R.string.cm)
        }"
        else "${binding?.userHeightEditText?.text} ${getString(R.string.ft)}"

    private fun getFirstName() =
        binding?.userNameEditText?.text.toString()

    // Go to home page.
    private fun goToHomePage() =
        findNavController().navigate(R.id.action_userInfoSettingsFragment2_to_viewPagerFragment2)

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
