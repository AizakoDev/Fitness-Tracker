package com.comfortablesporttools.yourfitnesstracker.ui

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.comfortablesporttools.yourfitnesstracker.R
import com.comfortablesporttools.yourfitnesstracker.viewmodel.FitnessSharedViewModel
import com.comfortablesporttools.yourfitnesstracker.workout.WorkoutAdapter
import com.comfortablesporttools.yourfitnesstracker.workout.WorkoutHistoryAdapter
import com.comfortablesporttools.yourfitnesstracker.workoutdb.Workout
import java.time.LocalDateTime

class FitnessTrackerFragment : Fragment(), WorkoutAdapter.WorkoutListener, WorkoutHistoryAdapter.WorkoutHistoryListener{

    private val presets = ArrayList<Workout>()
    private val incoming = ArrayList<Workout>()
    private val history = ArrayList<Workout>()

    private lateinit var viewModel: FitnessSharedViewModel
    private var workouts = ArrayList<Workout>()

    private lateinit var presetsView: RecyclerView
    private lateinit var incomingView: RecyclerView
    private lateinit var historyView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_fitness_tracker, container, false)

        presetsView = view.findViewById(R.id.presetsWorkout)
        incomingView = view.findViewById(R.id.incomingWorkout)
        historyView = view.findViewById(R.id.historyWorkout)

        val btnWorkout = view.findViewById<TextView>(R.id.btnWorkout)
        btnWorkout.setOnClickListener {
            btnWorkout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alpha))
            findNavController().navigate(R.id.addWorkoutFragment)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(FitnessSharedViewModel::class.java)
        viewModel.workouts.observe(viewLifecycleOwner) { mutableList ->
            workouts = mutableList as ArrayList<Workout>
            presets.clear()
            history.clear()
            incoming.clear()
            for (workout in workouts) {
                when {
                    workout.ispreset -> presets.add(workout)
                    workout.isdone -> history.add(workout)
                    else -> incoming.add(workout)
                }
            }

            val sortedHistory =
                history.sortedByDescending { it.date }
            presetsView.adapter = WorkoutAdapter(presets, this, requireContext())
            incomingView.adapter = WorkoutAdapter(incoming, this, requireContext())
            historyView.adapter =
                WorkoutHistoryAdapter(ArrayList(sortedHistory), this, requireContext())
        }

    }

    override fun onWorkoutListener(workout: Workout, position: Int, editWorkout: Boolean, done: Boolean) {
        if (editWorkout) {
            viewModel.setWorkoutToEdit(workout)
            findNavController().navigate(R.id.editWorkoutFragment)

        } else if (done) {
            viewModel.deleteWorkout(workout)
            workout.isdone = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                workout.date = LocalDateTime.now().toString()
            }
            viewModel.insertWorkout(workout)

            incoming.remove(workout)
            history.add(workout)
            val sortedHistory =
                history.sortedByDescending { it.date }
            incomingView.adapter = WorkoutAdapter(incoming, this, requireContext())
            historyView.adapter =
                WorkoutHistoryAdapter(ArrayList(sortedHistory), this, requireContext())
        }
    }

    override fun onWorkoutHistoryListener(workout: Workout, position: Int, editMeal: Boolean) {
        viewModel.setWorkoutToEdit(workout)
        findNavController().navigate(R.id.editWorkoutFragment)
    }

}