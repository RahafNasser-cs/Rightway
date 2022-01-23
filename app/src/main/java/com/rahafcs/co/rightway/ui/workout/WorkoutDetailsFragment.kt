package com.rahafcs.co.rightway.ui.workout

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
import com.rahafcs.co.rightway.ViewModelFactory
import java.util.concurrent.TimeUnit

class WorkoutDetailsFragment : Fragment() {
    private val args: WorkoutDetailsFragmentArgs by navArgs()

    var isStarted = false
    var isPause = false
    var numberOfSeconds = 0
    var numberOfSecondsResume = 0
    var secondsRemaining = 0
    lateinit var countDownTimer: CountDownTimer

    init {
        createCountDownTimer(0)
    }

    private var binding: FragmentWorkoutDetailsBinding? = null
    private val viewModel: WorkoutsViewModel by activityViewModels<WorkoutsViewModel> {
        ViewModelFactory(
            ServiceLocator.provideWorkoutRepository(),
            ServiceLocator.provideDefaultUserRepository()
        )
    }

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
            workoutViewModel = viewModel
            backArrow.setOnClickListener { this@WorkoutDetailsFragment.upToTop() }
            shareImg.setOnClickListener { shareWorkout() }
            startBtn.setOnClickListener {
                if (numberOfSeconds == 0) {
                    requireContext().toast("Select a time")
                } else if (!isStarted && !isPause) {
                    createCountDownTimer(numberOfSeconds)
                    startTimer()
                } else if (isStarted && !isPause) {
                    Log.e("MainActivity", "onViewCreated: isStarted && !isPause")
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

                Log.d("MainActivity", "onTick: init $millisUntilFinished")
                val factor = 100 / numberOfSeconds
                Log.d("MainActivity", "onTick: millisUntilFinished ${millisUntilFinished / 1000}")

                secondsRemaining =
                    (millisUntilFinished / 1000).toInt() - numberOfSecondsResume // 8 -9
                val progressPercentage = (numberOfSeconds - secondsRemaining) * factor
                updateProgressBar(progressPercentage)
                Log.d("MainActivity", "onTick: ${millisUntilFinished / 1000f}")
                Log.d("MainActivity", "onTick: nof: $numberOfSeconds, sr:  $secondsRemaining")
                if (numberOfSeconds - secondsRemaining == numberOfSeconds) { // 8-7 ==1
                    Log.e("MainActivity", "onTick: cancel")
                    cancel()
                    binding?.startBtn?.text = "Start"
                    binding?.timerTextview?.text = "00:00"
                    isStarted = false
                    isPause = false
                }
                binding?.timerTextview?.text = requireContext().getString(
                    R.string.formatted_time,
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished - (numberOfSecondsResume * 1000)) % 60,
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished - (numberOfSecondsResume * 1000)) % 60
                )
            }

            override fun onFinish() {
                Log.e("MainActivity", "onFinish: cancel")
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
        countDownTimer.onFinish()
    }

    private fun pauseTimer() {
        isPause = true
        Log.e("MainActivity", "pauseTimer:$secondsRemaining ")
        numberOfSecondsResume = secondsRemaining
        countDownTimer.cancel()
        binding?.startBtn?.text = "Resume"
    }

    private fun resumeTimer(resumeTime: Int) {
        Log.e("MainActivity", "resumeTimer: resumeTimer $resumeTime")
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
