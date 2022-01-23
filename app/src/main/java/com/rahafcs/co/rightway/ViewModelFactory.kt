package com.rahafcs.co.rightway

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rahafcs.co.rightway.data.DefaultUserRepository
import com.rahafcs.co.rightway.data.DefaultWorkoutsRepository
import com.rahafcs.co.rightway.ui.auth.SignUpViewModel
import com.rahafcs.co.rightway.ui.coach.CoachViewModel
import com.rahafcs.co.rightway.ui.trainee.EmailViewModel
import com.rahafcs.co.rightway.ui.workout.WorkoutsViewModel

class ViewModelFactory(
    private val workoutRepository: DefaultWorkoutsRepository,
    private val userRepository: DefaultUserRepository,
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
            CoachViewModel(userRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
