package com.comfortablesporttools.yourfitnesstracker.workout

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.comfortablesporttools.yourfitnesstracker.R
import com.comfortablesporttools.yourfitnesstracker.workoutdb.Workout
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WorkoutHistoryAdapter(
    private val workouts: ArrayList<Workout>,
    private val workoutListener: WorkoutHistoryListener,
    private val context: Context
) : RecyclerView.Adapter<WorkoutHistoryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val workoutName: TextView = itemView.findViewById(R.id.workoutName)
        val workoutDate: TextView = itemView.findViewById(R.id.workoutDate)
        val workoutDateAgo: TextView = itemView.findViewById(R.id.workoutDateAgo)
        val workoutEdit: ImageView = itemView.findViewById(R.id.workoutEdit)
    }

    override fun getItemCount(): Int = workouts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_workout_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workout = workouts[position]
        with(holder) {
            workoutName.text = workout.name
            val aLDT = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDateTime.parse(workout.date)
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm")
            workoutDate.text = aLDT.format(formatter)
            val daysBetween = Duration.between(aLDT, LocalDateTime.now()).toDays()
            val daysBetweenCnt = daysBetween.toInt()
            val daysAgo =
                if (daysBetweenCnt == 0) "сегодня"
                else if (daysBetweenCnt in 5..20 || daysBetweenCnt % 10 in 5..9 || daysBetweenCnt % 10 == 0) "$daysBetween дней назад"
                else if (daysBetweenCnt % 10 == 1) "$daysBetween день назад"
                else "$daysBetween дня назад"
            workoutDateAgo.text = daysAgo
            workoutEdit.setOnClickListener {
                workoutEdit.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alpha))
                workoutListener.onWorkoutHistoryListener(workout, holder.layoutPosition, true)
            }
            holder.itemView.setOnClickListener {
                workoutListener.onWorkoutHistoryListener(workout, holder.layoutPosition, false)
            }
        }
    }

    interface WorkoutHistoryListener {
        fun onWorkoutHistoryListener(workout: Workout, position: Int, editMeal: Boolean)
    }
}