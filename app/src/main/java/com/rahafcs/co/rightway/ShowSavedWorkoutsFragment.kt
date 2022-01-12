package com.rahafcs.co.rightway

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.rahafcs.co.rightway.databinding.FragmentShowSavedWorkoutsBinding
import com.rahafcs.co.rightway.ui.WorkoutHorizontalAdapter
import com.rahafcs.co.rightway.ui.WorkoutsFragment.Companion.listOfSavedWorkouts
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.utility.upToTop
import com.rahafcs.co.rightway.viewmodels.ViewModelFactory
import com.rahafcs.co.rightway.viewmodels.WorkoutsViewModel

class ShowSavedWorkoutsFragment : Fragment() {
    private var _binding: FragmentShowSavedWorkoutsBinding? = null
    val binding get() = _binding!!
    val viewModel by activityViewModels<WorkoutsViewModel> {
        ViewModelFactory(
            ServiceLocator.provideWorkoutRepository(),
            ServiceLocator.provideUserRepository()
        )
    }

    var adapter = WorkoutHorizontalAdapter("SavedWorkouts") { false }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentShowSavedWorkoutsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setListSavedWorkout()
        binding.apply {
            adapter = WorkoutHorizontalAdapter("SavedWorkouts") { workoutsInfoUiState ->
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
            lifecycleOwner = viewLifecycleOwner
            recyclerview.adapter = adapter
            adapter.submitList(viewModel.listSavedWorkout.value)
            Log.e("MainActiv", "onViewCreated: ${viewModel.listSavedWorkout.value}")
            backArrow.setOnClickListener { this@ShowSavedWorkoutsFragment.upToTop() }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.listSavedWorkout.observe(viewLifecycleOwner, {
            adapter.submitList(it)
            if (it.isEmpty()) {
                binding.noSavedWorkout.visibility = View.VISIBLE
                binding.savedWorkout.visibility = View.GONE
            } else {
                binding.noSavedWorkout.visibility = View.GONE
                binding.savedWorkout.visibility = View.VISIBLE
            }
        })
    }

    private fun checkIsSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState) =
        listOfSavedWorkouts.contains(workoutsInfoUiState)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
