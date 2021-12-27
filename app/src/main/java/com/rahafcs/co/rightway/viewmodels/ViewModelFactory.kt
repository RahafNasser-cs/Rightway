package com.rahafcs.co.rightway.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rahafcs.co.rightway.data.WorkoutRepository

class ViewModelFactory(private val workoutRepository: WorkoutRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WorkoutsViewModel::class.java)) {
            WorkoutsViewModel(workoutRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

// if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
//    SignUpViewModel(userRepository) as T
// } 
