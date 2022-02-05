package com.rahafcs.co.rightway.ui.coach

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
import com.rahafcs.co.rightway.databinding.FragmentCoachesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoachesFragment : Fragment() {
    private var _binding: FragmentCoachesBinding? = null
    private val viewModel: CoachViewModel by activityViewModels()
//    {
//        ViewModelFactory(
//            ServiceLocator.provideWorkoutRepository(),
//            ServiceLocator.provideDefaultUserRepository(),
// //            ServiceLocator.provideAuthRepository()
//        )
//    }
    private var userType = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCoachesBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserType()
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            coachViewModel = viewModel
            recyclerview.adapter = CoachAdapter(userType)
        }
        viewModel.setCoachesList()
        handleLayout()
    }

    override fun onResume() {
        super.onResume()
        onBackPressedDispatcher()
        lifecycleScope.launch {
            viewModel.coachList.collect {
                val adapter = CoachAdapter(userType)
                _binding?.recyclerview?.adapter = adapter
                adapter.submitList(it.coachInfoUiState)
            }
        }
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

    // To get user type --> Trainer"Coach" or Trainee.
    private fun getUserType() =
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.getUserType().collect {
                    userType = it
                }
            }
        }

    // To handle layout status --> ERROR, LOADING, SUCCESS.
    private fun handleLayout() =
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.coachList.collect {
                    when (it.loadingState) {
                        LoadingStatus.ERROR -> {
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

    // To show success Layout.
    private fun shoeSuccessLayout() =
        _binding?.apply {
            error.visibility = View.GONE
            success.visibility = View.VISIBLE
            loading.visibility = View.GONE
        }

    // To show loading Layout.
    private fun showLoadingLayout() =
        _binding?.apply {
            error.visibility = View.GONE
            success.visibility = View.GONE
            loading.visibility = View.VISIBLE
        }

    // To show error Layout.
    private fun showErrorLayout() =
        _binding?.apply {
            error.visibility = View.VISIBLE
            success.visibility = View.GONE
            loading.visibility = View.GONE
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
