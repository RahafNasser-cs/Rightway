package com.rahafcs.co.rightway.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentWelcomeBinding
import com.rahafcs.co.rightway.utility.Constant.FIRST_NAME
import com.rahafcs.co.rightway.utility.Constant.SUPERSCRIPTION


class WelcomeFragment : Fragment() {
    private var binding: FragmentWelcomeBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Welcome"
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            welcomeTextview.text = getWelcomeStatement()
            getStartedBtn.setOnClickListener {
                if (getUserSubscriptionStatus().equals(requireContext().getString(R.string.trainee), true)) {
                    goToUserInfoPage()
                } else {
                    goToCoachInfoPage()
                }
            }
        }
    }

    private fun goToUserInfoPage() {
        findNavController().navigate(R.id.action_welcomeFragment_to_userInfoFragment)
    }

    private fun goToCoachInfoPage() {
        findNavController().navigate(R.id.action_welcomeFragment_to_coachInfoFragment)
    }

    private fun getWelcomeStatement(): String {
        val sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        val userName = sharedPreferences.getString(FIRST_NAME, "").toString()
        return "Welcome $userName, you're in!"
    }

    private fun getUserSubscriptionStatus() =
        activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
            .getString(SUPERSCRIPTION, "")!!

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
