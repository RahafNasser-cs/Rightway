package com.rahafcs.co.rightway.viewmodels

import androidx.lifecycle.ViewModel
import com.rahafcs.co.rightway.ui.state.CoachInfoUiState
import kotlinx.coroutines.flow.MutableStateFlow

class CoachViewModel : ViewModel() {
    private var _coachList = MutableStateFlow(listOf(CoachInfoUiState()))
    val coachList: MutableStateFlow<List<CoachInfoUiState>> get() = _coachList

    init {
        setCoachList()
    }

    private fun setCoachList() {
        _coachList.value = getCoachList()
    }

    private fun getCoachList(): List<CoachInfoUiState> = listOf(
        CoachInfoUiState("Rahaf", "coach five years", "050000000", "100-200"),
        CoachInfoUiState("Rahaf", "coach five years", "050000000", "100-200"),
        CoachInfoUiState("Rahaf", "coach five years", "050000000")
    )
}
