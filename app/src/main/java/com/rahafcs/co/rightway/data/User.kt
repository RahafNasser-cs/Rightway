package com.rahafcs.co.rightway.data

import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState

data class User(
    val firstName: String = "",
    val lastName: String = "",
    val subscriptionStatus: String = "",
    val weight: String = "",
    val height: String = "",
    val gender: String = "",
    val age: String = "",
    val activity: String = "",
    val savedWorkouts: List<WorkoutsInfoUiState> = listOf()
)
