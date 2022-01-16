package com.rahafcs.co.rightway.data

import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState

data class Coach(
    val name: String = "",
    val experience: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val price: String = "",
    val savedWorkouts: List<WorkoutsInfoUiState> = listOf()
)

data class CoachEmail(
    val coachesEmail: List<String> = listOf()
)
