package com.comfortablesporttools.yourfitnesstracker.workoutdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.comfortablesporttools.yourfitnesstracker.workoutdb.Workout

@Dao
interface WDAO {

    @Query("SELECT * FROM workouts ORDER BY date")
    suspend fun getAll(): List<Workout>

    @Insert
    suspend fun insertWorkout(workout: Workout)

    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Query("UPDATE workouts SET name=:name, exerciseList=:exerciseList, date=:data, ispreset=:ispreset, isdone=:isdone WHERE id=:id")
    suspend fun updateWorkout(
        id: Int,
        name: String,
        exerciseList: String,
        data: String,
        ispreset: Boolean,
        isdone: Boolean
    )

    @Query("DELETE FROM workouts")
    suspend fun deleteAll()

}