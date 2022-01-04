package com.rahafcs.co.rightway.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Workout(
    val gifUrl: String = "",
    val name: String = "",
    val equipment: String = "",
    val bodyPart: String = "",
    val target: String = ""
)
@Entity
data class WorkoutListForDatabase(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val name: String = "",
    @ColumnInfo val equipment: String = "",
    @ColumnInfo val target: String = "",
    @ColumnInfo val bodyPart: String = "",
    @ColumnInfo val gifUrl: String = "",
    @ColumnInfo val saved: Boolean = false
)

@Entity
data class SavedWorkoutFoeDatabase(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val name: String = "",
    @ColumnInfo val equipment: String = "",
    @ColumnInfo val target: String = "",
    @ColumnInfo val bodyPart: String = "",
    @ColumnInfo val gifUrl: String = ""
)
