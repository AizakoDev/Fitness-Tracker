package com.comfortablesporttools.yourfitnesstracker.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.comfortablesporttools.yourfitnesstracker.R
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.comfortablesporttools.yourfitnesstracker.exercise.Exercise
import com.comfortablesporttools.yourfitnesstracker.exercise.ExerciseAdapter
import com.comfortablesporttools.yourfitnesstracker.viewmodel.FitnessSharedViewModel
import com.comfortablesporttools.yourfitnesstracker.workoutdb.Workout
import com.comfortablesporttools.yourfitnesstracker.workoutpresets.WPresetAdapter
import java.lang.reflect.ParameterizedType
import java.time.LocalDateTime

class AddWorkoutFragment : Fragment(), ExerciseAdapter.ExerciseListener,
    WPresetAdapter.PresetListener {

    private lateinit var recValue: EditText
    private lateinit var exName: TextInputEditText
    private lateinit var exRep: TextInputEditText
    private lateinit var exSerie: TextInputEditText
    private lateinit var exKg: TextInputEditText
    private lateinit var exList: ArrayList<Exercise>
    private lateinit var exerciseRecycler: RecyclerView

    private var presetList = ArrayList<Workout>()
    private lateinit var presetDialog: Dialog
    private lateinit var workoutName: EditText
    private lateinit var newWorkout: Workout

    private lateinit var viewModel: FitnessSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_add_workout, container, false)

        workoutName = view.findViewById(R.id.title)

        exList = ArrayList()
        val inputFields = view.findViewById<ConstraintLayout>(R.id.inputLayout)
        exName = inputFields.findViewById(R.id.exerciseNameText)
        recValue = inputFields.findViewById(R.id.recValue)
        exRep = inputFields.findViewById(R.id.repText)
        exSerie = inputFields.findViewById(R.id.serieText)
        exKg = inputFields.findViewById(R.id.kgText)
        val addEx = inputFields.findViewById<TextView>(R.id.addBtn)

        exerciseRecycler = view.findViewById(R.id.exerciseRecycler)

        addEx.setOnClickListener {
            addEx.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alpha))
            if (exName.text.toString() != "") {
                val exNameTmp = exName.text.toString()
                val repTmp =
                    if (exRep.text.toString() != "") exRep.text.toString()
                        .toInt() else 0
                val serieTmp =
                    if (exSerie.text.toString() != "") exSerie.text.toString()
                        .toInt() else 0
                val kgTmp =
                    if (exKg.text.toString() != "") exKg.text.toString()
                        .toInt() else 0
                val recTmp =
                    if (recValue.text.toString() != "") recValue.text.toString()
                        .toInt() else 0
                exList.add(Exercise(exNameTmp, repTmp, serieTmp, kgTmp, recTmp))
                exerciseRecycler.adapter = ExerciseAdapter(exList, this)
                exName.setText("")
                exRep.setText("")
                exSerie.setText("")
                exKg.setText("")
                recValue.setText("")
                exName.requestFocus()
            }
        }

        val proceed = view.findViewById<TextView>(R.id.btnProceed)
        proceed.setOnClickListener {
            proceed.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alpha))
            val workoutNameTmp = workoutName.text.toString()
            if (exList.size > 0 && workoutNameTmp != "") {
                val dialog = Dialog(requireContext())
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(true)
                dialog.setContentView(R.layout.dialog_save)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val title = dialog.findViewById<TextView>(R.id.title)
                title.text = workoutNameTmp
                val exJson = Gson().toJson(exList)
                val newId = viewModel.getLastId() + 1
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    newWorkout = Workout(
                        newId,
                        workoutNameTmp, exJson, LocalDateTime.now().toString(),
                        ispreset = false,
                        isdone = false
                    )
                }
                Log.d("AddWorkoutFragment", "new workout ${newWorkout.name} id ${newWorkout.id}")

                val doneWorkout = dialog.findViewById<TextView>(R.id.doneWorkout)
                doneWorkout.setOnClickListener {
                    doneWorkout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alpha))
                    newWorkout.isdone = true
                    Log.d("AddWorkoutFragment", "asDone ${newWorkout.name} id ${newWorkout.id}")
                    viewModel.insertWorkout(newWorkout)
                    dialog.dismiss()
                    findNavController().navigateUp()
                }

                val aspreset = dialog.findViewById<TextView>(R.id.aspreset)
                aspreset.setOnClickListener {
                    aspreset.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alpha))
                    newWorkout.ispreset = true
                    Log.d("AddWorkoutFragment", "asPreset ${newWorkout.name} id ${newWorkout.id}")
                    viewModel.insertWorkout(newWorkout)
                    dialog.dismiss()
                    findNavController().navigateUp()
                }

                val schedule = dialog.findViewById<TextView>(R.id.schedule)
                schedule.setOnClickListener {
                    schedule.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alpha))
                    Log.d("AddWorkoutFragment", "name ${newWorkout.name} id ${newWorkout.id}")
                    viewModel.insertWorkout(newWorkout)
                    dialog.dismiss()
                    findNavController().navigateUp()
                }
                dialog.show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Введите имя и добавьте хотя бы одно упражнение",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val savedWorkouts = view.findViewById<TextView>(R.id.btnSavedWorkouts)
        savedWorkouts.setOnClickListener {
            savedWorkouts.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alpha))
            presetDialog = Dialog(requireContext())
            presetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            presetDialog.setCancelable(true)
            presetDialog.setContentView(R.layout.dialog_presets)
            val recyclerPresets = presetDialog.findViewById<RecyclerView>(R.id.recyclerWorkouts)
            recyclerPresets.adapter = WPresetAdapter(presetList, this)
            presetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            presetDialog.show()
        }

        val close = view.findViewById<ImageView>(R.id.close)
        close.setOnClickListener {
            findNavController().navigateUp()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(FitnessSharedViewModel::class.java)
        presetList = viewModel.getPresets()
    }


    override fun onExerciseListener(exercise: Exercise, position: Int, longpress: Boolean) {
        exName.setText(exercise.name)
        exRep.setText(exercise.rep.toString())
        exSerie.setText(exercise.set.toString())
        exKg.setText(exercise.kg.toString())
        recValue.setText(exercise.rec.toString())
        exList.remove(exercise)
        exerciseRecycler.adapter = ExerciseAdapter(exList, this)
        exName.requestFocus()
    }

    private val listTypeExercise: ParameterizedType = Types.newParameterizedType(
        List::class.java, Exercise::class.java
    )

    override fun onPresetListener(
        presetName: String,
        presetExercises: String,
        position: Int,
        longpress: Boolean
    ) {
        workoutName.setText(presetName)
        exerciseDeserializer(presetExercises)
        presetDialog.dismiss()
        Log.i("AddWorkoutFragment", "name $presetName : $presetExercises")
    }

    private fun exerciseDeserializer(jsonListOfExercise: String) {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val adapter: JsonAdapter<List<Exercise>> = moshi.adapter(listTypeExercise)
        val exercises: List<Exercise>? = adapter.fromJson(jsonListOfExercise)
        exList = exercises as ArrayList<Exercise>
        exerciseRecycler.adapter = ExerciseAdapter(exercises, this)
    }

}