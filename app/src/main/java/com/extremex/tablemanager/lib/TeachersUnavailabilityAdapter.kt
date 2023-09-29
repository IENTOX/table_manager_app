package com.extremex.tablemanager.lib


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.extremex.tablemanager.R


class TeachersUnavailabilityAdapter(private val teacherList: List<UnavailableTeachers>) :
    RecyclerView.Adapter<TeachersUnavailabilityAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_unavailable_teachers_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val teacher = teacherList[position]
        holder.nameTextView.text = teacher.name
        holder.idNumberTextView.text = teacher.idNumber
        holder.AvailableView.text = teacher.availability
    }

    override fun getItemCount(): Int {
        return teacherList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView
        var idNumberTextView: TextView
        var AvailableView: TextView

        init {
            nameTextView = itemView.findViewById(R.id.Name)
            idNumberTextView = itemView.findViewById(R.id.IDNumber)
            AvailableView = itemView.findViewById(R.id.AvailableUntil)
        }
    }
}
