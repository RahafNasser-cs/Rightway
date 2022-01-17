package com.rahafcs.co.rightway.viewmodels

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahafcs.co.rightway.data.DefaultWorkoutsRepository
import com.rahafcs.co.rightway.data.LoadingStatus
import com.rahafcs.co.rightway.data.User
import com.rahafcs.co.rightway.data.UserRepository
import com.rahafcs.co.rightway.ui.state.*
import com.rahafcs.co.rightway.utility.capitalizeFormatIfFirstLatterSmall
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WorkoutsViewModel(
    private val workoutRepository: DefaultWorkoutsRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val tag = WorkoutsViewModel::class.java.name

    private val _listWorkoutsUiState = MutableStateFlow(ListWorkoutsUiState())
    val listWorkoutsUiState: MutableStateFlow<ListWorkoutsUiState> = _listWorkoutsUiState

    // TODO() set _workoutsInfoUiState a value to show in WorkoutFragment
    private val _workoutsInfoUiState = MutableStateFlow(WorkoutsInfoUiState())
    val workoutsInfoUiState: MutableStateFlow<WorkoutsInfoUiState> = _workoutsInfoUiState

    // To Brows workout by equipment
    private var _browsWorkoutUiState = MutableStateFlow(BrowsWorkoutUiState())
    val browsWorkoutUiState: StateFlow<BrowsWorkoutUiState> get() = _browsWorkoutUiState

    // To show saved workout
    private val _listSavedWorkout = MutableLiveData<List<WorkoutsInfoUiState>>()
    val listSavedWorkout: MutableLiveData<List<WorkoutsInfoUiState>> get() = _listSavedWorkout

    // To define user status --> trainer or trainee
    private var _userStatus = MutableStateFlow("")
    val userStatus: MutableStateFlow<String> get() = _userStatus

    // To test
    private var _traineeList = MutableStateFlow(listOf<User>())
    val traineeList: MutableStateFlow<List<User>> get() = _traineeList

    init {
        // TODO() remove the comment from getAllWorkouts(), because number of request in api
        getAllWorkouts()
        // setWorkoutsInfoUiState(getWorkoutsInfoUiState())
    }

    fun setListSavedWorkout() {
        viewModelScope.launch {
            reloadListOfSavedWorkouts().collect {
                _listSavedWorkout.value = it
            }
        }
    }

    fun setWorkoutsInfoUiState(workout: WorkoutsInfoUiState) {
        _workoutsInfoUiState.value = workout
        Log.d("TAG", "setWorkoutsInfoUiState: $workout")
    }

    fun getWorkoutsByEquipment(equipment: String) {
        viewModelScope.launch {
            try {
                _browsWorkoutUiState.update { it.copy(loadingState = LoadingStatus.LOADING) }
                val result = if (equipment.isEmpty()) workoutRepository.getAllWorkouts()
                else
                    workoutRepository.getWorkoutsByEquipment(equipment)
                val type =
                    if (equipment.isEmpty()) "All Equipment" else equipment
                        .capitalizeFormatIfFirstLatterSmall()
                val list = result.map {
                    WorkoutsInfoUiState(
                        gifUrl = it.gifUrl,
                        name = it.name,
                        equipment = it.equipment,
                        target = it.target,
                        bodyPart = it.bodyPart
                    )
                }
                _browsWorkoutUiState.update {
                    it.copy(
                        workoutsUiState = WorkoutsUiState(
                            WorkoutTypeUiState(type), list
                        ),
                        loadingState = LoadingStatus.SUCCESS
                    )
                }
            } catch (e: Exception) {
                Log.e("WorkoutsViewModel", "getWorkoutsByEquipment: $e")
                _browsWorkoutUiState.update {
                    it.copy(
                        loadingState = LoadingStatus.FAILURE,
                        userMsg = "Error try again!"
                    )
                }
                Log.e(
                    "TAG",
                    "getWorkoutsByEquipment: ${_browsWorkoutUiState.value.loadingState}",
                )
            }
        }
    }

    private fun getAllWorkouts() {
        Log.d("TAG", "getAllWorkouts: First fun")
        viewModelScope.launch {
            try {
                _listWorkoutsUiState.update { it.copy(loadingState = LoadingStatus.LOADING) }
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
                    it.copy(workUiState = workoutsUiState, loadingState = LoadingStatus.SUCCESS)
                }
            } catch (e: Exception) {
                Log.d(tag, "getAllWorkouts: error $e")
                _listWorkoutsUiState.update {
                    it.copy(
                        loadingState = LoadingStatus.FAILURE,
                        userMsg = "Error tray again!"
                    )
                }
            }
        }
    }

    suspend fun reloadListOfSavedWorkouts(): Flow<List<WorkoutsInfoUiState>> =
        userRepository.reloadListOfSavedWorkouts()

    fun addListOfSavedWorkoutsLocal(workoutsInfoUiState: WorkoutsInfoUiState) =
        userRepository.addListOfSavedWorkoutsLocal(workoutsInfoUiState)

    fun removeListOfSavedWorkoutsLocal(workoutsInfoUiState: WorkoutsInfoUiState) =
        userRepository.removeListOfSavedWorkoutsLocal(workoutsInfoUiState)

    fun checkIsSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState) =
        userRepository.checkIsSavedWorkout(workoutsInfoUiState)

    fun getUserStatus(): Flow<String> = userRepository.getUserStatus()

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
