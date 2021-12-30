package com.rahafcs.co.rightway.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rahafcs.co.rightway.data.UserRepository
import com.rahafcs.co.rightway.data.WorkoutRepository

class ViewModelFactory(
    private val workoutRepository: WorkoutRepository,
    private val userRepository: UserRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WorkoutsViewModel::class.java)) {
            WorkoutsViewModel(workoutRepository, userRepository) as T
        } else if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            SignUpViewModel(userRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

// if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
//    SignUpViewModel(userRepository) as T
// }
