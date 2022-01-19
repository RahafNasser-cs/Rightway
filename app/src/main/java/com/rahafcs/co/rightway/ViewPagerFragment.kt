package com.rahafcs.co.rightway

import android.os.Bundle
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
import com.rahafcs.co.rightway.databinding.FragmentViewPagerBinding
import com.rahafcs.co.rightway.ui.brows.BrowsFragment
import com.rahafcs.co.rightway.ui.coach.CoachesFragment
import com.rahafcs.co.rightway.ui.workout.WorkoutsFragment
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
    private var userType = ""

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
                0 -> tab.text = requireContext().getString(R.string.Workouts)
                1 -> tab.text = requireContext().getString(R.string.Brows)
                2 -> tab.text = requireContext().getString(R.string.Coaches)
            }
        }.attach()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workoutsViewModel.getUserType() // to get user type--> trainer or trainee
        getUserType()
        reloadListOfSavedWorkouts()
        binding.userInfo.setOnClickListener {
            if (userType.equals(requireContext().getString(R.string.trainer), true)) {
                goToCoachInfoSettings()
            } else {
                goToUserInfoSettings()
            }
        }
        binding.savedWorkout.setOnClickListener { goToSavedWorkoutsPage() }
    }

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

    private fun getUserType() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                workoutsViewModel.getUserType().collect {
                    userType = it
                }
            }
        }
    }

    private fun reloadListOfSavedWorkouts() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                workoutsViewModel.reloadListOfSavedWorkouts().collect {
                    WorkoutsFragment.listOfSavedWorkouts = it.toMutableList()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
