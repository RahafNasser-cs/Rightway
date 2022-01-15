package com.rahafcs.co.rightway.ui.settings.coach

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
import com.rahafcs.co.rightway.ui.auth.SignUpFragment
import com.rahafcs.co.rightway.ui.coach.CoachesFragment.Companion.coachesEmail
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.viewmodels.ViewModelFactory

class CoachInfoSettingsFragment : Fragment() {
    private var _binding: FragmentCoachInfoSettingsBinding? = null
    val binding: FragmentCoachInfoSettingsBinding get() = _binding!!
    private val viewModel by activityViewModels<CoachViewModel> {
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
        _binding = FragmentCoachInfoSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            coachViewModel = viewModel
            homeImg.setOnClickListener { goToHomePage() }
            logoutImg.setOnClickListener { signOutConfirmDialog() }
        }
        if (coachesEmail.contains(FirebaseAuth.getInstance().currentUser?.email)) {
            viewModel.readCoachInfo(true)
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
                editor.putBoolean(SignUpFragment.SIGN_IN, false)
                editor.apply()
                findNavController().navigate(R.id.registrationFragment)
            }.addOnFailureListener {
            }
    }

    private fun getUserSubscriptionStatus() =
        activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
            .getString(SignUpFragment.FIRST_NAME, "")!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
