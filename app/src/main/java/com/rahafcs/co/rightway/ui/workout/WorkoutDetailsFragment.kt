package com.rahafcs.co.rightway.ui.workout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentWorkoutDetailsBinding
import com.rahafcs.co.rightway.utility.toast
import com.rahafcs.co.rightway.utility.upToTop
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class WorkoutDetailsFragment : Fragment() {

    private val timerViewModel by activityViewModels<TimerViewModel>()
    private val args: WorkoutDetailsFragmentArgs by navArgs()
    private var binding: FragmentWorkoutDetailsBinding? = null
    private val workoutsViewModel: WorkoutsViewModel by activityViewModels()
    private var isStarted = false
    private var isPause = false
    private var numberOfSeconds = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentWorkoutDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            workoutViewModel = workoutsViewModel
            backArrow.setOnClickListener { this@WorkoutDetailsFragment.upToTop() }
            shareImg.setOnClickListener { shareWorkout() }
            startBtn.setOnClickListener { timerStatus() }
            stopBtn.setOnClickListener { stopTimer() }
            plus.setOnClickListener { incrementNumberOfSeconds() }
            minus.setOnClickListener { decrementNumberOfSeconds() }
        }
        workoutsViewModel.setWorkoutsInfoUiState(args.workoutInfoUiState)
        updateProgressBar()
        updateTimerSecondsTextview()
        checkTimerIsFinished()
    }

    override fun onResume() {
        super.onResume()
        onBackPressedDispatcher()
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

    // To check timer is finished.
    private fun checkTimerIsFinished() {
        timerViewModel.finished.observe(viewLifecycleOwner, {
            if (it) {
                binding?.stopBtn?.isEnabled = false
                stopTimer()
            }
        })
    }

    // To update timer seconds in textview.
    private fun updateTimerSecondsTextview() {
        timerViewModel.millisUntilFinished.observe(viewLifecycleOwner, {
            binding?.timerTextview?.text = requireContext().getString(
                R.string.formatted_time,
                TimeUnit.MILLISECONDS.toMinutes(it) % 60,
                TimeUnit.MILLISECONDS.toSeconds(it) % 60
            )
        })
    }

    // Check of timer status.
    private fun timerStatus() {
        if (numberOfSeconds == 0) {
            requireContext().toast(getString(R.string.select_time))
        } else if (!isStarted && !isPause) {
            startTimer()
        } else if (isStarted && !isPause) {
            pauseTimer()
        } else {
            resumeTimer()
        }
    }

    // Increment timer.
    private fun incrementNumberOfSeconds() {
        numberOfSeconds += 1
        binding?.timeTextview?.text = numberOfSeconds.toString()
    }

    // Decrement timer.
    private fun decrementNumberOfSeconds() {
        if (numberOfSeconds != 0) {
            numberOfSeconds -= 1
            binding?.timeTextview?.text = numberOfSeconds.toString()
        }
    }

    // Share workout.
    private fun shareWorkout() {
        val intent = Intent(Intent.ACTION_SEND).putExtra(
            Intent.EXTRA_TEXT, workoutsViewModel.shareMessage()
        ).setType("text/plain")
        startActivity(intent)
    }

    // To start the timer.
    private fun startTimer() {
        binding?.stopBtn?.isEnabled = true
        timerViewModel.setTimerValue(numberOfSeconds)
        timerViewModel.startTimer()
        isStarted = true
        binding?.startBtn?.text = getString(R.string.pause)
    }

    // To stop the timer.
    private fun stopTimer() {
        isStarted = false
        isPause = false
        binding?.startBtn?.text = getString(R.string.start)
        binding?.timerTextview?.text = getString(R.string.reset_timer)
        binding?.stopBtn?.isEnabled = false
        timerViewModel.stopTimer()
    }

    // To pause the timer.
    private fun pauseTimer() {
        isPause = true
        timerViewModel.pauseTimer()
        binding?.startBtn?.text = getString(R.string.resume)
    }

    // To resume the timer.
    private fun resumeTimer() {
        isPause = false
        timerViewModel.resumeTimer()
        binding?.startBtn?.text = getString(R.string.pause)
    }

    // Update progress bar.
    private fun updateProgressBar() {
        timerViewModel.progressPercentage.observe(viewLifecycleOwner, {
            binding?.progressBar?.progress = it
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        timerViewModel.stopTimer()
    }
}
