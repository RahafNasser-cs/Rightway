package com.rahafcs.co.rightway.ui.state

import com.rahafcs.co.rightway.data.LoadingStatus

data class CoachInfoUiState(
    val name: String = "",
    val experience: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val price: String = "",
    val savedWorkouts: List<WorkoutsInfoUiState> = listOf(),
)

data class ListCoachInfoUiState(
    val coachInfoUiState: List<CoachInfoUiState> = listOf(),
    val loadingState: LoadingStatus = LoadingStatus.LOADING,
    val userMsg: String = "",
)
