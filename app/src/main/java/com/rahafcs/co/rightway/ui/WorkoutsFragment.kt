package com.rahafcs.co.rightway.ui

import android.content.Context
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
import com.rahafcs.co.rightway.databinding.FragmentWorkoutsBinding
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.viewmodels.ViewModelFactory
import com.rahafcs.co.rightway.viewmodels.WorkoutsViewModel
import kotlinx.coroutines.launch
import java.lang.Exception

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
        savedInstanceState: Bundle?
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
                checkIsSavedWorkout(workoutsInfoUiState)
                lifecycleScope.launch {
                    try {
                        if (viewModel.isSavedWorkout.value) {
                            viewModel.deleteWorkout(workoutsInfoUiState)
                            Log.e("WorkoutFragment", "onViewCreated: recycleview is false")
                            false
                        } else {
                            val savedWorkout = workoutsInfoUiState.copy(isSaved = true)
                            viewModel.addUserWorkout(savedWorkout)
                            Log.e("WorkoutFragment", "onViewCreated: recycleview is true")
                            true
                        }
                    } catch (e: Exception) {
                    }
                }

                false // to avoid crash

                // Or
//                if (!workoutsInfoUiState.isSaved) {
//                    val savedWorkout = workoutsInfoUiState.copy(isSaved = true)
//                    viewModel.addUserWorkout(savedWorkout)
//                    true
//                } else {
//                    viewModel.deleteWorkout(workoutsInfoUiState)
//                    false
//                }
            }
        }
        // to test Firestore
        // addUserWorkout()
    }

    //    suspend fun isSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState): Boolean {
//        return viewModel.isSavedWorkout(workoutsInfoUiState)
//    }
    private fun addUserWorkout() {
        val sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        // sharedPreferences.getString(FIRST_NAME, "")?.let { viewModel.addUserWorkout(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun checkIsSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.isSavedWorkout(workoutsInfoUiState)
            }
        }
    }

    companion object {
        var listOfSavedWorkouts = mutableListOf<WorkoutsInfoUiState>()
    }
}
