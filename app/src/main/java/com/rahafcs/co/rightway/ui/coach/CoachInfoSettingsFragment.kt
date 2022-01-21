package com.rahafcs.co.rightway.ui.coach

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.ViewModelFactory
import com.rahafcs.co.rightway.databinding.FragmentCoachInfoSettingsBinding
import com.rahafcs.co.rightway.ui.state.CoachInfoUiState
import com.rahafcs.co.rightway.utility.Constant.SIGN_IN
import com.rahafcs.co.rightway.utility.ServiceLocator
import kotlinx.coroutines.launch

class CoachInfoSettingsFragment : Fragment() {
    private var _binding: FragmentCoachInfoSettingsBinding? = null
    val binding: FragmentCoachInfoSettingsBinding get() = _binding!!
    private val viewModel by activityViewModels<CoachViewModel> {
        ViewModelFactory(
            ServiceLocator.provideWorkoutRepository(),
            ServiceLocator.provideDefaultUserRepository()
        )
    }
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
        viewModel.getCoachInfo()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            coachViewModel = viewModel
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
    }

    // To get user "coach" info then show it in edittext.
    private fun readCoachInfo() {
        lifecycleScope.launch {
            viewModel.coachInfoUiState.collect {
                if (isEditMode)
                    showEditUserInfo(it)
            }
        }
    }

    // Show views "edittext" to enable edit info.
    private fun showEditUserInfo(coachInfo: CoachInfoUiState) {
        makeEditTextVisible()
        binding.apply {
            representUserInfoIntoEditText(coachInfo)
            closeImg.setOnClickListener {
                if (isEditMode) {
                    isEditMode = false
                    it.visibility = View.GONE
                    binding.editImg.visibility = View.VISIBLE
                    hideEditUserInfo()
                    showUserInfoTextView()
                }
            }
            saveBtn.setOnClickListener {
                if (isEditMode) {
                    isEditMode = false
                    binding.closeImg.visibility = View.GONE
                    binding.editImg.visibility = View.VISIBLE
                    saveUserInfo(getUpdatedCoachInfo(coachInfo))
                    hideEditUserInfo()
                    showUserInfoTextView()
                }
            }
        }
    }

    // Save updated user "coach" info.
    private fun saveUserInfo(updatedCoachInfo: CoachInfoUiState) =
        viewModel.saveCoachInfo(updatedCoachInfo)

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
    private fun signOutConfirmDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.sign_out))
            .setMessage(getString(R.string.confirm_sign_out))
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.sign_out)) { _, _ ->
                signOut()
            }.show()
    }

    // To sign out.
    private fun signOut() {
        val sharedPreferences =
            activity?.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)!!
        val editor = sharedPreferences.edit()
        AuthUI.getInstance()
            .signOut(requireContext()).addOnSuccessListener {
                FirebaseAuth.getInstance().signOut()
                editor.putBoolean(SIGN_IN, false)
                editor.apply()
                findNavController().navigate(R.id.registrationFragment)
            }.addOnFailureListener {
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
