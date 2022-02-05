package com.rahafcs.co.rightway.ui.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.rahafcs.co.rightway.data.LoadingStatus
import com.rahafcs.co.rightway.databinding.FragmentWorkoutsBinding
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WorkoutsFragment : Fragment() {
    private var binding: FragmentWorkoutsBinding? = null
    private val viewModel: WorkoutsViewModel by activityViewModels()
    private lateinit var adapter: WorkoutVerticalAdapter

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
            workoutViewModel = viewModel
            adapter = WorkoutVerticalAdapter { workoutsInfoUiState ->
                if (!viewModel.checkIsSavedWorkout(workoutsInfoUiState)) {
                    viewModel.addListOfSavedWorkoutsLocal(workoutsInfoUiState)
                    true
                } else {
                    viewModel.removeListOfSavedWorkoutsLocal(workoutsInfoUiState)
                    false
                }
            }
            titleRecyclerview.adapter = adapter
        }
        handleLayout()
    }

    // Handel back press.
    private fun onBackPressedDispatcher() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )
    }

    // To handle layout status --> ERROR, LOADING, SUCCESS.
    private fun handleLayout() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.listWorkoutsUiState.collect {
                    when (it.loadingState) {
                        LoadingStatus.ERROR -> {
                            showErrorLayout()
                        }
                        LoadingStatus.LOADING -> {
                            showLoadingLayout()
                        }
                        LoadingStatus.SUCCESS -> {
                            showSuccessLayout()
                        }
                    }
                }
            }
        }
    }

    // To show success Layout.
    private fun showSuccessLayout() {
        binding?.apply {
            error.visibility = View.GONE
            success.visibility = View.VISIBLE
            loading.visibility = View.GONE
        }
    }

    // To show loading Layout.
    private fun showLoadingLayout() {
        binding?.apply {
            error.visibility = View.GONE
            success.visibility = View.GONE
            loading.visibility = View.VISIBLE
        }
    }

    // To show error Layout.
    private fun showErrorLayout() {
        binding?.apply {
            error.visibility = View.VISIBLE
            success.visibility = View.GONE
            loading.visibility = View.GONE
        }
    }

    // To redraw the layout.
    override fun onResume() {
        super.onResume()
        onBackPressedDispatcher()
        viewModel.setListSavedWorkout()
        viewModel.listSavedWorkout.observe(viewLifecycleOwner, {
            binding?.titleRecyclerview?.adapter = adapter
            lifecycleScope.launch {
                viewModel.listWorkoutsUiState.collect {
                    adapter.submitList(it.workUiState)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    // Local list to check saved workouts when create the recyclerview.
    companion object {
        var listOfSavedWorkouts = mutableListOf<WorkoutsInfoUiState>()
    }
}
