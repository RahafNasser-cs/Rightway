package com.rahafcs.co.rightway.ui.settings.coach

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahafcs.co.rightway.data.CoachRepository
import com.rahafcs.co.rightway.data.User
import com.rahafcs.co.rightway.ui.state.CoachInfoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CoachViewModel(private val coachRepository: CoachRepository) : ViewModel() {
    private var _coachList = MutableLiveData(listOf<CoachInfoUiState>())
    val coachList: MutableLiveData<List<CoachInfoUiState>> get() = _coachList

    private var _coachInfo = MutableStateFlow(User())
    val coachInfo: MutableStateFlow<User> get() = _coachInfo

    private var _coachInfoUiState = MutableLiveData(CoachInfoUiState())
    val coachInfoUiState: MutableLiveData<CoachInfoUiState> get() = _coachInfoUiState

    init {
        setCoachesList()
        getCoachInfo()
    }

    fun setCoachesList() {
        getCoachList()
    }

    fun saveCoachInfo(coach: User) {
        coachRepository.saveCoachInfo(coach)
    }

    private fun getCoachList() {
        viewModelScope.launch {
            coachRepository.getCoachList().collect {
                val result = it.map {
                    CoachInfoUiState(
                        name = it.firstName,
                        experience = it.experience,
                        email = it.email,
                        phoneNumber = it.phoneNumber,
                        price = it.price
                    )
                }
                Log.e("CoachViewModel", "getTrainer: $result")
                _coachList.value = result
            }
        }
    }

    fun saveCoachInfo(coachInfoUiState: CoachInfoUiState) {
        _coachInfo.update {
            it.copy(
                experience = coachInfoUiState.experience,
                email = coachInfoUiState.email,
                phoneNumber = coachInfoUiState.phoneNumber,
                price = coachInfoUiState.price,
                firstName = coachInfoUiState.name
            )
        }
        saveCoachInfo(_coachInfo.value)
    }

    private fun getCoachInfo() {
        viewModelScope.launch {
            coachRepository.readCoachInfo().collect {
                _coachInfo.value = it
                val result = CoachInfoUiState(
                    name = it.firstName,
                    experience = it.experience,
                    email = it.email,
                    phoneNumber = it.phoneNumber,
                    price = it.price
                )
                Log.e("CoachViewModel", "getCoachInfo: $result")
                _coachInfoUiState.value = result
            }
        }
    }
}
