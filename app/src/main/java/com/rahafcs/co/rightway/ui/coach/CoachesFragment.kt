package com.rahafcs.co.rightway.ui.coach

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rahafcs.co.rightway.databinding.FragmentCoachesBinding
import com.rahafcs.co.rightway.ui.settings.coach.CoachViewModel
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.viewmodels.ViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CoachesFragment : Fragment() {
    private var _binding: FragmentCoachesBinding? = null
    val binding: FragmentCoachesBinding get() = _binding!!
    private val viewModel: CoachViewModel by activityViewModels() {
        ViewModelFactory(
            ServiceLocator.provideWorkoutRepository(),
            ServiceLocator.provideUserRepository(),
            ServiceLocator.provideCoachRepository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCoachesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            coachViewModel = viewModel
            recyclerview.adapter = CoachAdapter()
        }
        viewModel.setCoachesList()
        reloadListOfCoachesEmail()
    }

    override fun onResume() {
        super.onResume()
        viewModel.coachList.observe(viewLifecycleOwner, {
            val adapter = CoachAdapter()
            binding.recyclerview.adapter = adapter
            adapter.submitList(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun reloadListOfCoachesEmail() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.reloadCoachEmailList().collect {
                    coachesEmail = it.toMutableList()
                }
            }
        }
    }

    companion object {
        var coachesEmail = mutableListOf<String>()
    }
}
