package com.rahafcs.co.rightway

import android.content.Context
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
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.rahafcs.co.rightway.databinding.FragmentViewPagerBinding
import com.rahafcs.co.rightway.ui.brows.BrowsFragment
import com.rahafcs.co.rightway.ui.coach.CoachesFragment
import com.rahafcs.co.rightway.ui.workout.WorkoutsFragment
import com.rahafcs.co.rightway.utility.Constant.FIRST_NAME
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.viewmodels.ViewModelFactory
import com.rahafcs.co.rightway.viewmodels.WorkoutsViewModel
import kotlinx.coroutines.launch

class ViewPagerFragment : Fragment() {
    private var _binding: FragmentViewPagerBinding? = null
    val binding get() = _binding!!

    private val workoutsViewModel: WorkoutsViewModel by activityViewModels {
        ViewModelFactory(
            ServiceLocator.provideWorkoutRepository(),
            ServiceLocator.provideUserRepository(),
            ServiceLocator.provideCoachRepository()
        )
    }
    private var userStatus = ""

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
        workoutsViewModel.getUserStatus() // to get user status trainer or trainee
        getUserStatus()
        binding.userInfo.setOnClickListener {
            Log.e(
                "ViewPagerFragment",
                "onViewCreated: ${getUserSubscriptionStatus() == requireContext().getString(R.string.trainee)}",
            )
            Log.e("ViewPagerFragment", "onViewCreated: ${getUserSubscriptionStatus()}")
            if (userStatus.equals(requireContext().getString(R.string.trainer), true)) {
                Log.e(
                    "Viewpager",
                    "onViewCreated: ${FirebaseAuth.getInstance().currentUser?.email}",
                )
                goToCoachInfoSettings()
            } else {
                goToUserInfoSettings()
            }
        }
        binding.savedWorkout.setOnClickListener { goToSavedWorkoutsPage() }
    }

    private fun getUserSubscriptionStatus() =
        activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
            .getString(FIRST_NAME, "")!!

    private fun goToUserInfoSettings() =
        findNavController().navigate(R.id.action_viewPagerFragment2_to_userInfoSettingsFragment2)

    private fun goToCoachInfoSettings() =
        findNavController().navigate(R.id.action_viewPagerFragment2_to_coachInfoSettingsFragment)

    private fun goToSavedWorkoutsPage() =
        findNavController().navigate(R.id.action_viewPagerFragment2_to_showSavedWorkoutsFragment)

    private fun getFragmentList(): ArrayList<Fragment> = arrayListOf(
        WorkoutsFragment(),
        BrowsFragment(),
        CoachesFragment()
    )

    private fun getUserStatus() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                workoutsViewModel.getUserStatus().collect {
                    Log.e("ViewPagerFragment", "getUserStatus: $it ")
                    userStatus = it
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
