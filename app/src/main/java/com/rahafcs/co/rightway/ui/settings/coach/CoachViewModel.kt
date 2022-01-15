package com.rahafcs.co.rightway.ui.settings.coach

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahafcs.co.rightway.data.Coach
import com.rahafcs.co.rightway.data.CoachRepository
import com.rahafcs.co.rightway.ui.state.CoachInfoUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CoachViewModel(private val coachRepository: CoachRepository) : ViewModel() {
    private var _coachList = MutableLiveData(listOf<CoachInfoUiState>())
    val coachList: MutableLiveData<List<CoachInfoUiState>> get() = _coachList

    private var _coachInfo = MutableLiveData(CoachInfoUiState())
    val coachInfo: MutableLiveData<CoachInfoUiState> get() = _coachInfo

    init {
        setCoachesList()
    }

    fun readCoachInfo(isCoach: Boolean) {
        if (isCoach) {
            getCoachInfo()
        }
    }

    fun setCoachesList() {
        getCoachesInfo()
    }

    fun saveCoachInfo(coach: Coach) {
        coachRepository.saveCoachInfo(coach)
    }

    fun addListOfCoachesEmail(email: String) = coachRepository.addListOfCoachesEmail(email)
    fun removeListOfCoachesEmail(email: String) = coachRepository.removeListOfCoachesEmail(email)
    fun checkIsCoach(email: String) = coachRepository.checkIsCoach(email)
    fun reloadCoachEmailList(): Flow<List<String>> = coachRepository.reloadCoachEmailList()

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

    private fun getCoachInfo() {
        viewModelScope.launch {
            coachRepository.readCoachInfo().collect { it ->
                val result = CoachInfoUiState(
                    name = it.name,
                    experience = it.experience,
                    email = it.email,
                    phoneNumber = it.phoneNumber,
                    price = it.price
                )
                _coachInfo.value = result
            }
        }
    }
}
