// package com.rahafcs.co.rightway
//
// import androidx.lifecycle.ViewModel
// import androidx.lifecycle.ViewModelProvider
// import com.rahafcs.co.rightway.data.DefaultUserRepository
// import com.rahafcs.co.rightway.data.DefaultWorkoutsRepository
//
// class ViewModelFactory(
//    private val workoutRepository: DefaultWorkoutsRepository,
//    private val userRepository: DefaultUserRepository,
// //    private val authRepository: AuthRepository,
// ) :
//    ViewModelProvider.Factory {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return when {
// //            modelClass.isAssignableFrom(WorkoutsViewModel::class.java) -> {
// //                WorkoutsViewModel(workoutRepository, userRepository) as T
// //            }
// //            modelClass.isAssignableFrom(EmailViewModel::class.java) -> {
// //                EmailViewModel(userRepository) as T
// //            }
// //            modelClass.isAssignableFrom(CoachViewModel::class.java) -> {
// //                CoachViewModel(userRepository) as T
// //            }
// //            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
// //                AuthViewModel(authRepository) as T
// //            }
// //            modelClass.isAssignableFrom(TraineeViewModel::class.java) -> {
// //                TraineeViewModel(userRepository) as T
// //            }
//            else -> {
//                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
//            }
//        }
//    }
// }
