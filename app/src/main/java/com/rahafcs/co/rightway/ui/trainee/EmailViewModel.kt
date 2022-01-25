package com.rahafcs.co.rightway.ui.trainee

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahafcs.co.rightway.data.DefaultUserRepository
import com.rahafcs.co.rightway.data.User
import com.rahafcs.co.rightway.utility.Constant.PRE_MESSAGE
import com.rahafcs.co.rightway.utility.Constant.PRE_SUBJECT
import kotlinx.coroutines.launch

class EmailViewModel(private val userRepository: DefaultUserRepository) : ViewModel() {
    private var _user = MutableLiveData(arrayListOf(""))
    val user: MutableLiveData<ArrayList<String>> get() = _user

    private var _preMessage = MutableLiveData("")
    val preMessage: MutableLiveData<String> get() = _preMessage

    private var _preSubject = MutableLiveData("")
    val preSubject: MutableLiveData<String> get() = _preSubject

    init {
        readUserInfo()
        setPreSubject()
    }

    // To set pre subject.
    private fun setPreSubject() {
        _preSubject.value = PRE_SUBJECT
    }

    // To get user info.
    fun readUserInfo() {
        viewModelScope.launch {
            try {
                userRepository.readUserInfo().collect {
                    showUserMessage(it)
                }
            } catch (e: Exception) {
            }
        }
    }

    // To show pre message.
    private fun showUserMessage(userInfo: User) {
        user.value =
            arrayListOf(
                userInfo.firstName,
                userInfo.gender,
                userInfo.height,
                userInfo.weight,
                userInfo.age,
                userInfo.activity
            )
        _preMessage.value = PRE_MESSAGE
    }
}
