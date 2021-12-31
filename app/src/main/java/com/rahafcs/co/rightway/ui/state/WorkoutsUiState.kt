package com.rahafcs.co.rightway.ui.state

data class WorkoutsUiState(
    val workoutTypeUiState: WorkoutTypeUiState = WorkoutTypeUiState(),
    val workoutsInfoUiState: List<WorkoutsInfoUiState> = listOf()
)

data class WorkoutTypeUiState(val bodyPart: String = "")

// @Entity(tableName = "WorkoutsInfo")
data class WorkoutsInfoUiState(
    // @PrimaryKey(autoGenerate = true)val id: Int = 0,
    val gifUrl: String = "",
    val name: String = "",
    val equipment: String = "",
    val target: String = "",
    val bodyPart: String = ""
)

data class ListWorkoutsUiState(
    val workUiState: List<WorkoutsUiState> = listOf(),
    val isSigned: Boolean = false
)

data class CoachInfoUiState(
    val name: String = "",
    val experience: String = "",
    val phoneNumber: String = "",
    val price: String = ""
)
