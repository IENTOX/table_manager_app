package com.extremex.tablemanager.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.extremex.tablemanager.R
import com.extremex.tablemanager.models.PendingTeacherObject


class PendingTeachersAdapter(private val teacherList: List<PendingTeacherObject>) :
    RecyclerView.Adapter<PendingTeachersAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pending_teachers_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val teacher = teacherList[position]
        if (teacher.name.isNotEmpty()){
            holder.notNullUI.visibility = View.VISIBLE
            holder.nullUI.visibility = View.GONE
            holder.nameTextView.text = teacher.name
        } else {
            holder.notNullUI.visibility = View.GONE
            holder.nullUI.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return teacherList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView
        var notNullUI: RelativeLayout
        var nullUI: LinearLayout
        init {
            nameTextView = itemView.findViewById(R.id.Name)
            nullUI = itemView.findViewById(R.id.NoPendingRequestsUI)
            notNullUI = itemView.findViewById(R.id.PendingRequestUI)
        }
    }
}
