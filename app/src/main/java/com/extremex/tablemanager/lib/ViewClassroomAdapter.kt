package com.extremex.tablemanager.lib

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.extremex.tablemanager.databinding.ItemClassroomDataBinding

class ViewClassroomAdapter(private val context: Context, private val classroomData: MutableList<ClassroomModel>) : RecyclerView.Adapter<ViewClassroomAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemClassroomDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(context, binding)
    }

    override fun getItemCount() = classroomData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(classroomData[position])
    }

    inner class ViewHolder(
        private val context: Context,
        private val binding: ItemClassroomDataBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(classroomModel: ClassroomModel) {
            binding.apply {
                ClassroomCode.text = classroomModel.classroomCode
                ClassroomName.text = classroomModel.classroomName
                RemoveItemClassroomButton.visibility = View.GONE
                if (ClassroomName.text.toString().isNotBlank()) {

                    RemoveItemClassroomButton.visibility = View.VISIBLE
                    RemoveItemClassroomButton.setOnClickListener {
                        val position = adapterPosition
                        if (position != RecyclerView.NO_POSITION) {
                            classroomData[position].classroomName?.let { it1 -> removeItem(it1, position) }
                        }
                    }
                } else {
                    RemoveItemClassroomButton.visibility = View.GONE
                }
            }
        }
    }

    private fun removeItem(key: String, position: Int) {
        val pref = context.getSharedPreferences("ClassRoomData", Context.MODE_PRIVATE)
        val edit = pref.edit()
        classroomData.removeAt(position)
        Log.w("remove key:","selected key is $key")
        edit.remove(key)
        edit.commit()
        notifyItemRemoved(position)
    }
}
