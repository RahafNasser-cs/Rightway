package com.rahafcs.co.rightway.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.databinding.FragmentWorkoutDetailsBinding
import com.rahafcs.co.rightway.utility.ServiceLocator
import com.rahafcs.co.rightway.utility.toast
import com.rahafcs.co.rightway.utility.upToTop
import com.rahafcs.co.rightway.viewmodels.ViewModelFactory
import com.rahafcs.co.rightway.viewmodels.WorkoutsViewModel
import java.util.concurrent.TimeUnit

class WorkoutDetailsFragment : Fragment() {
    private val args: WorkoutDetailsFragmentArgs by navArgs()

    var isStarted = false
    var isPause = false
    var isCancel = false
    var numberOfSeconds = 0
    var numberOfSecondsResume = 0
    var secondsRemaining = 0
    lateinit var countDownTimer: CountDownTimer

    private var binding: FragmentWorkoutDetailsBinding? = null
    private val viewModel: WorkoutsViewModel by activityViewModels<WorkoutsViewModel> {
        ViewModelFactory(
            ServiceLocator.provideWorkoutRepository(),
            ServiceLocator.provideUserRepository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorkoutDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            workoutViewModel = viewModel
            backArrow.setOnClickListener { this@WorkoutDetailsFragment.upToTop() }
            shareImg.setOnClickListener { shareWorkout() }
            startBtn.setOnClickListener {
                if (!isStarted && !isPause) {
                    createCountDownTimer(numberOfSeconds)
                    startTimer()
                } else if (isStarted && !isPause) {
                    pauseTimer()
                } else {
                    resumeTimer(numberOfSecondsResume)
                }
            }
            stopBtn.setOnClickListener { stopTimer() }
            // resumeBtn.setOnClickListener { resumeTimer(numberOfSecondsResume) }
            plus.setOnClickListener { incrementNumberOfSeconds() }
            minus.setOnClickListener { decrementNumberOfSeconds() }
        }
        Log.d("TAG", "onViewCreated: ${args.workoutInfoUiState}")
//        viewModel.setWorkoutsInfoUiState(bundle.getParcelable<WorkoutsInfoUiState>("WORKOUT")!!)
        viewModel.setWorkoutsInfoUiState(args.workoutInfoUiState)
    }

    private fun incrementNumberOfSeconds() {
        numberOfSeconds += 1
        binding?.timeTextview?.text = numberOfSeconds.toString()
    }

    private fun decrementNumberOfSeconds() {
        if (numberOfSeconds != 0) {
            numberOfSeconds -= 1
            binding?.timeTextview?.text = numberOfSeconds.toString()
        }
    }

    private fun shareWorkout() {
        val intent = Intent(Intent.ACTION_SEND).putExtra(
            Intent.EXTRA_TEXT, viewModel.shareMessage()
        ).setType("text/plain")
        startActivity(intent)
    }

    private fun createCountDownTimer(numberOfSeconds: Int) {

        countDownTimer = object : CountDownTimer(1000 * numberOfSeconds.toLong(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val factor = 100 / numberOfSeconds
                secondsRemaining = (millisUntilFinished / 1000).toInt() - numberOfSecondsResume
                val progressPercentage = (numberOfSeconds - secondsRemaining) * factor
                updateProgressBar(progressPercentage)
                Log.d("MainActivity", "onTick: ${millisUntilFinished / 1000f}")
                if (secondsRemaining == 0) {
                    cancel()
                    binding?.startBtn?.text = "Start"
                    binding?.timerTextview?.text = "00:00"
                    isStarted = false
                    isPause = false
                }
                binding?.timerTextview?.text = getString(
                    R.string.formatted_time,
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                )
            }

            override fun onFinish() {
                Log.d("MainActivity", "onFinish: cancel")
                isStarted = false
                isPause = false
                numberOfSecondsResume = secondsRemaining
                binding?.startBtn?.text = "Start"
            }
        }
    }

    private fun startTimer() {
        if (numberOfSeconds != 0) {
            countDownTimer.start()
            isStarted = true
            binding?.startBtn?.text = "Pause"
            // binding?.timerTextview?.text = "00:"
        } else {
            requireContext().toast("Chose a time")
        }
    }

    private fun stopTimer() {
        numberOfSecondsResume = 0
        isStarted = false
        isPause = false
        binding?.startBtn?.text = "Start"
        updateProgressBar(0)
        binding?.timerTextview?.text = "00:00"
        countDownTimer.cancel()
    }

    private fun pauseTimer() {
        isPause = true
        numberOfSecondsResume = secondsRemaining
        countDownTimer.cancel()
        binding?.startBtn?.text = "Resume"
    }

    fun resumeTimer(resumeTime: Int) {
        isPause = false
        binding?.startBtn?.text = "Pause"
        // createCountDownTimer(resumeTime)
        // startTimer()
        countDownTimer.onTick(1000 * resumeTime.toLong())
        countDownTimer.start()
    }

    fun updateProgressBar(progress: Int) {
        binding?.progressBar?.progress = progress
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        countDownTimer.cancel()
    }
}
