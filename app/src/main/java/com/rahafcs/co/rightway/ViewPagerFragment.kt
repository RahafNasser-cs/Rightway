package com.rahafcs.co.rightway

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.rahafcs.co.rightway.databinding.FragmentViewPagerBinding
import com.rahafcs.co.rightway.ui.auth.SignUpFragment
import com.rahafcs.co.rightway.ui.brows.BrowsFragment
import com.rahafcs.co.rightway.ui.coach.CoachesFragment
import com.rahafcs.co.rightway.ui.workout.WorkoutsFragment
import com.rahafcs.co.rightway.utility.toast

class ViewPagerFragment : Fragment() {
    private var _binding: FragmentViewPagerBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        val adapter = ViewPagerAdapter(
            getFragmentList(),
            childFragmentManager, // to avoid IllegalStateException
            lifecycle
        )
        binding.viewPager.isUserInputEnabled = false // to disable swiping
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Workouts"
                1 -> tab.text = "Brows"
                2 -> tab.text = "Coaches"
            }
        }.attach()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userInfo.setOnClickListener { goToInfoSettings() }
        binding.savedWorkout.setOnClickListener { goToSavedWorkoutsPage() }
        // binding?.logout?.setOnClickListener { signOut() }
    }

    private fun goToInfoSettings() =
        findNavController().navigate(R.id.action_viewPagerFragment2_to_userInfoSettingsFragment2)

    private fun goToSavedWorkoutsPage() =
        findNavController().navigate(R.id.action_viewPagerFragment2_to_showSavedWorkoutsFragment)

    private fun getFragmentList(): ArrayList<Fragment> = arrayListOf(
        WorkoutsFragment(),
        BrowsFragment(),
        CoachesFragment()
    )

    private fun signOut() {
        val sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        val editor = sharedPreferences.edit()
        editor.putBoolean(SignUpFragment.SIGN_IN, false)
        editor.apply()
        requireContext().toast("${FirebaseAuth.getInstance().currentUser?.email}")
        FirebaseAuth.getInstance().signOut()
        findNavController().navigate(R.id.registrationFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
