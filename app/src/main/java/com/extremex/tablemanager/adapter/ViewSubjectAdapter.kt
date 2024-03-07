package com.extremex.tablemanager.adapter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.extremex.tablemanager.databinding.ItemSubjectViewBinding
import com.extremex.tablemanager.lib.CustomDialog
import com.extremex.tablemanager.lib.StandardCompanion
import com.extremex.tablemanager.lib.SubjectListModel
import com.extremex.tablemanager.lib.SwipeToDeleteCallbackS
import java.lang.IndexOutOfBoundsException

class ViewSubjectAdapter(private val context: Context,private val subjectList: MutableList<SubjectListModel>) : RecyclerView.Adapter<ViewSubjectAdapter.ViewHolder>() {

    // Define a listener interface
    interface ViewSubjectAdapterListener {
        fun onNotificationCleared()
    }

    // Listener instance
    private var listener: ViewSubjectAdapterListener? = null
    private val swipeToDeleteCallback = SwipeToDeleteCallbackS(this)
    private val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
    private val pref: SharedPreferences = context.getSharedPreferences(StandardCompanion.SUBJECT_DATA_FILE_MANE, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSubjectViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(context, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(subjectList[position])
    }

    override fun getItemCount() = subjectList.size


    inner class ViewHolder(
        private val context: Context,
        private val binding: ItemSubjectViewBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(subjectListModel: SubjectListModel) {
            binding.apply {

                SubjectCode.text = subjectListModel.subjectCode
                SubjectName.text = "${subjectListModel.subjectName} in ${subjectListModel.subjectClassroomName}"
                SubjectYear.text = subjectListModel.yearView
                ElectiveSubjectCode.text = subjectListModel.electiveSubjectCode
                ElectiveSubjectName.text = "${subjectListModel.electiveSubjectName} in ${subjectListModel.electiveSubjectClassroomName}"
                if (subjectListModel.electiveSubjectName.isNullOrBlank() && subjectListModel.electiveSubjectCode.isNullOrBlank() && subjectListModel.electiveSubjectClassroomName.isNullOrBlank()){
                    ElectiveSubjectTitle.visibility = View.GONE
                    ElectiveSubject.visibility = View.GONE
                    MainSubjectTitle.visibility = View.VISIBLE
                    DividerElective.visibility = View.GONE
                } else {
                    DividerMain.visibility = View.GONE
                    ElectiveSubjectTitle.visibility = View.VISIBLE
                    ElectiveSubject.visibility = View.VISIBLE
                    MainSubjectTitle.visibility = View.GONE
                }
            }
        }
    }

    fun removeItem(position: Int) {
        try {
            val fromPrefKeys = pref.all.keys.toMutableList()
            editor.remove(fromPrefKeys[position])
            editor.commit()
            listener?.onNotificationCleared()
            notifyItemRemoved(position)
        } catch (e: IndexOutOfBoundsException) {
            Log.v("deletion", "current position: $position")
            for (i in pref.all.keys) {
                Log.v("deletion", "OutOfBoundsFile -> ${pref.getString(i, "")}")
            }
        }
    }

    // Setter method for the listener
    fun setListener(listener: ViewSubjectAdapterListener) {
        this.listener = listener
    }

    // Attach the ItemTouchHelper to the RecyclerView in your Fragment or Activity
    fun attachItemTouchHelper(recyclerView: RecyclerView) {
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}
