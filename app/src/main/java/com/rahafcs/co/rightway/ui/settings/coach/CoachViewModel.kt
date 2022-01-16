package com.rahafcs.co.rightway.ui.settings.coach

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahafcs.co.rightway.data.CoachRepository
import com.rahafcs.co.rightway.data.User
import com.rahafcs.co.rightway.ui.state.CoachInfoUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CoachViewModel(private val coachRepository: CoachRepository) : ViewModel() {
    private var _coachList = MutableLiveData(listOf<CoachInfoUiState>())
    val coachList: MutableLiveData<List<CoachInfoUiState>> get() = _coachList

    private var _coachInfo = MutableStateFlow(User())
    val coachInfo: MutableStateFlow<User> get() = _coachInfo

    private var _coachInfoUiState = MutableLiveData(CoachInfoUiState())
    val coachInfoUiState: MutableLiveData<CoachInfoUiState> get() = _coachInfoUiState

    private var _coachesEmail = MutableStateFlow(listOf<String>())
    // val coachesEmail: MutableStateFlow<List<String>> get() = _coachesEmail

    init {
        setCoachesList()
        readCoachInfo(true)
        // Log.e("CoachViewModel", "init: ${_coachesEmail.value}")
    }

    fun readCoachInfo(isCoach: Boolean) {
        if (isCoach) {
            getCoachInfo()
        }
    }

    fun setCoachesList() {
        // getCoachesInfo()
        getTrainer()
    }

    fun saveCoachInfo(coach: User) {
        coachRepository.saveCoachInfo(coach)
    }

    fun addListOfCoachesEmail(email: String) = coachRepository.addListOfCoachesEmail(email)
    fun removeListOfCoachesEmail(email: String) = coachRepository.removeListOfCoachesEmail(email)
    fun checkIsCoach(email: String) = coachRepository.checkIsCoach(email)

    //    fun reloadCoachEmailList(): Flow<List<String>> = coachRepository.reloadCoachEmailList()
    suspend fun reloadCoachEmailList(): Flow<List<String>> = coachRepository.reloadCoachEmailList()

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

    private fun getTrainer() {
        viewModelScope.launch {
            coachRepository.getTrainer().collect {
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
                _coachInfoUiState.value = result
            }
        }
    }
}
