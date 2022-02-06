package com.rahafcs.co.rightway.ui.coach

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
import com.rahafcs.co.rightway.databinding.FragmentCoachInfoSettingsBinding
import com.rahafcs.co.rightway.ui.auth.AuthViewModel
import com.rahafcs.co.rightway.ui.state.CoachInfoUiState
import com.rahafcs.co.rightway.utility.Constant.SIGN_IN
import com.rahafcs.co.rightway.utility.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoachInfoSettingsFragment : Fragment() {
    private var _binding: FragmentCoachInfoSettingsBinding? = null
    val binding: FragmentCoachInfoSettingsBinding get() = _binding!!
    private val coachViewModel by activityViewModels<CoachViewModel>()
    private val authViewModel by activityViewModels<AuthViewModel>()
    private var isEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCoachInfoSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coachViewModel.getCoachInfo()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            coachViewModel = coachViewModel
            homeImg.setOnClickListener { goToHomePage() }
            logoutImg.setOnClickListener { signOutConfirmDialog() }
            editImg.setOnClickListener {
                if (!isEditMode) {
                    isEditMode = true
                    it.visibility = View.GONE
                    binding.closeImg.visibility = View.VISIBLE
                    hideUserInfoTextView()
                    readCoachInfo()
                }
            }
        }
        readCoachInfo()
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

    // To get user "coach" info then show it in edittext.
    private fun readCoachInfo() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                coachViewModel.coachInfoUiState.collect {
                    if (isEditMode)
                        showEditUserInfo(it)
                    else
                        showUserInfo(it)
                }
            }
        }
    }

    // Show views "edittext" to enable edit info.
    private fun showEditUserInfo(coachInfo: CoachInfoUiState) {
        makeEditTextVisible()
        binding.apply {
            representUserInfoIntoEditText(coachInfo)
            closeImg.setOnClickListener {
                if (isEditMode)
                    cancelEditInfo()
            }
            saveBtn.setOnClickListener {
                if (isEditMode)
                    saveEditInfo(coachInfo)
            }
        }
    }

    // To complete edit process.
    private fun saveEditInfo(coachInfo: CoachInfoUiState) {
        isEditMode = false
        binding.closeImg.visibility = View.GONE
        binding.editImg.visibility = View.VISIBLE
        saveUserInfo(getUpdatedCoachInfo(coachInfo))
        hideEditUserInfo()
        showUserInfoTextView()
    }

    // Cancel edit info.
    private fun cancelEditInfo() {
        isEditMode = false
        binding.closeImg.visibility = View.GONE
        binding.editImg.visibility = View.VISIBLE
        hideEditUserInfo()
        showUserInfoTextView()
    }

    // Save updated user "coach" info.
    private fun saveUserInfo(updatedCoachInfo: CoachInfoUiState) =
        coachViewModel.saveCoachInfo(updatedCoachInfo)

    // Get updated user "coach" info.
    private fun getUpdatedCoachInfo(oldCoachInfo: CoachInfoUiState) =
        oldCoachInfo.copy(
            name = getCoachName(),
            experience = getCoachExperience(),
            email = getCoachEmail(),
            phoneNumber = getCoachPhone(),
            price = getCoachRangePrice()
        )

    // To get updated user info from views "edittext".
    private fun getCoachName() = binding.coachNameEditText.text.toString()
    private fun getCoachEmail() = binding.emailEditText.text.toString()
    private fun getCoachExperience() = binding.experienceEditText.text.toString()
    private fun getCoachPhone() = binding.phoneEditText.text.toString()
    private fun getCoachRangePrice() = binding.rangePriceEditText.text.toString()

    // Show user info -- make textview visible.
    private fun showUserInfoTextView() =
        binding.apply {
            coachNameTextview.visibility = View.VISIBLE
            coachExperience.visibility = View.VISIBLE
            coachEmail.visibility = View.VISIBLE
            coachPhone.visibility = View.VISIBLE
            coachRangePrice.visibility = View.VISIBLE
        }

    // Show user "trainee" info in textview.
    private fun showUserInfo(coachInfo: CoachInfoUiState) {
        binding.apply {
            coachNameTextview.text = coachInfo.name
            coachExperience.text = coachInfo.experience
            coachEmail.text = coachInfo.email
            coachPhone.text = coachInfo.phoneNumber
            coachRangePrice.text = coachInfo.price
        }
    }

    // Make edittext gone to disable edit mode.
    private fun hideEditUserInfo() =
        binding.apply {
            coachNameInputLayout.visibility = View.GONE
            emailInputLayout.visibility = View.GONE
            experienceInputLayout.visibility = View.GONE
            phoneInputLayout.visibility = View.GONE
            rangePriceInputLayout.visibility = View.GONE
            saveBtn.visibility = View.GONE
        }

    // represent old user info into edit text.
    private fun representUserInfoIntoEditText(coachInfo: CoachInfoUiState) =
        binding.apply {
            coachNameEditText.setText(coachInfo.name)
            emailEditText.setText(coachInfo.email)
            experienceEditText.setText(coachInfo.experience)
            phoneEditText.setText(coachInfo.phoneNumber)
            rangePriceEditText.setText(coachInfo.price)
        }

    // Make edittext visible to enable edit mode.
    private fun makeEditTextVisible() =
        binding.apply {
            coachNameInputLayout.visibility = View.VISIBLE
            emailInputLayout.visibility = View.VISIBLE
            experienceInputLayout.visibility = View.VISIBLE
            phoneInputLayout.visibility = View.VISIBLE
            rangePriceInputLayout.visibility = View.VISIBLE
            saveBtn.visibility = View.VISIBLE
        }

    // Make textview gone to enable edit mode.
    private fun hideUserInfoTextView() =
        binding.apply {
            coachNameTextview.visibility = View.GONE
            coachExperience.visibility = View.GONE
            coachEmail.visibility = View.GONE
            coachPhone.visibility = View.GONE
            coachRangePrice.visibility = View.GONE
        }

    // To go home page.
    private fun goToHomePage() =
        findNavController().navigate(R.id.action_coachInfoSettingsFragment_to_viewPagerFragment2)

    // To confirm sign out process. 
    private fun signOutConfirmDialog() =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.sign_out))
            .setMessage(getString(R.string.confirm_sign_out))
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.sign_out)) { _, _ ->
                signOut()
            }.show()

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
