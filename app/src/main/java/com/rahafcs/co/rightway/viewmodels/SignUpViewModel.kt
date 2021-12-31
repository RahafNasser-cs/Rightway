package com.rahafcs.co.rightway.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahafcs.co.rightway.data.RegistrationStatus
import com.rahafcs.co.rightway.data.User
import com.rahafcs.co.rightway.data.UserRepository
import kotlinx.coroutines.flow.Flow

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {
    private var _status = MutableLiveData<RegistrationStatus>()
    val status: LiveData<RegistrationStatus> get() = _status

    fun setRegistrationStatus(status: RegistrationStatus) {
        _status.value = status
    }

    fun userInfo(user: User) {
        userRepository.addUserInfo(user)
    }

    suspend fun readUserInfo(userName: String): Flow<User> =
        userRepository.readUserInfo(userName)
}
