package com.rahafcs.co.rightway.ui.settings.coach

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
        CoachInfoUiState("Rahaf", "coach five years", "trainer1@gmail.com", "050000000", "100-300"),
        CoachInfoUiState("Nasser", "coach four years", "trainer2@gmail.com", "050000000", "100-200"),
        CoachInfoUiState("Noura", "coach two years", "trainer3@gmail.com", "050000000", "100-200")
    )
}
