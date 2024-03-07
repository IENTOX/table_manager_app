package com.extremex.tablemanager.admin.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.FragmentManageSubjectsViewBinding
import com.extremex.tablemanager.lib.CustomDialog
import com.extremex.tablemanager.lib.LocalStorageData
import com.extremex.tablemanager.lib.SubjectListModel
import com.extremex.tablemanager.adapter.ViewSubjectAdapter

@Suppress("UNCHECKED_CAST")
class ManageSubjectFragment : Fragment(), ViewSubjectAdapter.ViewSubjectAdapterListener{

    interface ManageSubjectListener{
        fun onBack()
    }
    private lateinit var binding: FragmentManageSubjectsViewBinding
    private var listener: ManageSubjectListener? = null
    private lateinit var localStorage: LocalStorageData
    private var isElective: Boolean = false
    private var years = arrayOf(0,0,0)


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ManageSubjectListener){
            listener = context
        } else {
            throw IllegalArgumentException("ManageSubjectListener has to be implemented to the root Activity")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { return inflater.inflate(R.layout.fragment_manage_subjects_view, container, false) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentManageSubjectsViewBinding.bind(view)

        val errorBox = CustomDialog(requireContext())
        localStorage = LocalStorageData(requireContext())
        toggleSubjectList()
        addToListView(requireContext())
        val classroomList = localStorage.getSpinnerClassroomItems().ifEmpty { mutableListOf("No classrooms") } // Get the list of subjects
        val classroomList2 = localStorage.getSpinnerClassroomItems().ifEmpty { mutableListOf("No classrooms") } // Get the list of subjects
        val itemList = if (requireContext().resources.getStringArray(R.array.NumberByTimes).isNotEmpty()){
            requireContext().resources.getStringArray(R.array.NumberByTimes)
        } else { arrayOf("Unresolved Numbers") }

        var selection1 = classroomList[0]  // Get the default selection to start with
        var selection2 = classroomList2[0]  // Get the default selection for elective to start with
        var selection3Position = 0

        binding.ClassroomCodeSetter.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, classroomList)
        binding.ClassroomCodeElectiveSetter.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, classroomList2)
        binding.NumberSetter.adapter =  ArrayAdapter(requireContext(), R.layout.item_simple_spinner_default,itemList)

        binding.NumberSetter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) { selection3Position = position }
            override fun onNothingSelected(parent: AdapterView<*>?) { selection3Position = 0 }
        }

        binding.ClassroomCodeSetter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) { selection1 = classroomList[position] }
            override fun onNothingSelected(parent: AdapterView<*>?) { selection1 = classroomList[0] }// Update default Selection with the first subject if nothing is selected
        }

        binding.ClassroomCodeElectiveSetter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) { selection2 = classroomList2[position] }
            override fun onNothingSelected(parent: AdapterView<*>?) { selection2 = classroomList2[1]} // Update default Selection with the first subject if nothing is selected
        }

        binding.ElectiveCheckBox.setOnCheckedChangeListener { _, isChecked ->
            onElectiveChecked(isChecked)
        }
        binding.Year1CheckBox.setOnCheckedChangeListener { _, isChecked ->
            years[0] = if (isChecked){ 1 } else { 0 }
        }
        binding.Year2Checkbox.setOnCheckedChangeListener { _, isChecked ->
            years[1] = if (isChecked){ 1 } else { 0 }
        }
        binding.Year3CheckBox.setOnCheckedChangeListener { _, isChecked ->
            years[2] = if (isChecked){ 1 } else { 0 }
        }
        binding.AddButton.setOnClickListener {
            if (years[0] + years[1] + years[2] <= 0 || years[0] + years[1] + years[2] > 3){
                errorBox.createBasicCustomDialog(
                    "Dismiss",
                    "You have to select at least a year before proceeding.",
                    true
                )
            } else if ((binding.ElectiveCheckBox.isChecked && binding.ElectiveSubjectNameText.text.isNullOrBlank()) ||
                (binding.ElectiveCheckBox.isChecked && binding.ElectiveSubjectCode.text.isNullOrBlank())) {
                errorBox.createBasicCustomDialog(
                    "Dismiss",
                    "Elective subject fields cannot be empty, If you don't have any then please uncheck the elective option.",
                    true
                )
            } else {
                val electiveClassroomSelection = if (binding.ElectiveCheckBox.isChecked) selection2 else null
                val cc = if (binding.ClassroomCodeSetter.selectedItem.toString() == "No classrooms have been added") "Classroom Undefined" else selection1
                val ecc = if (electiveClassroomSelection == "No classrooms have been added") null else electiveClassroomSelection
                val esn = binding.ElectiveSubjectNameText.text.toString().ifBlank { null }
                val esc = binding.ElectiveSubjectCode.text.toString().ifBlank { null }

                val data = verifySubjects(binding.SubjectNameText.text.toString(), binding.SubjectCode.text.toString(), cc, esn, esc, ecc, selection3Position, years)

                if (data != null) {
                    localStorage.createSubject(
                        data.subjectName,
                        data.subjectCode,
                        data.subjectClassroomName,
                        data.electiveSubjectName,
                        data.electiveSubjectCode,
                        data.electiveSubjectClassroomName,
                        data.subjectPerWeek,
                        data.year
                    )
                } else {
                    errorBox.createBasicCustomDialog(
                        "Close",
                        "Error while processing the Data",
                        true
                    )
                }
                toggleSubjectList()
                addToListView(requireContext())
                clearViews()
            }
        }

        binding.BackButton.setOnClickListener {
            listener?.onBack()
        }

    }

    private fun onElectiveChecked(checked: Boolean){
        if (checked) {
            isElective = true
            binding.AddElectiveSubjectTitle.visibility = View.VISIBLE
            binding.AddElectiveSubjectContainer.visibility = View.VISIBLE
            binding.Divider2.visibility = View.VISIBLE
        } else {
            isElective = false
            binding.AddElectiveSubjectTitle.visibility = View.GONE
            binding.AddElectiveSubjectContainer.visibility = View.GONE
            binding.Divider2.visibility = View.GONE
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun addToListView(context: Context){
        val finalList: MutableList<SubjectListModel> =
            localStorage.get(LocalStorageData.From.MANAGE_SUBJECT) as MutableList<SubjectListModel>
        val subjectListAdapter = ViewSubjectAdapter(context, finalList)
        subjectListAdapter.setListener(this)
        subjectListAdapter.attachItemTouchHelper(binding.SubjectList)
        binding.SubjectList.adapter = subjectListAdapter
        binding.SubjectList.layoutManager = LinearLayoutManager(context)
        binding.SubjectList.adapter?.notifyDataSetChanged()
    }
    private fun verifySubjects(
        subjectName: String,
        subjectCode: String,
        subjectClassroom: String,
        electiveSubjectName: String?,
        electiveSubjectCode: String?,
        electiveSubjectClassroom: String?,
        subjectCountPerWeek: Int,
        subjectYears: Array<Int>
    ): SubjectListModel? {
        var data: SubjectListModel? = null
        if (subjectName.isNotEmpty() && subjectCode.isNotEmpty()){
            if (subjectYears[0] + subjectYears[1] + subjectYears[2] in 0..3){
                data = if (electiveSubjectName?.isNotEmpty() == true && electiveSubjectCode?.isNotEmpty() == true){
                    Log.v("NON-NULL", "Value =  esn: $electiveSubjectName esc: $electiveSubjectCode ecc: $electiveSubjectClassroom")
                    SubjectListModel(subjectName, subjectCode, subjectClassroom,electiveSubjectName, electiveSubjectCode, electiveSubjectClassroom, subjectCountPerWeek, subjectYears,"")
                } else {
                    Log.v("NULL", "Value =  esn: $electiveSubjectName esc: $electiveSubjectCode ecc: $electiveSubjectClassroom")
                    SubjectListModel(subjectName, subjectCode, subjectClassroom,electiveSubjectName, electiveSubjectCode, electiveSubjectClassroom, subjectCountPerWeek, subjectYears,"")
                }
            }
        }
        return data
    }
    private fun toggleSubjectList() {
        if (localStorage.get(LocalStorageData.From.MANAGE_SUBJECT).isEmpty()) {
            binding.PlaceholderImage.visibility = View.VISIBLE
            binding.SubjectList.visibility = View.GONE
        } else {
            binding.PlaceholderImage.visibility = View.GONE
            binding.SubjectList.visibility = View.VISIBLE
        }
    }

    private fun clearViews() {
        if (::binding.isInitialized) {
            binding.apply {
                if (SubjectNameText.text.toString().isNotEmpty()) {
                    SubjectNameText.text?.clear()
                }
                if (SubjectCode.text.toString().isNotEmpty()) {
                    SubjectCode.text?.clear()
                }
                if (ElectiveSubjectNameText.text.toString().isNotEmpty()) {
                    ElectiveSubjectNameText.text?.clear()
                }
                if (ElectiveSubjectCode.text.toString().isNotEmpty()) {
                    ElectiveSubjectCode.text?.clear()
                }

                ElectiveCheckBox.isChecked = false
                NumberSetter.setSelection(0)
                Year1CheckBox.isChecked = false
                Year2Checkbox.isChecked = false
                Year3CheckBox.isChecked = false
            }
        }
    }

    override fun onNotificationCleared() {
        addToListView(requireContext())
        toggleSubjectList()
    }
}