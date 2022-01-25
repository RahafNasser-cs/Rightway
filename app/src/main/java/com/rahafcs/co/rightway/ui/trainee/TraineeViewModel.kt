package com.rahafcs.co.rightway.ui.trainee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahafcs.co.rightway.data.DefaultUserRepository
import com.rahafcs.co.rightway.data.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TraineeViewModel(private val userRepository: DefaultUserRepository) : ViewModel() {
    private var _traineeInfo = MutableStateFlow(TraineeInfoUiState())
    val traineeInfo: MutableStateFlow<TraineeInfoUiState> get() = _traineeInfo

    init {
        readTraineeInfo()
    }

    // Get trainee info.
    private fun readTraineeInfo() {
        viewModelScope.launch {
            userRepository.readUserInfo().collect {
                val traineeInfo = TraineeInfoUiState(
                    firstName = it.firstName,
                    subscriptionStatus = it.subscriptionStatus,
                    weight = it.weight,
                    height = it.height,
                    gender = it.gender,
                    age = it.age,
                    activity = it.activity
                )
                _traineeInfo.update { traineeInfo }
            }
        }
    }

    // Save a new trainee.
    fun userInfo(user: User) {
        userRepository.saveUserInfo(user)
    }

    // Get trainee info.
    suspend fun readUserInfo(): Flow<User> =
        userRepository.readUserInfo()
}
