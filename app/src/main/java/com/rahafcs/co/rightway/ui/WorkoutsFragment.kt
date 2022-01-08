package com.rahafcs.co.rightway.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.rahafcs.co.rightway.databinding.FragmentWorkoutsBinding
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.viewmodels.ViewModelFactory
import com.rahafcs.co.rightway.viewmodels.WorkoutsViewModel
import kotlinx.coroutines.launch

class WorkoutsFragment : Fragment() {
    private var binding: FragmentWorkoutsBinding? = null
    private val viewModel: WorkoutsViewModel by activityViewModels<WorkoutsViewModel> {
        ViewModelFactory(
            ServiceLocator.provideWorkoutRepository(),
            ServiceLocator.provideUserRepository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWorkoutsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            workoutsFragment = this@WorkoutsFragment
            workoutViewModel = viewModel
            titleRecyclerview.adapter = WorkoutVerticalAdapter { workoutsInfoUiState ->
//                checkIsSavedWorkout(workoutsInfoUiState)
//                lifecycleScope.launch {
//                    try {
//                        if (viewModel.isSavedWorkout.value) {
//                            viewModel.deleteWorkout(workoutsInfoUiState)
//                            Log.e("WorkoutFragment", "onViewCreated: recycleview is false")
//                            false
//                        } else {
//                            val savedWorkout = workoutsInfoUiState.copy(isSaved = true)
//                            viewModel.addUserWorkout(savedWorkout)
//                            Log.e("WorkoutFragment", "onViewCreated: recycleview is true")
//                            true
//                        }
//                    } catch (e: Exception) {
//                    }
//                }
                if (!checkIsSavedWorkout(workoutsInfoUiState)) {
                    // val newItem = workoutsInfoUiState.copy(isSaved = true)
                    listOfSavedWorkouts.add(workoutsInfoUiState)
                    viewModel.addUserWorkout(listOfSavedWorkouts)
                    true
                } else {
                    // val newItem = workoutsInfoUiState.copy(isSaved = true)
                    listOfSavedWorkouts.remove(workoutsInfoUiState)
                    viewModel.deleteWorkout(listOfSavedWorkouts)
                    false
                }
            }
        }
        // reloadListOfSavedWorkouts from Firestore 
        reloadListOfSavedWorkouts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun checkIsSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState): Boolean {
        Log.e(
            "WorkoutFragment",
            "checkIsSavedWorkout: ${listOfSavedWorkouts.contains(workoutsInfoUiState)}",
        )
        return listOfSavedWorkouts.contains(workoutsInfoUiState)
    }

    private fun reloadListOfSavedWorkouts() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.reloadListOfSavedWorkouts().collect {
                    listOfSavedWorkouts = it.toMutableList()
                    Log.e("WorkoutFragment", "reloadListOfSavedWorkouts: $listOfSavedWorkouts")
                }
            }
        }
    }

    companion object {
        var listOfSavedWorkouts = mutableListOf<WorkoutsInfoUiState>()
    }
}
