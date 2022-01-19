package com.rahafcs.co.rightway.ui.settings.coach

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahafcs.co.rightway.data.CoachRepository
import com.rahafcs.co.rightway.data.LoadingStatus
import com.rahafcs.co.rightway.data.User
import com.rahafcs.co.rightway.ui.state.CoachInfoUiState
import com.rahafcs.co.rightway.ui.state.ListCoachInfoUiState
import com.rahafcs.co.rightway.utility.Constant.ERROR_MESSAGE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception

class CoachViewModel(private val coachRepository: CoachRepository) : ViewModel() {

    private var _coachList = MutableStateFlow(ListCoachInfoUiState())
    val coachList: MutableStateFlow<ListCoachInfoUiState> get() = _coachList

    private var _coachInfo = MutableStateFlow(User())

    private var _coachInfoUiState = MutableStateFlow(CoachInfoUiState())
    val coachInfoUiState: MutableStateFlow<CoachInfoUiState> get() = _coachInfoUiState

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
            try {
                _coachList.update { it.copy(loadingState = LoadingStatus.LOADING) }
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
                    _coachList.update {
                        it.copy(
                            coachInfoUiState = result,
                            loadingState = LoadingStatus.SUCCESS
                        )
                    }
                }
            } catch (e: Exception) {
                _coachList.update {
                    it.copy(
                        loadingState = LoadingStatus.FAILURE,
                        userMsg = ERROR_MESSAGE
                    )
                }
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

    fun getCoachInfo() {
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
                _coachInfoUiState.update { result }
            }
        }
    }

    fun getUserType(): Flow<String> = coachRepository.getUserType()
}
