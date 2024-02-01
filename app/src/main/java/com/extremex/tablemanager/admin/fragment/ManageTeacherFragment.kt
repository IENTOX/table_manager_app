package com.extremex.tablemanager.admin.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.FragmantManageTeacherViewBinding
import com.extremex.tablemanager.lib.FileBuilder
import com.extremex.tablemanager.lib.ManageAllTeachersAdapter
import com.extremex.tablemanager.lib.ManageTeachersModel

class ManageTeacherFragment: Fragment() {
    interface ManageTeacherListener{
        fun onBack()
    }
    private lateinit var _binding: FragmantManageTeacherViewBinding
    private val binding get() = _binding
    private var listener: ManageTeacherListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ManageTeacherListener){
            listener = context
        } else {
            throw IllegalArgumentException("ManageTeacherListener has to be implemented on root Activity")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmantManageTeacherViewBinding.inflate(LayoutInflater.from(context), container, false)


        val subjectList = subjectListView().ifEmpty { mutableListOf("No subject have been added") }
        val teachersList = if (requireContext().resources.getStringArray(R.array.TeacherNames).isNotEmpty()){
            requireContext().resources.getStringArray(R.array.TeacherNames)
        } else { arrayOf("No teachers have been added") }
        var selection = subjectList[0] // Get the default selection to start with
        var selectedTeacher = teachersList[0]
        val allTeacherList = mutableListOf(
            ManageTeachersModel("John Doe", "ID001", "Web Tech"),
            ManageTeachersModel("Jane Smith", "ID002", "Python"),
            ManageTeachersModel("Robert Brown", "ID003", "Java"),
            ManageTeachersModel("Emily Clark", "ID004", "Not Assigned"),
            ManageTeachersModel("Michael White", "ID005", "Maths")
        )
        val allAdapter = ManageAllTeachersAdapter(requireContext(), allTeacherList)
        binding.ManageTeachersListView.layoutManager = LinearLayoutManager(context)
        binding.ManageTeachersListView.adapter = allAdapter

        binding.SubjectSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, subjectList)
        binding.TeachersSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, teachersList)

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





        binding.BackBtn.setOnClickListener {
            listener?.onBack()
        }

        return binding.root
    }
    private fun subjectListView(): MutableList<String>{
        val subjects = mutableListOf<String>()
        val prefs = requireActivity().getSharedPreferences("SubjectData",Context.MODE_PRIVATE)
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
        val fileBuilder: FileBuilder = FileBuilder(requireContext())
        return  fileBuilder.readDataFromFileForClassroomStorage(filename)
    }
}