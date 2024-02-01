package com.extremex.tablemanager.lib

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.extremex.tablemanager.databinding.ItemClassroomDataBinding

class ViewClassroomAdapter(private val context: Context, private val classroomData: MutableList<ClassroomModel>) : RecyclerView.Adapter<ViewClassroomAdapter.ViewHolder>() {

    // Initialize the ItemTouchHelper
    private val swipeToDeleteCallback = SwipeToDeleteCallback(this)
    private val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)

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
            }
        }
    }

    fun removeItem( position: Int) {
        val pref = context.getSharedPreferences("ClassRoomData", Context.MODE_PRIVATE)
        val edit = pref.edit()
        val key = classroomData[position]
        if (position != 0 && position != null) {
            classroomData.removeAt(position)
            Log.w("remove key:", "selected key is $key")
            edit.remove(key.toString())
            edit.commit()
            notifyItemRemoved(position)
            notifyDataSetChanged()
        } else {
            classroomData.removeAt(0)
            Log.w("remove key:", "selected key is $key")
            edit.remove(key.toString())
            edit.commit()
            notifyItemRemoved(0)
            notifyDataSetChanged()
        }
    }

    // Attach the ItemTouchHelper to the RecyclerView in your Fragment or Activity
    fun attachItemTouchHelper(recyclerView: RecyclerView) {
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}
