package com.comfortablesporttools.yourfitnesstracker.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.comfortablesporttools.yourfitnesstracker.workoutdb.WRepository
import com.comfortablesporttools.yourfitnesstracker.workoutdb.Workout

class FitnessSharedViewModel(val app: Application) : AndroidViewModel(app) {
    private val workoutsdb = WRepository(app)
    var workouts = MutableLiveData<List<Workout>>()
    private var workoutToEdit: Workout? = null

    init {
        workoutsdb.getAll()
        workouts = workoutsdb.workoutData
        Log.d("FitnessSharedViewModel", "init: $workouts")
    }

    fun setWorkoutToEdit(workout: Workout) {
        workoutToEdit = workout
    }

    fun getWorkoutToEdit(): Workout {
        return workoutToEdit!!
    }

    fun insertWorkout(workout: Workout) {
        workoutsdb.insertWorkout(workout)
    }

    fun deleteWorkout(workout: Workout) {
        workoutsdb.deleteWorkout(workout)
    }

    fun getLastId(): Int {
        var maxId = 0
        workouts.value!!.forEach {
            maxId = if (it.id > maxId) it.id else maxId
        }
        return maxId
    }

    fun getPresets(): ArrayList<Workout> {
        val presets = ArrayList<Workout>()
        workouts.value!!.forEach {
            if (it.ispreset)
                presets.add(it)
        }
        return presets
    }
}