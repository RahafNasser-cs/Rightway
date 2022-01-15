package com.rahafcs.co.rightway.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rahafcs.co.rightway.data.CoachRepository
import com.rahafcs.co.rightway.data.DefaultWorkoutsRepository
import com.rahafcs.co.rightway.data.UserRepository
import com.rahafcs.co.rightway.ui.settings.coach.CoachViewModel

class ViewModelFactory(
    private val workoutRepository: DefaultWorkoutsRepository,
    private val userRepository: UserRepository,
    private val coachRepository: CoachRepository,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WorkoutsViewModel::class.java)) {
            WorkoutsViewModel(workoutRepository, userRepository) as T
        } else if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            SignUpViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(EmailViewModel::class.java)) {
            EmailViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(CoachViewModel::class.java)) {
            CoachViewModel(coachRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

// if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
//    SignUpViewModel(userRepository) as T
// }
