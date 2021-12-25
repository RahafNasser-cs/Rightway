package com.rahafcs.co.rightway.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WorkoutResponse(
    val workoutResponse: List<WorkoutResponseItem?>? = null
) : Parcelable

@Parcelize
data class WorkoutResponseItem(
    val gifUrl: String? = null,
    val name: String? = null,
    val equipment: String? = null,
    val id: String? = null,
    val bodyPart: String? = null,
    val target: String? = null
) : Parcelable
