package com.rahafcs.co.rightway.ui.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentSavedWorkoutsBinding
import com.rahafcs.co.rightway.utility.upToTop
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedWorkoutsFragment : Fragment() {
    private var _binding: FragmentSavedWorkoutsBinding? = null
    val binding get() = _binding!!
    val viewModel by activityViewModels<WorkoutsViewModel>()
    private lateinit var adapter: WorkoutHorizontalAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSavedWorkoutsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setListSavedWorkout()
        binding.apply {
            adapter =
                WorkoutHorizontalAdapter(getString(R.string.saved_workouts_fragment)) { workoutsInfoUiState ->
                    if (!viewModel.checkIsSavedWorkout(workoutsInfoUiState)) {
                        viewModel.addListOfSavedWorkoutsLocal(workoutsInfoUiState)
                        true
                    } else {
                        viewModel.removeListOfSavedWorkoutsLocal(workoutsInfoUiState)
                        false
                    }
                }
            lifecycleOwner = viewLifecycleOwner
            recyclerview.adapter = adapter
            backArrow.setOnClickListener { this@SavedWorkoutsFragment.upToTop() }
        }
    }

    override fun onResume() {
        super.onResume()
        onBackPressedDispatcher()
        viewModel.listSavedWorkout.observe(viewLifecycleOwner, {
            adapter.submitList(it)
            if (it.isEmpty()) {
                showLayoutDumbbellAnimation()
            } else {
                showLayoutOfSavedWorkouts()
            }
        })
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

    // To show saved workouts.
    private fun showLayoutOfSavedWorkouts() {
        binding.noSavedWorkout.visibility = View.GONE
        binding.savedWorkout.visibility = View.VISIBLE
    }

    // If there is no saved workout --> show animation.
    private fun showLayoutDumbbellAnimation() {
        binding.noSavedWorkout.visibility = View.VISIBLE
        binding.savedWorkout.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
