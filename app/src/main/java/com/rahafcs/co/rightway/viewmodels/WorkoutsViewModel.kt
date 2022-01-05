package com.rahafcs.co.rightway.viewmodels

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahafcs.co.rightway.data.DefaultWorkoutsRepository
import com.rahafcs.co.rightway.data.UserRepository
import com.rahafcs.co.rightway.ui.state.ListWorkoutsUiState
import com.rahafcs.co.rightway.ui.state.WorkoutTypeUiState
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import com.rahafcs.co.rightway.ui.state.WorkoutsUiState
import com.rahafcs.co.rightway.utility.capitalizeFormatIfFirstLatterSmall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkoutsViewModel(
    private val workoutRepository: DefaultWorkoutsRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val tag = WorkoutsViewModel::class.java.name

    private val _listWorkoutsUiState = MutableStateFlow(ListWorkoutsUiState())
    val listWorkoutsUiState: MutableStateFlow<ListWorkoutsUiState> = _listWorkoutsUiState

    // TODO() set _workoutsInfoUiState a value
    private val _workoutsInfoUiState = MutableStateFlow(WorkoutsInfoUiState())
    val workoutsInfoUiState: MutableStateFlow<WorkoutsInfoUiState> = _workoutsInfoUiState

    init {
        getAllWorkouts()
        setWorkoutsInfoUiState(getWorkoutsInfoUiState())
    }

    fun setWorkoutsInfoUiState(workout: WorkoutsInfoUiState) {
        _workoutsInfoUiState.value = workout
        Log.d("TAG", "setWorkoutsInfoUiState: $workout")
    }

    private fun getAllWorkouts() {
        Log.d("TAG", "getAllWorkouts: First fun")
        viewModelScope.launch {
            try {
                val result = workoutRepository.getAllWorkouts()
                val listOfBodyParts = result.distinctBy { it.bodyPart }.map { it.bodyPart }
                val listOfWorkOts = result.map {
                    WorkoutsInfoUiState(
                        gifUrl = it.gifUrl,
                        name = it.name,
                        equipment = it.equipment,
                        target = it.target,
                        bodyPart = it.bodyPart
                    )
                }
                val workoutsUiState = listOfBodyParts.map {
                    WorkoutsUiState(
                        WorkoutTypeUiState(it),
                        listOfWorkOts.filter { workoutsInfoUiState -> workoutsInfoUiState.bodyPart == it }
                    )
                }

                Log.d("TAG", "getAllWorkouts: $workoutsUiState")
                _listWorkoutsUiState.update {
                    it.copy(workUiState = workoutsUiState)
                }
            } catch (e: Exception) {
                Log.d(tag, "getAllWorkouts: error $e")
                // _listWorkoutsUiState.value = ListWorkoutsUiState(listOf())
            }
        }
    }

//    fun addUserWorkout(workoutsInfoUiState: WorkoutsInfoUiState) =
//        userRepository.addUserWorkout(workoutsInfoUiState)
//
//    fun deleteWorkout(workoutsInfoUiState: WorkoutsInfoUiState) =
//        userRepository.deleteWorkout(workoutsInfoUiState)
//
//    fun isSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState) =
//        userRepository.isSavedWorkout(workoutsInfoUiState)

    // to test Firestore
    private fun getWorkoutsInfoUiState(): WorkoutsInfoUiState {
        return WorkoutsInfoUiState(
            "http://d205bpvrqc9yn1.cloudfront.net/0003.gif",
            "air bike".capitalizeFormatIfFirstLatterSmall(),
            "body weight",
            "abs",
            "waist".capitalizeFormatIfFirstLatterSmall()
        )
    }

    fun shareMessage(): String {
        return "Hi! I'm training now\nLook at my workout\n ${
        _workoutsInfoUiState.value.gifUrl.toUri().buildUpon().scheme("http").build()
        }\nWorkout name: ${_workoutsInfoUiState.value.name}\nBody part: ${_workoutsInfoUiState.value.bodyPart}"
    }
}

/*
*     private fun getAllWorkouts() {
        Log.d("TAG", "getAllWorkouts: First fun")
        viewModelScope.launch {
            try {
                val result = workoutRepository.getAllWorkouts()
                val listOfBodyParts = result.distinctBy { it.bodyPart }.map { it.bodyPart }
                val listOfWorkOts = result.map {
                    WorkoutsInfoUiState(
                        gifUrl = it.gifUrl,
                        name = it.name,
                        equipment = it.equipment,
                        target = it.target,
                        bodyPart = it.bodyPart
                    )
                }
                val workoutsUiState = listOfBodyParts.map {
                    WorkoutsUiState(
                        WorkoutTypeUiState(it),
                        listOfWorkOts.filter { workoutsInfoUiState -> workoutsInfoUiState.bodyPart == it }
                    )
                }

                Log.d("TAG", "getAllWorkouts: $workoutsUiState")
                _listWorkoutsUiState.update {
                    it.copy(workUiState = workoutsUiState)
                }
            } catch (e: Exception) {
                Log.d(tag, "getAllWorkouts: error $e")
                // _listWorkoutsUiState.value = ListWorkoutsUiState(listOf())
            }
        }
    }

* */
