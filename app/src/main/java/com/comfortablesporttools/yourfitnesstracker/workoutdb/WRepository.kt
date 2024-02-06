package com.comfortablesporttools.yourfitnesstracker.workoutdb

import android.app.Application
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WRepository(app: Application) {
    var workoutData = MutableLiveData<List<Workout>>()
    private val workoutDAO = WDatabase.getDatabase(app).workoutDAO()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            workoutData.postValue(workoutDAO.getAll())
        }
    }

    fun getAll() {
        CoroutineScope(Dispatchers.IO).launch {
            workoutData.postValue(workoutDAO.getAll())
        }
    }

    fun insertWorkout(workout: Workout) {
        CoroutineScope(Dispatchers.IO).launch {
            workoutDAO.insertWorkout(workout)
            workoutData.postValue(workoutDAO.getAll())
        }
    }

    fun deleteWorkout(workout: Workout) {
        CoroutineScope(Dispatchers.IO).launch {
            workoutDAO.deleteWorkout(workout)
            workoutData.postValue(workoutDAO.getAll())
        }
    }

}