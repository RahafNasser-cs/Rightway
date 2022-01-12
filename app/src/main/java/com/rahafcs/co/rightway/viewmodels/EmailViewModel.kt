package com.rahafcs.co.rightway.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahafcs.co.rightway.data.User
import com.rahafcs.co.rightway.data.UserRepository
import kotlinx.coroutines.launch

class EmailViewModel(private val userRepository: UserRepository) : ViewModel() {
    private var _preMessage = MutableLiveData<String>()
    val preMessage: MutableLiveData<String> get() = _preMessage

    init {
        readUserInfo()
    }

    fun setPreMessage(message: String) {
        _preMessage.value = message
    }

    private fun readUserInfo() {
        viewModelScope.launch {
            try {
                userRepository.readUserInfo().collect {
                    showUserMessage(it)
                }
            } catch (e: Exception) {
                Log.e("EmailViewModel", "readUserInfo: a error $e")
            }
        }
    }

    private fun showUserMessage(userInfo: User) {
        val preMessage =
            "Hi I'm ${userInfo.firstName}\nI would like to subscribe with you!" +
                "\nSome info about me:\nGender: ${userInfo.gender}" +
                "\nHeight: ${userInfo.height}\nWeight: ${userInfo.weight}" +
                "\nAge: ${userInfo.age}\nActivity level: ${userInfo.activity}\n"
        setPreMessage(preMessage)
    }
}
