package com.comfortablesporttools.yourfitnesstracker.workoutdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Workout::class], version = 1, exportSchema = false)
abstract class WDatabase : RoomDatabase() {

    abstract fun workoutDAO(): WDAO

    companion object {
        @Volatile
        private var INSTANCE: WDatabase? = null

        fun getDatabase(context: Context): WDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        WDatabase::class.java,
                        "workouts.db"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}