package com.extremex.tablemanager.lib

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.extremex.tablemanager.databinding.ItemSubjectViewBinding

class ViewSubjectAdapter(private val context: Context,private val subjectList: MutableList<SubjectListModel>) : RecyclerView.Adapter<ViewSubjectAdapter.ViewHolder>() {
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
                SubjectName.text = "${subjectListModel.subjectName} in ${subjectListModel.cName}"
                SubjectYear.text = subjectListModel.year
                ElectiveSubjectCode.text = subjectListModel.electiveSubjectCode
                ElectiveSubjectName.text = "${subjectListModel.electiveSubjectName} in ${subjectListModel.ecName}"
                if (subjectListModel.electiveSubjectCode.isNullOrBlank()){
                    ElectiveSubjectTitle.visibility = View.GONE
                    ElectiveSubject.visibility = View.GONE
                    MainSubjectTitle.visibility = View.VISIBLE
                } else {
                    ElectiveSubjectTitle.visibility = View.VISIBLE
                    ElectiveSubject.visibility = View.VISIBLE
                    MainSubjectTitle.visibility = View.GONE
                }

                RemoveItemSubjectButton.visibility = View.GONE
                if (SubjectCode.text.toString().isNotBlank()) {

                    RemoveItemSubjectButton.visibility = View.VISIBLE
                    RemoveItemSubjectButton.setOnClickListener {
                        val position = adapterPosition
                        if (position != RecyclerView.NO_POSITION) {
                            subjectList[position].subjectCode?.let { it1 -> removeItem(it1, position) }
                        }
                    }
                } else {
                    RemoveItemSubjectButton.visibility = View.GONE
                }
            }
        }
    }

    private fun removeItem(key: String, position: Int) {
        val pref = context.getSharedPreferences("SubjectData", Context.MODE_PRIVATE)
        val edit = pref.edit()
        subjectList.removeAt(position)
        Log.w("remove key:","selected key is $key")
        edit.remove(key)
        edit.commit()
        notifyItemRemoved(position)
    }
}
