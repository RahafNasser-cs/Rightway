package com.rahafcs.co.rightway

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.rahafcs.co.rightway.databinding.FragmentShowWorkoutsByEquipmentBinding
import com.rahafcs.co.rightway.ui.WorkoutHorizontalAdapter
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.utility.upToTop
import com.rahafcs.co.rightway.viewmodels.ViewModelFactory
import com.rahafcs.co.rightway.viewmodels.WorkoutsViewModel

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
            val adapter = WorkoutHorizontalAdapter {
                false
            }
            workoutRecyclerview.adapter = adapter
            adapter.submitList(viewModel.listOfWorkoutByEquipment.value)
            backArrow.setOnClickListener { this@ShowWorkoutsByEquipmentFragment.upToTop() }
        }
        // adapter.submitList(viewModel.listOfWorkoutByEquipment.value) // I submit in xml
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
