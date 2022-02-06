package com.rahafcs.co.rightway.ui.workout

import android.os.Parcelable
import com.rahafcs.co.rightway.data.LoadingStatus
import kotlinx.android.parcel.Parcelize

data class WorkoutsUiState(
    val workoutTypeUiState: WorkoutTypeUiState = WorkoutTypeUiState(),
    val workoutsInfoUiState: List<WorkoutsInfoUiState> = listOf(),
)

data class WorkoutTypeUiState(val bodyPart: String = "")

@Parcelize
data class WorkoutsInfoUiState(
    val gifUrl: String = "",
    val name: String = "",
    val equipment: String = "",
    val target: String = "",
    val bodyPart: String = "",
    val isSaved: Boolean = false,
) : Parcelable

// Use it to WorkoutsFragment.
data class ListWorkoutsUiState(
    val workUiState: List<WorkoutsUiState> = listOf(),
    val loadingState: LoadingStatus = LoadingStatus.LOADING,
    val userMsg: String = "",
    val isSigned: Boolean = false,
)

// Use it to BrowsFragment.
data class BrowsWorkoutUiState(
    val workoutsUiState: WorkoutsUiState = WorkoutsUiState(),
    val loadingState: LoadingStatus = LoadingStatus.SUCCESS,
    val userMsg: String = "",
)
