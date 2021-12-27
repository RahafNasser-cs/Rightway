package com.rahafcs.co.rightway.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahafcs.co.rightway.data.Workout
import com.rahafcs.co.rightway.data.WorkoutRepository
import kotlinx.coroutines.launch

class WorkoutsViewModel(private val repository: WorkoutRepository) : ViewModel() {
    private val tag = WorkoutsViewModel::class.java.name
    private var _workout = MutableLiveData<List<Workout?>>()
    val workout: MutableLiveData<List<Workout?>> get() = _workout

    fun getAllWorkouts() {
        viewModelScope.launch {
            try {
                val result = repository.getAllWorkouts()
                result?.let {
                    _workout.value = it
                    Log.d("WorkoutViewModel", "getAllWorkouts: ${_workout.value.toString()}")
                }
            } catch (e: Exception) {
                Log.d(tag, "getAllWorkouts: error $e")
                _workout.value = listOf()
            }
        }
    }
}
