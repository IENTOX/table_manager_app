package com.extremex.tablemanager.lib

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.findViewTreeLifecycleOwner
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

        val subjectList = subjectListView() // Get the list of subjects
        val teachersList = context.resources.getStringArray(R.array.TeacherNames)
        var selection = subjectList[0]  // Get the default selection to start with
        var selectedTeacher = teachersList[0]

        binding.SubjectSpinner.adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, subjectList)
        binding.TeachersSpinner.adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, teachersList)

        binding.SubjectSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selection = subjectList[position] // Update default Selection with the selected subject
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selection = subjectList[0] // Update default Selection with the first subject if nothing is selected
            }
        }
        binding.TeachersSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedTeacher = teachersList[position] // Update default Selection with the selected subject
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedTeacher = teachersList[0] // Update default Selection with the first subject if nothing is selected
            }
        }

        binding.CancelButton.setOnClickListener {
            alertDialog.dismiss()
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
            val esName = sbody[4]
            val esCode = sbody[3]
            if (esCode != "") {
                subjects.add(keys, "$esName ($esCode)")
            }
            if(sCode != "") {
                subjects.add(keys,"$sName ($sCode)")
            }
        }
        return subjects
    }
    private fun assignTeacher(teacherName: Array<String>, subjects: Array<String>) {
        val assignTeachers: List<Map<Array<String>, Array<String>>> = listOf(
            mapOf(
                teacherName to subjects
            ),
        )
    }
    private fun readList(filename: String): MutableList<String>{
        val fileBuilder: FileBuilder = FileBuilder(context)
        return  fileBuilder.readDataFromFileForClassroomStorage(filename)
    }
}