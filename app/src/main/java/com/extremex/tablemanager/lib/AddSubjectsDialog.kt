package com.extremex.tablemanager.lib

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.DialogAddClassroomBinding
import com.extremex.tablemanager.databinding.DialogAddSubjectsBinding
import java.time.Year

class AddSubjectsDialog(private val context: Context) {

    private lateinit var binding: DialogAddSubjectsBinding
    private var isElective: Boolean = false
    private var isYear: Boolean = false
    private lateinit var subjectListData: MutableList<SubjectListModel>
    private var year1: Int = 0
    private var year2: Int = 0
    private var year3: Int = 0
    private lateinit var subjectData:Map<String,String>
    fun show() {
        //val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_subjects, null)
        // Initialize view binding
        binding = DialogAddSubjectsBinding.inflate(LayoutInflater.from(context))

        val dialogBuilder = AlertDialog.Builder(context, R.style.DialogBox)
            .setView(binding.root)
        val alertDialog = dialogBuilder.create()
        binding.ElectiveCheckBox.setOnCheckedChangeListener { _, isChecked ->
            onElectiveChecked(binding,isChecked)
            }

        binding.viewAllSubjectsButton.setOnClickListener {
            toggleSubjectList(binding)
        }
        binding.AddButton.setOnClickListener {
            verifySubjects(
                binding.SubjectNameText.text.toString(),
                binding.SubjectCode.text.toString(),
                binding.ElectiveSubjectNameText.text.toString(),
                binding.ElectiveSubjectCode.text.toString()
            )
            alertDialog.dismiss()
        }
        binding.CancelButton.setOnClickListener {
            alertDialog.dismiss()
        }



        alertDialog.show()
    }

    private fun verifySubjects(
        subjectName: String,
        subjectCode: String,
        electiveSubjectName: String ="",
        electiveSubjectCode: String=""){
        if (subjectName.isNotEmpty() && subjectCode.isNotEmpty()){
            if ( isElective){
                if (electiveSubjectName.isNotEmpty() && electiveSubjectCode.isNotEmpty()){
                    if (isYear){

                    } else {
                        //TODO: elective sub verification faile
                    }
                } else {
                    //TODO: elective sub verification failed
                }
            } else {
                //TODO: add code for non elective subjects
            }

        } else {
            //TODO: sub verification failed
        }

    }
    private fun checkYearSelected(context: Context, binding: DialogAddSubjectsBinding): IntArray {
        binding.Year1CheckBox.setOnCheckedChangeListener { _, isChecked ->
            year1 = if (isChecked){ 1 } else {0}
        }
        binding.Year2Checkbox.setOnCheckedChangeListener { _, isChecked ->
            year2 = if (isChecked){ 1 } else {0}
        }
        binding.Year3CheckBox.setOnCheckedChangeListener { _, isChecked ->
            year3 = if (isChecked){ 1 } else {0}
        }
        if (year1 + year2 + year3 == 0 && year1 + year2 + year3 < 0 && year1 + year2 + year3 > 3){
            isYear = false
            PopUpBox(context,"Dismiss","You have to select at least a year before proceeding.",true)
        } else {
            isYear = true
            return intArrayOf(year1,year2,year3)
        }
    }

    private fun onElectiveChecked(binding: DialogAddSubjectsBinding, checked: Boolean) {
        if (checked){
            isElective = true
            binding.ElectiveSubjectNameTitle.visibility = View.VISIBLE
            binding.ElectiveSubjectCode.visibility = View.VISIBLE
            binding.ElectiveSubjectNameText.visibility = View.VISIBLE
            binding.ElectiveSubjectCodeTitle.visibility = View.VISIBLE
        } else {
            isElective = false
            binding.ElectiveSubjectNameTitle.visibility = View.GONE
            binding.ElectiveSubjectCodeTitle.visibility = View.GONE
            binding.ElectiveSubjectCode.visibility = View.GONE
            binding.ElectiveSubjectNameText.visibility = View.GONE
        }
    }
    private fun addToListView(binding: DialogAddSubjectsBinding){
        var subjectList: MutableList<SubjectListModel> = mutableListOf(SubjectListModel("","", intArrayOf(1,1,1)))
        val prefs = context.getSharedPreferences("SubjectData",Context.MODE_PRIVATE)
        val readKeys = readList("SubjectData")
        for (keys in 0 until readKeys.size) {
            val sCode = readKeys[keys]
            val sbody = prefs.getString(sCode,"")?.split("ӿ")
            val sName = sbody?.get(0)!!
            val sYear = sbody?.get(2)!!
            val esName = sbody?.get(4)
            val esCode = sbody?.get(3)

            subjectList.add(SubjectListModel(sName,sCode,sYear,esName,esCode))
        }

        subjectList.removeAt(0)
        val subjectListAdapter = ViewSubjectAdapter(context, subjectList)
        binding.SubjectList.adapter = subjectListAdapter
        binding.SubjectList.layoutManager =
            LinearLayoutManager(context)
    }
    private fun storeSubjectData(
        subjectName: String,
        subjectCode: String,
        year: IntArray,
        electiveSubjectName: String ="",
        electiveSubjectCode: String=""){
        val fileBuilder: FileBuilder = FileBuilder(context)
        subjectData = mapOf(subjectCode to "${subjectName+"ӿ"+subjectCode+"ӿ"+yearDeterminer(year)+"ӿ"+electiveSubjectCode+"ӿ"+electiveSubjectName}")
        fileBuilder.makeFileForStorage("ClassRoomData", subjectData)

    }
    private fun yearDeterminer(year: IntArray): String {
        when (year) {
            intArrayOf(1,0,0) -> return "First year"
            intArrayOf(0,1,0) -> return "Second year"
            intArrayOf(0,0,1) -> return "Third year"
            intArrayOf(1,1,0) -> return "First and Second year"
            intArrayOf(0,1,1) -> return "Second and Third year"
            intArrayOf(1,0,1) -> return "First and Third year"
            intArrayOf(1,1,1) -> return "First, Second and third year"
        }
    }
    private fun readList(filename: String): MutableList<String>{
        val fileBuilder: FileBuilder = FileBuilder(context)
        return  fileBuilder.readDataFromFileForClassroomStorage(filename)
    }
    private fun toggleSubjectList(binding: DialogAddSubjectsBinding) {
        if (binding.SubjectList.isVisible){
            binding.PlaceholderImage.visibility = View.VISIBLE
            binding.SubjectList.visibility = View.GONE
            binding.viewAllSubjectsButton.text = "View Added Subjects"
        } else {
            binding.PlaceholderImage.visibility = View.GONE
            binding.SubjectList.visibility = View.VISIBLE
            binding.viewAllSubjectsButton.text = "Hide..."
        }
    }
}