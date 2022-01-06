package com.rahafcs.co.rightway.viewmodels

import android.util.Log
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

//    private var _userInfo = MutableStateFlow(User())
//    val userInfo: MutableStateFlow<User> = _userInfo

    private var _userInfo = MutableLiveData<User>()
    val userInfo: MutableLiveData<User> = _userInfo

    fun setRegistrationStatus(status: RegistrationStatus) {
        _status.value = status
    }

    init {
        setUserInfo(getUserInfo())
    }

    // signupFragment
    fun userInfo(user: User) {
        userRepository.addUserInfo(user)
    }

    // userInfoSettingsFragment
    suspend fun readUserInfo(): Flow<User> =
        userRepository.readUserInfo()

    fun setUserInfo(userInfo: User) {
        Log.d("SiginUpViewModel", "setUserInfo: $userInfo")
        _userInfo.value = userInfo
//        _userInfo.update {
//            it.copy(
//                firstName = userInfo.firstName,
//                lastName = userInfo.lastName,
//                subscriptionStatus = userInfo.activity,
//                weight = userInfo.weight,
//                height = userInfo.height,
//                gender = userInfo.age,
//                age = userInfo.gender,
//                activity = userInfo.subscriptionStatus
//            )
//        }
//        viewModelScope.launch {
//            readUserInfo("").collect {
//                _userInfo.value = it
//                Log.d("SignUpViewModel", "setUserInfo: $it")
//            }
//        }
    }

    private fun getUserInfo(): User = User(
        firstName = "Rahaf",
        lastName = "Nasser",
        subscriptionStatus = "NONE",
        weight = "8Pound",
        height = "8feet",
        gender = "Male",
        age = "24",
        activity = "2"
    )
}
