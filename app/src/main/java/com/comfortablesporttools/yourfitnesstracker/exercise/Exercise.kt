package com.comfortablesporttools.yourfitnesstracker.exercise

import java.io.Serializable

data class Exercise(
    var name: String,
    var rep: Int,
    var set: Int,
    var kg: Int,
    var rec: Int) : Serializable