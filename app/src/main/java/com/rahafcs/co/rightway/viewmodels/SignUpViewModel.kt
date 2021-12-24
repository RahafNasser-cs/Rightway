package com.rahafcs.co.rightway.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahafcs.co.rightway.data.RegistrationStatus

class SignUpViewModel : ViewModel() {
    private var _status = MutableLiveData<RegistrationStatus>()
    val status: LiveData<RegistrationStatus> get() = _status

    fun setRegistrationStatus(status: RegistrationStatus) {
        _status.value = status
    }
}
