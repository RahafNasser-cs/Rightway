package com.rahafcs.co.rightway.ui.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.ViewModelFactory
import com.rahafcs.co.rightway.data.LoadingStatus
import com.rahafcs.co.rightway.databinding.FragmentWorkoutsByEquipmentBinding
import com.rahafcs.co.rightway.ui.state.BrowsWorkoutUiState
import com.rahafcs.co.rightway.ui.workout.WorkoutsFragment.Companion.listOfSavedWorkouts
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.utility.upToTop
import kotlinx.coroutines.launch

class WorkoutsByEquipmentFragment : Fragment() {

    private var binding: FragmentWorkoutsByEquipmentBinding? = null
    private val args: WorkoutsByEquipmentFragmentArgs by navArgs()
    private val viewModel by activityViewModels<WorkoutsViewModel> {
        ViewModelFactory(
            ServiceLocator.provideWorkoutRepository(),
            ServiceLocator.provideDefaultUserRepository(),
            ServiceLocator.provideAuthRepository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWorkoutsByEquipmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reloadListOfSavedWorkouts()
        viewModel.getWorkoutsByEquipment(args.equipment)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            val adapter =
                WorkoutHorizontalAdapter(getString(R.string.workouts_by_equipment_fragment)) { workoutsInfoUiState ->
                    if (!viewModel.checkIsSavedWorkout(workoutsInfoUiState)) {
                        viewModel.addListOfSavedWorkoutsLocal(workoutsInfoUiState)
                        true
                    } else {
                        viewModel.removeListOfSavedWorkoutsLocal(workoutsInfoUiState)
                        false
                    }
                }
            workoutsViewModel = viewModel
            workoutRecyclerview.adapter = adapter
            backArrow.setOnClickListener { this@WorkoutsByEquipmentFragment.upToTop() }
        }
        handleLayout()
    }

    // To handle layout status --> ERROR, LOADING, SUCCESS.
    private fun handleLayout() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.browsWorkoutUiState.collect { browsWorkoutUiState ->
                    when (browsWorkoutUiState.loadingState) {
                        LoadingStatus.ERROR -> {
                            showErrorLayout(browsWorkoutUiState)
                        }
                        LoadingStatus.SUCCESS -> {
                            showSuccessLayout()
                        }
                        LoadingStatus.LOADING -> {
                            showLoadingLayout()
                        }
                    }
                }
            }
        }
    }

    // To show loading Layout.
    private fun showLoadingLayout() {
        binding?.apply {
            loading.visibility = View.VISIBLE
            error.visibility = View.GONE
            success.visibility = View.GONE
        }
    }

    // To show success Layout.
    private fun showSuccessLayout() {
        binding?.apply {
            loading.visibility = View.GONE
            error.visibility = View.GONE
            success.visibility = View.VISIBLE
        }
    }

    // To show error Layout.
    private fun showErrorLayout(browsWorkoutUiState: BrowsWorkoutUiState) {
        binding?.apply {
            loading.visibility = View.GONE
            error.visibility = View.VISIBLE
            errorMsg.text = browsWorkoutUiState.userMsg
            success.visibility = View.GONE
        }
    }

    // Reload list of saved workouts from Firestore.
    private fun reloadListOfSavedWorkouts() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.reloadListOfSavedWorkouts().collect {
                    listOfSavedWorkouts = it.toMutableList()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
