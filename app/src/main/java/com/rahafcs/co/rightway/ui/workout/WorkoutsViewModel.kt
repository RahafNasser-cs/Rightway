package com.rahafcs.co.rightway.ui.workout

import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahafcs.co.rightway.data.DefaultUserRepository
import com.rahafcs.co.rightway.data.DefaultWorkoutsRepository
import com.rahafcs.co.rightway.data.LoadingStatus
import com.rahafcs.co.rightway.ui.state.*
import com.rahafcs.co.rightway.utility.Constant.ALL_EQUIPMENT
import com.rahafcs.co.rightway.utility.Constant.ERROR_MESSAGE
import com.rahafcs.co.rightway.utility.capitalizeFormatIfFirstLatterSmall
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WorkoutsViewModel(
    private val workoutRepository: DefaultWorkoutsRepository,
    private val userRepository: DefaultUserRepository,
) : ViewModel() {

    // Use it to workouts fragment.
    private val _listWorkoutsUiState = MutableStateFlow(ListWorkoutsUiState())
    val listWorkoutsUiState: MutableStateFlow<ListWorkoutsUiState> = _listWorkoutsUiState

    // Use it to workouts details fragment.
    private val _workoutsInfoUiState = MutableStateFlow(WorkoutsInfoUiState())
    val workoutsInfoUiState: MutableStateFlow<WorkoutsInfoUiState> = _workoutsInfoUiState

    // Use it to Brows workout by equipment fragment.
    private var _browsWorkoutUiState = MutableStateFlow(BrowsWorkoutUiState())
    val browsWorkoutUiState: StateFlow<BrowsWorkoutUiState> get() = _browsWorkoutUiState

    // Use it to saved workouts fragment.
    private val _listSavedWorkout = MutableLiveData<List<WorkoutsInfoUiState>>()
    val listSavedWorkout: MutableLiveData<List<WorkoutsInfoUiState>> get() = _listSavedWorkout

    // To define user status --> trainer or trainee.
    private var _userStatus = MutableStateFlow("")

    init {
        getAllWorkouts()
    }

    // To set list od saved workouts from Firestore.
    fun setListSavedWorkout() {
        viewModelScope.launch {
            reloadListOfSavedWorkouts().collect {
                _listSavedWorkout.value = it
            }
        }
    }

    // Set workout info to show it in workouts details xml.
    fun setWorkoutsInfoUiState(workout: WorkoutsInfoUiState) {
        _workoutsInfoUiState.value = workout
    }

    // Get workouts by equipments.
    fun getWorkoutsByEquipment(equipment: String) {
        viewModelScope.launch {
            try {
                _browsWorkoutUiState.update { it.copy(loadingState = LoadingStatus.LOADING) }
                val result = if (equipment.isEmpty()) workoutRepository.getAllWorkouts()
                else
                    workoutRepository.getWorkoutsByEquipment(equipment)
                val type =
                    if (equipment.isEmpty()) ALL_EQUIPMENT else equipment
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
                _browsWorkoutUiState.update {
                    it.copy(
                        loadingState = LoadingStatus.ERROR,
                        userMsg = ERROR_MESSAGE
                    )
                }
            }
        }
    }

    // Get All workouts.
    private fun getAllWorkouts() {
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

                _listWorkoutsUiState.update {
                    it.copy(workUiState = workoutsUiState, loadingState = LoadingStatus.SUCCESS)
                }
            } catch (e: Exception) {
                _listWorkoutsUiState.update {
                    it.copy(
                        loadingState = LoadingStatus.ERROR,
                        userMsg = ERROR_MESSAGE
                    )
                }
            }
        }
    }

    // Get list of saved workouts from Firestore.
    suspend fun reloadListOfSavedWorkouts(): Flow<List<WorkoutsInfoUiState>> =
        userRepository.reloadListOfSavedWorkouts()

    // Add new workout to list of saved workouts.
    fun addListOfSavedWorkoutsLocal(workoutsInfoUiState: WorkoutsInfoUiState) =
        userRepository.addListOfSavedWorkoutsLocal(workoutsInfoUiState)

    // Remove workout from list of saved workouts.
    fun removeListOfSavedWorkoutsLocal(workoutsInfoUiState: WorkoutsInfoUiState) =
        userRepository.removeListOfSavedWorkoutsLocal(workoutsInfoUiState)

    // Check if  workoutsInfoUiState exists in list of saved workouts. 
    fun checkIsSavedWorkout(workoutsInfoUiState: WorkoutsInfoUiState) =
        userRepository.checkIsSavedWorkout(workoutsInfoUiState)

    // Get user type --> trainee or trainer "coach".
    fun getUserType(): Flow<String> = userRepository.getUserType()

    // Share workout.
    fun shareMessage(): String {
        return "Hi! I'm training now\nLook at my workout\n ${
        _workoutsInfoUiState.value.gifUrl.toUri().buildUpon().scheme("http").build()
        }\nWorkout name: ${_workoutsInfoUiState.value.name}\nBody part: ${_workoutsInfoUiState.value.bodyPart}"
    }
}
