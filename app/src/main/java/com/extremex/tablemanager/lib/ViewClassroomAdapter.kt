package com.extremex.tablemanager.lib

import android.content.Context
import android.content.SharedPreferences
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
    private val pref: SharedPreferences = context.getSharedPreferences(StandardCompanion.CLASSROOM_DATA_FILE_MANE, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()
    private var keys_d = pref.all.keys.toMutableList()
    private var value_d = pref.all.values.toMutableList()

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



    fun removeItem(position: Int, name: String="Item") {
        if (keys_d.size == value_d.size && keys_d.isNotEmpty()){
            editor.remove(keys_d[position])
            editor.commit()
            keys_d = pref.all.keys.toMutableList()
            value_d = pref.all.values.toMutableList()
            notifyItemRemoved(position)
            notifyItemRangeChanged(0,keys_d.size-1)

        } else {
            val dialog = CustomDialog(context, null, null)
            dialog.createBasicCustomDialog(
                "Dismiss", "An error was caused while deleting ${name}, try again later.",
                true,
                true,
                "failed To Delete"
            )
        }
    }

    // Attach the ItemTouchHelper to the RecyclerView in your Fragment or Activity
    fun attachItemTouchHelper(recyclerView: RecyclerView) {
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}
