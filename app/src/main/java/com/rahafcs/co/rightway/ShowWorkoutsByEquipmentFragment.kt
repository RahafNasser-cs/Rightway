package com.rahafcs.co.rightway

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
import com.rahafcs.co.rightway.data.LoadingStatus
import com.rahafcs.co.rightway.databinding.FragmentShowWorkoutsByEquipmentBinding
import com.rahafcs.co.rightway.ui.WorkoutHorizontalAdapter
import com.rahafcs.co.rightway.ui.WorkoutsFragment.Companion.listOfSavedWorkouts
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.utility.upToTop
import com.rahafcs.co.rightway.viewmodels.ViewModelFactory
import com.rahafcs.co.rightway.viewmodels.WorkoutsViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ShowWorkoutsByEquipmentFragment : Fragment() {

    private var binding: FragmentShowWorkoutsByEquipmentBinding? = null
    private val args: ShowWorkoutsByEquipmentFragmentArgs by navArgs()
    private val viewModel by activityViewModels<WorkoutsViewModel> {
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
        binding = FragmentShowWorkoutsByEquipmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getWorkoutsByEquipment(args.equipment)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            val adapter = WorkoutHorizontalAdapter("") { workoutsInfoUiState ->
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
            workoutsViewModel = viewModel
            workoutRecyclerview.adapter = adapter
            // adapter.submitList(viewModel.listOfWorkoutByEquipment.value)
            backArrow.setOnClickListener { this@ShowWorkoutsByEquipmentFragment.upToTop() }
        }
        loadingState()
        // adapter.submitList(viewModel.listOfWorkoutByEquipment.value) // I submit in xml
    }

    private fun loadingState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.browsWorkoutUiState.collect { browsWorkoutUiState ->
                    when (browsWorkoutUiState.loadingState) {
                        LoadingStatus.FAILURE -> {
                            binding?.apply {
                                loading.visibility = View.GONE
                                error.visibility = View.VISIBLE
                                errorMsg.text = browsWorkoutUiState.userMsg
                                success.visibility = View.GONE
                            }
                        }
                        LoadingStatus.SUCCESS -> {
                            binding?.apply {
                                loading.visibility = View.GONE
                                error.visibility = View.GONE
                                success.visibility = View.VISIBLE
                            }
                        }
                        LoadingStatus.LOADING -> {
                            binding?.apply {
                                loading.visibility = View.VISIBLE
                                error.visibility = View.GONE
                                success.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun checkIsSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState): Boolean {
        Log.e(
            "WorkoutFragment",
            "checkIsSavedWorkout: ${
            listOfSavedWorkouts.contains(workoutsInfoUiState)
            }",
        )
        return listOfSavedWorkouts.contains(workoutsInfoUiState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
