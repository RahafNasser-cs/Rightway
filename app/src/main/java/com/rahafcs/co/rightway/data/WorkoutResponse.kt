package com.rahafcs.co.rightway.data

data class WorkoutResponse(
    val workoutResponse: List<Workout> = listOf()
)

data class Workout(
    val gifUrl: String = "",
    val name: String = "",
    val equipment: String = "",
    val id: String = "",
    val bodyPart: String = "",
    val target: String = ""
)
