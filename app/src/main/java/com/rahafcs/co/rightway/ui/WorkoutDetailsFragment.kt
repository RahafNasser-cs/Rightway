package com.rahafcs.co.rightway.ui

import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat.Token.fromBundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.rahafcs.co.rightway.databinding.FragmentWorkoutDetailsBinding
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.viewmodels.ViewModelFactory
import com.rahafcs.co.rightway.viewmodels.WorkoutsViewModel

class WorkoutDetailsFragment : Fragment() {
    val args: WorkoutDetailsFragmentArgs by navArgs()

    private var binding: FragmentWorkoutDetailsBinding? = null
    private val viewModel: WorkoutsViewModel by activityViewModels<WorkoutsViewModel> {
        ViewModelFactory(
            ServiceLocator.provideWorkoutRepository(),
            ServiceLocator.provideUserRepository()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            workoutViewModel = viewModel
        }
        viewModel.setWorkoutsInfoUiState(args.workoutInfoUiState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
