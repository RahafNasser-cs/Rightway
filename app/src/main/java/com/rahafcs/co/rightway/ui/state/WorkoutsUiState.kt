package com.rahafcs.co.rightway.ui.state

data class WorkoutsUiState(
    val workoutBodyTargetUiState: WorkoutBodyTargetUiState = WorkoutBodyTargetUiState(),
    val workoutsInfoUiState: List<WorkoutsInfoUiState> = listOf()
)

data class WorkoutBodyTargetUiState(val bodyPart: String = "")
data class WorkoutsInfoUiState(
    val gifUrl: String = "",
    val name: String = "",
    val equipment: String = "",
    val target: String = ""
)

data class ListWorkUiState(
    val workUiState: List<WorkoutsUiState> = listOf(),
    val isSigned: Boolean = false
)
