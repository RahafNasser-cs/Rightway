package com.rahafcs.co.rightway.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahafcs.co.rightway.data.Workout
import com.rahafcs.co.rightway.data.WorkoutRepository
import com.rahafcs.co.rightway.ui.state.ListWorkUiState
import com.rahafcs.co.rightway.ui.state.WorkoutBodyTargetUiState
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import com.rahafcs.co.rightway.ui.state.WorkoutsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WorkoutsViewModel(private val repository: WorkoutRepository) : ViewModel() {
    private val tag = WorkoutsViewModel::class.java.name
    private var _workout = MutableLiveData<List<Workout>>()
    val workout: MutableLiveData<List<Workout>> get() = _workout

    private val _workoutsUiState = MutableStateFlow(WorkoutsUiState())
    val workoutsUiState: MutableStateFlow<WorkoutsUiState> = _workoutsUiState

    private val _listWorkoutsUiState = MutableStateFlow(ListWorkUiState())
    val listWorkoutsUiState: MutableStateFlow<ListWorkUiState> = _listWorkoutsUiState

//    private val _workoutBodyTargetUiState = MutableStateFlow(listOf(WorkoutBodyTargetUiState()))
//    val workoutBodyTargetUiState: MutableStateFlow<List<WorkoutBodyTargetUiState>> =
//        _workoutBodyTargetUiState
//    private val _workoutsInfoUiState = MutableStateFlow(listOf(WorkoutsInfoUiState()))
//    val workoutsInfoUiState: MutableStateFlow<List<WorkoutsInfoUiState>> = _workoutsInfoUiState

    init {
        getAllWorkouts()
    }

    private fun getAllWorkouts() {
        Log.d("TAG", "getAllWorkouts: Fist fun")
        viewModelScope.launch {
            try {
                val result = repository.getAllWorkouts()
                result.let {
                    _workout.value = it
                    Log.d("WorkoutViewModel", "getAllWorkouts: ${_workout.value?.toString()}")
                }
                val newsItem = getListWorkoutsUiState()
//                val workoutBodyTargetUiStateItem = getWorkoutBodyTargetUiState()
//                _workoutBodyTargetUiState.value = workoutBodyTargetUiStateItem
//                val workoutsInfoUiStateItems = getWorkoutsInfoUiState()
//                _workoutsInfoUiState.value = workoutsInfoUiStateItems
                _listWorkoutsUiState.value = newsItem
            } catch (e: Exception) {
                Log.d(tag, "getAllWorkouts: error $e")
                _workout.value = listOf()
            }
        }
    }

    private fun getListWorkoutsUiState(): ListWorkUiState {
        return ListWorkUiState(
            listOf(
                getWorkoutsUiState("Waist"),
                getWorkoutsUiState("Shoulders"),
                getWorkoutsUiState("Legs")
            ),
            true
        )
    }

    private fun getWorkoutsUiState(str: String): WorkoutsUiState {
        val workoutBodyTargetUiState = getWorkoutBodyTargetUiState(str)
        val workoutsInfoUiState = listOf(
            getWorkoutsInfoUiState(),
            getWorkoutsInfoUiState(),
            getWorkoutsInfoUiState()
        )
        return WorkoutsUiState(workoutBodyTargetUiState, workoutsInfoUiState)
    }

    private fun getWorkoutsInfoUiState(): WorkoutsInfoUiState {
        return WorkoutsInfoUiState(
            "http://d205bpvrqc9yn1.cloudfront.net/0003.gif",
            "air bike",
            "body weight",
            "abs"
        )
    }

    private fun getWorkoutBodyTargetUiState(str: String): WorkoutBodyTargetUiState {
        return WorkoutBodyTargetUiState(str)
    }
}
