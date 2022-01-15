package com.rahafcs.co.rightway.ui.settings.coach

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahafcs.co.rightway.data.Coach
import com.rahafcs.co.rightway.data.CoachRepository
import com.rahafcs.co.rightway.ui.state.CoachInfoUiState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CoachViewModel(private val coachRepository: CoachRepository) : ViewModel() {
    private var _coachList = MutableLiveData(listOf<CoachInfoUiState>())
    val coachList: MutableLiveData<List<CoachInfoUiState>> get() = _coachList

    init {
        setCoachList()
    }

    private fun setCoachList() {
        getCoachesInfo()
    }

    fun saveCoachInfo(coach: Coach) {
        coachRepository.saveCoachInfo(coach)
    }

    private fun getCoachesInfo() {
        viewModelScope.launch {
            coachRepository.readCoachesInfo().collect { it ->
                val result = it.map {
                    CoachInfoUiState(
                        name = it.name,
                        experience = it.experience,
                        email = it.email,
                        phoneNumber = it.phoneNumber,
                        price = it.price
                    )
                }
                Log.e("CoachViewModel", "getCoachesInfo: $result")
                _coachList.value = result
            }
        }
    }
}
