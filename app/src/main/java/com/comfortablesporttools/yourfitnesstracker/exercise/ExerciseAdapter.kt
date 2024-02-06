package com.comfortablesporttools.yourfitnesstracker.exercise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.comfortablesporttools.yourfitnesstracker.R

class ExerciseAdapter(
    private val exerciseList: List<Exercise>,
    private val exerciseListener: ExerciseListener
) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseName: TextView = itemView.findViewById(R.id.name)
        private val infolayout: LinearLayout = itemView.findViewById(R.id.linearExercise)
        val exerciseRep: TextView = infolayout.findViewById(R.id.rep)
        val exerciseSerie: TextView = infolayout.findViewById(R.id.serie)
        val exerciseKg: TextView = infolayout.findViewById(R.id.kg)
        val exerciseRec: TextView = infolayout.findViewById(R.id.rec)

    }

    override fun getItemCount(): Int = exerciseList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_exercise, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercise = exerciseList[position]
        with(holder) {
            exerciseName.text = exercise.name
            var textTmp = "повторений:\n" + exercise.rep.toString()
            exerciseRep.text = textTmp
            textTmp = "серий:\n" + exercise.set.toString()
            exerciseSerie.text = textTmp
            textTmp = "кг:\n" + exercise.kg.toString()
            exerciseKg.text = textTmp
            textTmp = "восст:\n" + exercise.rec.toString()
            exerciseRec.text = textTmp
            holder.itemView.setOnClickListener {
                exerciseListener.onExerciseListener(exercise, holder.layoutPosition, false)
            }
            holder.itemView.setOnLongClickListener {
                exerciseListener.onExerciseListener(exercise, holder.layoutPosition, true)
                true
            }
        }
    }


    interface ExerciseListener {
        fun onExerciseListener(exercise: Exercise, position: Int, longpress: Boolean)
    }
}