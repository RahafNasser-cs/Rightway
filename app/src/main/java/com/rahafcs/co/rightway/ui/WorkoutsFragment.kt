package com.rahafcs.co.rightway.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.rahafcs.co.rightway.databinding.FragmentWorkoutsBinding
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.viewmodels.ViewModelFactory
import com.rahafcs.co.rightway.viewmodels.WorkoutsViewModel

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
                val savedWorkout = workoutsInfoUiState.copy(isSaved = true)
                listOfSavedWorkouts.add(savedWorkout)
                viewModel.addUserWorkout(listOfSavedWorkouts)
            }
        }
        // to test Firestore
        // addUserWorkout()
    }

    private fun addUserWorkout() {
        val sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)!!
        // sharedPreferences.getString(FIRST_NAME, "")?.let { viewModel.addUserWorkout(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        var listOfSavedWorkouts = mutableListOf<WorkoutsInfoUiState>()
    }
}
