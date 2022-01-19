package com.rahafcs.co.rightway.ui.coach

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
import com.rahafcs.co.rightway.databinding.FragmentCoachesBinding
import com.rahafcs.co.rightway.ui.settings.coach.CoachViewModel
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.viewmodels.ViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CoachesFragment : Fragment() {
    private var _binding: FragmentCoachesBinding? = null
    val binding: FragmentCoachesBinding get() = _binding!!
    private val viewModel: CoachViewModel by activityViewModels {
        ViewModelFactory(
            ServiceLocator.provideWorkoutRepository(),
            ServiceLocator.provideUserRepository(),
            ServiceLocator.provideCoachRepository()
        )
    }
    private var userType = ""

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
        getUserType()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            coachViewModel = viewModel
            recyclerview.adapter = CoachAdapter(userType)
        }
        viewModel.setCoachesList()
        handleLayout()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.coachList.collect {
                val adapter = CoachAdapter(userType)
                binding.recyclerview.adapter = adapter
                adapter.submitList(it.coachInfoUiState)
            }
        }
    }

    private fun getUserType() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.getUserType().collect {
                    Log.e("ViewPagerFragment", "getUserStatus: $it ")
                    userType = it
                }
            }
        }
    }

    private fun handleLayout() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.coachList.collect {
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
        binding.apply {
            error.visibility = View.GONE
            success.visibility = View.VISIBLE
            loading.visibility = View.GONE
        }
    }

    private fun showLoadingLayout() {
        binding.apply {
            error.visibility = View.GONE
            success.visibility = View.GONE
            loading.visibility = View.VISIBLE
        }
    }

    private fun showErrorLayout() {
        binding.apply {
            error.visibility = View.VISIBLE
            success.visibility = View.GONE
            loading.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
