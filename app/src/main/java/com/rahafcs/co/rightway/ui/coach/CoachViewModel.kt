package com.rahafcs.co.rightway.ui.coach

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahafcs.co.rightway.data.DefaultUserRepository
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

class CoachViewModel(
    private val userRepository: DefaultUserRepository,
) : ViewModel() {
    // To show it in coaches list --> fragment_coaches.xml.
    private var _coachList = MutableStateFlow(ListCoachInfoUiState())
    val coachList: MutableStateFlow<ListCoachInfoUiState> get() = _coachList

    // A copy of the all original user info.
    private var _coachInfo = MutableStateFlow(User())

    // To show it in coach info settings --> fragment_coach_info_settings.xml.
    private var _coachInfoUiState = MutableStateFlow(CoachInfoUiState())
    val coachInfoUiState: MutableStateFlow<CoachInfoUiState> get() = _coachInfoUiState

    init {
        setCoachesList()
        getCoachInfo()
    }

    fun setCoachesList() =
        getCoachList()

    fun saveCoachInfo(coach: User) =
        userRepository.saveUserInfo(coach)

    // To get list of coaches to show on fragment_coaches.xml.
    private fun getCoachList() =
        viewModelScope.launch {
            try {
                _coachList.update { it.copy(loadingState = LoadingStatus.LOADING) }
                userRepository.getCoachList().collect { coachesList ->
                    val result = coachesList.map {
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
                        loadingState = LoadingStatus.ERROR,
                        userMsg = ERROR_MESSAGE
                    )
                }
            }
        }

    // Save coach info.
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

    // Get coach info to show on fragment_coach_info_settings.xml.
    fun getCoachInfo() {
        viewModelScope.launch {
            userRepository.readUserInfo().collect {
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

    // To get user type --> trainee or trainer "coach".
    fun getUserType(): Flow<String> = userRepository.getUserType()
}
