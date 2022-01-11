package com.rahafcs.co.rightway.ui.state

import android.os.Parcelable
import com.rahafcs.co.rightway.data.LoadingStatus
import kotlinx.android.parcel.Parcelize

data class WorkoutsUiState(
    val workoutTypeUiState: WorkoutTypeUiState = WorkoutTypeUiState(),
    val workoutsInfoUiState: List<WorkoutsInfoUiState> = listOf()
)

data class WorkoutTypeUiState(val bodyPart: String = "")

// @Entity(tableName = "WorkoutsInfo")
@Parcelize
data class WorkoutsInfoUiState(
    // @PrimaryKey(autoGenerate = true)val id: Int = 0,
    val gifUrl: String = "",
    val name: String = "",
    val equipment: String = "",
    val target: String = "",
    val bodyPart: String = "",
    val isSaved: Boolean = false
) : Parcelable

data class ListWorkoutsUiState(
    val workUiState: List<WorkoutsUiState> = listOf(),
    val loadingState: LoadingStatus = LoadingStatus.LOADING,
    val isSigned: Boolean = false
)

data class CoachInfoUiState(
    val name: String = "",
    val experience: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val price: String = ""
)

data class BrowsWorkoutUiState(
    val workoutsUiState: WorkoutsUiState = WorkoutsUiState(),
    val loadingState: LoadingStatus = LoadingStatus.SUCCESS,
    val userMsg: String = ""
)
