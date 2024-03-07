package com.extremex.tablemanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.extremex.tablemanager.R
import com.extremex.tablemanager.lib.TimetableEntry

class TimetableAdapter(private val timetableEntries: List<TimetableEntry>) : RecyclerView.Adapter<TimetableAdapter.TimetableViewHolder>() {

    class TimetableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTextView: TextView = itemView.findViewById(R.id.dayTextView)
        val timeSlotTextView: TextView = itemView.findViewById(R.id.timeSlotTextView)
        val teacherTextView: TextView = itemView.findViewById(R.id.teacherTextView)
        val subjectTextView: TextView = itemView.findViewById(R.id.subjectTextView)
        val classroomTextView: TextView = itemView.findViewById(R.id.classroomTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimetableViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_timetable, parent, false)
        return TimetableViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TimetableViewHolder, position: Int) {
        val timetableEntry = timetableEntries[position]

        holder.dayTextView.text = timetableEntry.day
        holder.timeSlotTextView.text = timetableEntry.timeSlot
        holder.teacherTextView.text = timetableEntry.teacher?.name
        holder.subjectTextView.text = timetableEntry.subject
        holder.classroomTextView.text = timetableEntry.classroom
    }

    override fun getItemCount(): Int {
        return timetableEntries.size
    }
}
