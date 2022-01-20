package com.rahafcs.co.rightway.ui.coach

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentCoachInfoSettingsBinding
import com.rahafcs.co.rightway.ui.state.CoachInfoUiState
import com.rahafcs.co.rightway.utility.Constant.SIGN_IN
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.ViewModelFactory

class CoachInfoSettingsFragment : Fragment() {
    private var _binding: FragmentCoachInfoSettingsBinding? = null
    val binding: FragmentCoachInfoSettingsBinding get() = _binding!!
    private val viewModel by activityViewModels<CoachViewModel> {
        ViewModelFactory(
            ServiceLocator.provideWorkoutRepository(),
            ServiceLocator.provideDefaultUserRepository()
        )
    }
    var isEditMode = false

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
                    showEditUserInfo(viewModel.coachInfoUiState.value)
                }
            }
        }
    }

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

    private fun saveUserInfo(updatedCoachInfo: CoachInfoUiState) {
        viewModel.saveCoachInfo(updatedCoachInfo)
    }

    private fun getUpdatedCoachInfo(oldCoachInfo: CoachInfoUiState) =
        oldCoachInfo.copy(
            name = getCoachName(),
            experience = getCoachExperience(),
            email = getCoachEmail(),
            phoneNumber = getCoachPhone(),
            price = getCoachRangePrice()
        )

    fun createCoach(newCoachInfo: CoachInfoUiState) =
        CoachInfoUiState(
            newCoachInfo.name,
            newCoachInfo.experience,
            newCoachInfo.email,
            newCoachInfo.phoneNumber,
            newCoachInfo.price,
            newCoachInfo.savedWorkouts
        )

    private fun getCoachName() = binding.coachNameEditText.text.toString()
    private fun getCoachEmail() = binding.emailEditText.text.toString()
    private fun getCoachExperience() = binding.experienceEditText.text.toString()
    private fun getCoachPhone() = binding.phoneEditText.text.toString()
    private fun getCoachRangePrice() = binding.rangePriceEditText.text.toString()

    private fun showUserInfoTextView() {
        binding.apply {
            coachNameTextview.visibility = View.VISIBLE
            coachExperience.visibility = View.VISIBLE
            coachEmail.visibility = View.VISIBLE
            coachPhone.visibility = View.VISIBLE
            coachRangePrice.visibility = View.VISIBLE
        }
    }

    private fun hideEditUserInfo() {
        binding.apply {
            coachNameInputLayout.visibility = View.GONE
            emailInputLayout.visibility = View.GONE
            experienceInputLayout.visibility = View.GONE
            phoneInputLayout.visibility = View.GONE
            rangePriceInputLayout.visibility = View.GONE
            saveBtn.visibility = View.GONE
        }
    }

    private fun representUserInfoIntoEditText(coachInfo: CoachInfoUiState) {
        binding.apply {
            coachNameEditText.setText(coachInfo.name)
            emailEditText.setText(coachInfo.email)
            experienceEditText.setText(coachInfo.experience)
            phoneEditText.setText(coachInfo.phoneNumber)
            rangePriceEditText.setText(coachInfo.price)
        }
    }

    private fun makeEditTextVisible() {
        binding.apply {
            coachNameInputLayout.visibility = View.VISIBLE
            emailInputLayout.visibility = View.VISIBLE
            experienceInputLayout.visibility = View.VISIBLE
            phoneInputLayout.visibility = View.VISIBLE
            rangePriceInputLayout.visibility = View.VISIBLE
            saveBtn.visibility = View.VISIBLE
        }
    }

    private fun hideUserInfoTextView() {
        binding.apply {
            coachNameTextview.visibility = View.GONE
            coachExperience.visibility = View.GONE
            coachEmail.visibility = View.GONE
            coachPhone.visibility = View.GONE
            coachRangePrice.visibility = View.GONE
        }
    }

    private fun goToHomePage() =
        findNavController().navigate(R.id.action_coachInfoSettingsFragment_to_viewPagerFragment2)

    private fun signOutConfirmDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Sign Out")
            .setMessage("Are you sure you want to sign out?")
            .setNegativeButton("Cancel") { _, _ -> }
            .setPositiveButton("Sign out") { _, _ ->
                signOut()
            }.show()
    }

    private fun signOut() {
        val sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        val editor = sharedPreferences.edit()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
