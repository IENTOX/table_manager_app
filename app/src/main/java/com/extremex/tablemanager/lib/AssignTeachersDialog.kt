package com.extremex.tablemanager.lib

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.DialogAssignTeachersBinding

class AssignTeachersDialog(private val context: Context) {

    private lateinit var binding: DialogAssignTeachersBinding
    fun show() {
        //val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_assign_teachers, null)
        // Initialize view binding
        binding = DialogAssignTeachersBinding.inflate(LayoutInflater.from(context))

        val dialogBuilder = AlertDialog.Builder(context, R.style.DialogBox)
            .setView(binding.root)
        val alertDialog = dialogBuilder.create()

        var defaultSelection = subjectListView()[0]
        binding.SubjectSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.SubjectSpinner.notifySubtreeAccessibilityStateChanged(
                    view!!,
                    view,
                    position
                )
                defaultSelection = subjectListView()[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                defaultSelection = subjectListView()[0]
            }
        }

        alertDialog.show()
    }
    private fun subjectListView(): MutableList<String>{
        val subjects = mutableListOf<String>()
        val prefs = context.getSharedPreferences("SubjectData",Context.MODE_PRIVATE)
        val readKeys = readList("SubjectData")
        for (keys in 0 until readKeys.size) {
            val sCode = readKeys[keys]
            val sbody = prefs.getString(sCode,"")?.split("ӿ")
            val sName = sbody?.get(0)!!
            subjects.add(keys,"$sName ($sCode)")
        }
        return subjects
    }
    private fun readList(filename: String): MutableList<String>{
        val fileBuilder: FileBuilder = FileBuilder(context)
        return  fileBuilder.readDataFromFileForClassroomStorage(filename)
    }
}