package com.rahafcs.co.rightway.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentWelcomeBinding
import com.rahafcs.co.rightway.utility.Constant.FIRST_NAME
import com.rahafcs.co.rightway.utility.Constant.SUPERSCRIPTION
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    val binding: FragmentWelcomeBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            welcomeTextview.text = getWelcomeStatement()
            getStartedBtn.setOnClickListener {
                if (isTrainer()) {
                    goToCoachInfoPage()
                } else {
                    goToTraineeInfoPage()
                }
            }
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

    // Check user type --> trainer "coach" or trainee.
    private fun isTrainer() =
        getUserSubscriptionStatus().equals(
            requireContext().getString(R.string.trainer_to_compare_en),
            true
        ) || getUserSubscriptionStatus().equals(
            requireContext().getString(R.string.trainer_to_compare_ar),
            true
        )

    // Go to trainee info page.
    private fun goToTraineeInfoPage() =
        findNavController().navigate(R.id.action_welcomeFragment_to_userInfoFragment)

    // Go to coach info page.
    private fun goToCoachInfoPage() =
        findNavController().navigate(R.id.action_welcomeFragment_to_coachInfoFragment)

    // Get welcome statement.
    private fun getWelcomeStatement(): String {
        val sharedPreferences =
            activity?.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)!!
        val userName = sharedPreferences.getString(FIRST_NAME, "").toString()
        return getString(R.string.welcome, userName)
    }

    // Get user type --> trainer "coach" or trainee.
    private fun getUserSubscriptionStatus() =
        activity?.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)!!
            .getString(SUPERSCRIPTION, "")!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
