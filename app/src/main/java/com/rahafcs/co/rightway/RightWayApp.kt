package com.rahafcs.co.rightway

import android.app.Application
import com.rahafcs.co.rightway.data.source.local.WorkoutRoomDatabase

class RightWayApp : Application() {
    val database by lazy {
        WorkoutRoomDatabase.getDatabase(this)
    }
}
