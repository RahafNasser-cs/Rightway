// package com.rahafcs.co.rightway.data.source.local
//
// import androidx.room.Database
// import androidx.room.RoomDatabase
// import com.rahafcs.co.rightway.data.SavedWorkoutForDatabase
// import com.rahafcs.co.rightway.data.WorkoutListForDatabase
//
// @Database(
//    entities = [WorkoutListForDatabase::class, SavedWorkoutForDatabase::class],
//    version = 2
// )
// abstract class WorkoutRoomDatabase : RoomDatabase() {
// //    abstract fun workoutsDao(): WorkoutsDao
// //
// //    companion object {
// //        @Volatile
// //        private var INSTANCE: WorkoutRoomDatabase? = null
// //        fun getDatabase(context: Context): WorkoutRoomDatabase {
// //            return INSTANCE ?: synchronized(this) {
// //                val instance = Room.databaseBuilder(
// //                    context.applicationContext,
// //                    WorkoutRoomDatabase::class.java,
// //                    "work_database"
// //                ).fallbackToDestructiveMigration().build()
// //                INSTANCE = instance
// //                return instance
// //            }
// //        }
// //    }
// }
