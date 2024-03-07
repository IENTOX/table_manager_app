package com.extremex.tablemanager.adapter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.extremex.tablemanager.databinding.ItemClassroomDataBinding
import com.extremex.tablemanager.lib.ClassroomModel
import com.extremex.tablemanager.lib.CustomDialog
import com.extremex.tablemanager.lib.StandardCompanion
import com.extremex.tablemanager.lib.SwipeToDeleteCallback
import java.lang.IndexOutOfBoundsException

class ViewClassroomAdapter(private val context: Context, private val classroomData: MutableList<ClassroomModel>) : RecyclerView.Adapter<ViewClassroomAdapter.ViewHolder>() {

    // Define a listener interface
    interface ViewClassroomAdapterListener {
        fun onClassroomCleared()
    }

    // Listener instance
    private var listener: ViewClassroomAdapterListener? = null
    private val swipeToDeleteCallback = SwipeToDeleteCallback(this)
    private val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
    private val pref: SharedPreferences = context.getSharedPreferences(StandardCompanion.CLASSROOM_DATA_FILE_MANE, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

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



    fun removeItem(position: Int) {
        try {
            val fromPrefKeys = pref.all.keys.toMutableList()
            editor.remove(fromPrefKeys[position])
            editor.commit()
            listener?.onClassroomCleared()
            notifyItemRemoved(position)
        } catch (e: IndexOutOfBoundsException) {
            Log.v("deletion", "current position: $position")
            for (i in pref.all.keys) {
                Log.v("deletion", "OutOfBoundsFile -> ${pref.getString(i, "")}")
            }
        }
    }

    // Setter method for the listener
    fun setListener(listener: ViewClassroomAdapterListener) {
        this.listener = listener
    }

    // Attach the ItemTouchHelper to the RecyclerView in your Fragment or Activity
    fun attachItemTouchHelper(recyclerView: RecyclerView) {
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}
