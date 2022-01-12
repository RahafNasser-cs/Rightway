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
import com.rahafcs.co.rightway.data.LoadingStatus
import com.rahafcs.co.rightway.databinding.FragmentWorkoutsBinding
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.viewmodels.ViewModelFactory
import com.rahafcs.co.rightway.viewmodels.WorkoutsViewModel
import kotlinx.coroutines.flow.collect
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
                if (!checkIsSavedWorkout(workoutsInfoUiState)) {
                    listOfSavedWorkouts.add(workoutsInfoUiState)
                    viewModel.addUserWorkout(listOfSavedWorkouts)
                    true
                } else {
                    listOfSavedWorkouts.remove(workoutsInfoUiState)
                    viewModel.deleteWorkout(listOfSavedWorkouts)
                    false
                }
            }
        }
        handleLayout()
        // reloadListOfSavedWorkouts from Firestore 
        reloadListOfSavedWorkouts()
    }

    private fun handleLayout() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.listWorkoutsUiState.collect {
                    when (it.loadingState) {
                        LoadingStatus.FAILURE -> {
                            showErrorLayout()
                        }
                        LoadingStatus.LOADING -> {
                            showLoadingLayout()
                        }
                        LoadingStatus.SUCCESS -> {
                            shoeSuccessLayout()
                        }
                    }
                }
            }
        }
    }

    private fun shoeSuccessLayout() {
        binding?.apply {
            error.visibility = View.GONE
            success.visibility = View.VISIBLE
            loading.visibility = View.GONE
        }
    }

    private fun showLoadingLayout() {
        binding?.apply {
            error.visibility = View.GONE
            success.visibility = View.GONE
            loading.visibility = View.VISIBLE
        }
    }

    private fun showErrorLayout() {
        binding?.apply {
            error.visibility = View.VISIBLE
            success.visibility = View.GONE
            loading.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun checkIsSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState) =
        listOfSavedWorkouts.contains(workoutsInfoUiState)

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
