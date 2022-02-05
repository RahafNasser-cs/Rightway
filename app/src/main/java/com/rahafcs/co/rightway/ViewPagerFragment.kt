package com.rahafcs.co.rightway

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.rahafcs.co.rightway.databinding.FragmentViewPagerBinding
import com.rahafcs.co.rightway.ui.coach.CoachesFragment
import com.rahafcs.co.rightway.ui.workout.BrowsFragment
import com.rahafcs.co.rightway.ui.workout.WorkoutsFragment
import com.rahafcs.co.rightway.ui.workout.WorkoutsViewModel
import com.rahafcs.co.rightway.utility.Constant
import com.rahafcs.co.rightway.utility.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewPagerFragment : Fragment() {
    private var _binding: FragmentViewPagerBinding? = null
    val binding get() = _binding!!
    private val localeHelper = LocaleHelper()

    private val workoutsViewModel: WorkoutsViewModel by activityViewModels()
//    {
//        ViewModelFactory(
//            ServiceLocator.provideWorkoutRepository(),
//            ServiceLocator.provideDefaultUserRepository(),
// //            ServiceLocator.provideAuthRepository()
//        )
//    }
    private var userType = ""

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appLanguage()
        workoutsViewModel.getUserType() // to get user type--> trainer or trainee
        getUserType()
        reloadListOfSavedWorkouts()
        binding.userInfo.setOnClickListener {
            if (isTrainer()) {
                goToCoachInfoSettings()
            } else {
                goToTraineeInfoSettings()
            }
        }
        binding.savedWorkout.setOnClickListener { goToSavedWorkoutsPage() }
        binding.settings.setOnClickListener { goToSettings() }
    }

    // To set local language from sharedPreferences.
    private fun appLanguage() {
        val sharedPreferences =
            activity?.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)!!
        sharedPreferences.getString(
            Constant.LANGUAGE_CODE,
            getString(R.string.language_code_select)
        )?.let {
            localeHelper.setLocale(it, requireContext())
            onConfigurationChanged(localeHelper.configuration)
        }
    }

    override fun onResume() {
        super.onResume()
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = requireContext().getString(R.string.Workouts)
                1 -> tab.text = requireContext().getString(R.string.Brows)
                2 -> tab.text = requireContext().getString(R.string.Coaches)
            }
        }.attach()
        binding.homePage.text = getString(R.string.home)
    }

    // Check user type --> trainer "coach" or trainee.
    private fun isTrainer() =
        userType.equals(
            requireContext().getString(R.string.trainer_to_compare_en),
            true
        ) || userType.equals(
            requireContext().getString(R.string.trainer_to_compare_ar),
            true
        )

    // Go to settings page.
    private fun goToSettings() {
        val action =
            ViewPagerFragmentDirections.actionViewPagerFragment2ToSettingsFragment(userType)
        findNavController().navigate(action)
    }

    // Go to trainee settings.
    private fun goToTraineeInfoSettings() =
        findNavController().navigate(R.id.action_viewPagerFragment2_to_userInfoSettingsFragment2)

    // Go to coach settings.
    private fun goToCoachInfoSettings() =
        findNavController().navigate(R.id.action_viewPagerFragment2_to_coachInfoSettingsFragment)

    // Go to saved workouts.
    private fun goToSavedWorkoutsPage() =
        findNavController().navigate(R.id.action_viewPagerFragment2_to_showSavedWorkoutsFragment)

    // Get list of fragments to show it in viewpager.
    private fun getFragmentList(): ArrayList<Fragment> = arrayListOf(
        WorkoutsFragment(),
        BrowsFragment(),
        CoachesFragment()
    )

    // Get user type --> trainer "coach" or trainee.
    private fun getUserType() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                workoutsViewModel.getUserType().collect {
                    userType = it
                }
            }
        }
    }

    // Reload list of saved workouts from Firestore.
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
