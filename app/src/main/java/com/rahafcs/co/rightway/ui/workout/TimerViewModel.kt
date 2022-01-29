package com.rahafcs.co.rightway.ui.workout

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {
    lateinit var timer: CountDownTimer
    private var _timerValue = MutableLiveData<Int>()
    private var _timerValueForProgressBar = MutableLiveData<Int>()
    private var _secondsLeft = MutableLiveData<Int>()
    private var _progressPercentage = MutableLiveData<Int>()
    val progressPercentage: MutableLiveData<Int> get() = _progressPercentage
    private var _finished = MutableLiveData<Boolean>()
    val finished: MutableLiveData<Boolean> get() = _finished
    private var _millisUntilFinished = MutableLiveData<Long>()
    val millisUntilFinished: MutableLiveData<Long> get() = _millisUntilFinished

    // To set timer value.
    fun setTimerValue(seconds: Int = 0) {
        _timerValue.value = seconds
        _timerValueForProgressBar.value = seconds
    }

    // To start the timer.
    fun startTimer() {
        timer = object : CountDownTimer((_timerValue.value?.times(1000))?.toLong()!!, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val timeLeft = millisUntilFinished / 1000
                _millisUntilFinished.value = millisUntilFinished
                _secondsLeft.value = timeLeft.toInt()
                updateProgressBarValue()
            }

            override fun onFinish() {
                _finished.value = true
            }
        }.start()
    }

    // To stop the timer.
    fun stopTimer() {
        timer.cancel()
        _millisUntilFinished.value = 0
        _timerValueForProgressBar.value = 0
        _progressPercentage.value = 0
        _timerValue.value = 0
        _secondsLeft.value = 0
    }

    // To pause the timer.
    fun pauseTimer() {
        timer.cancel()
        _timerValue.value = _secondsLeft.value
    }

    // To resume the timer.
    fun resumeTimer() {
        startTimer()
    }

    // Update progress bar value.
    fun updateProgressBarValue() {
        val factor = 100 / _timerValueForProgressBar.value!!
        _progressPercentage.value =
            ((_timerValueForProgressBar.value!! - _secondsLeft.value!!) * factor)
    }
}
