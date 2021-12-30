package com.rahafcs.co.rightway.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahafcs.co.rightway.data.UserRepository
import com.rahafcs.co.rightway.data.Workout
import com.rahafcs.co.rightway.data.WorkoutRepository
import com.rahafcs.co.rightway.ui.state.ListWorkUiState
import com.rahafcs.co.rightway.ui.state.WorkoutBodyTargetUiState
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import com.rahafcs.co.rightway.ui.state.WorkoutsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkoutsViewModel(
    private val workoutRepository: WorkoutRepository,
    private val userRepository: UserRepository
) : ViewModel() {
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
                val result = workoutRepository.getAllWorkouts()
                val listOfBodyParts = result.distinctBy { it.bodyPart }.map { it.bodyPart }
                val listOfWorkOts = result.map {
                    WorkoutsInfoUiState(
                        it.gifUrl,
                        it.name,
                        it.equipment,
                        it.target,
                        it.bodyPart
                    )
                }
                val workoutsUiState = listOfBodyParts.map {
                    WorkoutsUiState(
                        WorkoutBodyTargetUiState(it),
                        listOfWorkOts.filter { workoutsInfoUiState -> workoutsInfoUiState.bodyPart == it }
                    )
                }

                Log.d("TAG", "getAllWorkouts: $workoutsUiState")
                _listWorkoutsUiState.update {
                    it.copy(workUiState = workoutsUiState)
                }
//                result.let {
//                    _workout.value = it
//                    Log.d("WorkoutViewModel", "getAllWorkouts: ${_workout.value?.toString()}")
//                }
//                val newsItem = getListWorkoutsUiState()
//                _listWorkoutsUiState.value = newsItem
            } catch (e: Exception) {
                Log.d(tag, "getAllWorkouts: error $e")
                _workout.value = listOf()
            }
        }
    }

    fun addUserWorkout(userName: String) {
        userRepository.addUserWorkout(getWorkoutsInfoUiState(), userName)
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
