package com.rahafcs.co.rightway.data

import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState

interface WorkoutsRepository {
    suspend fun getAllWorkouts(forceUpdate: Boolean = false): Result<List<WorkoutsInfoUiState>>
}
