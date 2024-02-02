package com.extremex.tablemanager.admin.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.FragmentManageSubjectsViewBinding
import com.extremex.tablemanager.lib.FileBuilder
import com.extremex.tablemanager.lib.PopUpBox
import com.extremex.tablemanager.lib.SubjectListModel
import com.extremex.tablemanager.lib.ViewSubjectAdapter

class ManageSubjectFragment : Fragment(){

    interface ManageSubjectListener{
        fun onBack()
    }
    private lateinit var _binding: FragmentManageSubjectsViewBinding
    private val binding get() = _binding
    private var listener: ManageSubjectListener? = null
    private var isElective: Boolean = false
    private var isYear: Boolean = false
    private var year1: Int = 0
    private var year2: Int = 0
    private var year3: Int = 0
    private lateinit var subjectData:Map<String,String>

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
    ): View {
        _binding = FragmentManageSubjectsViewBinding.inflate(LayoutInflater.from(context), container, false)

        val classroomList = classroomListView().ifEmpty { mutableListOf("No classrooms have been added") } // Get the list of subjects
        val classroomList2 = classroomListView().ifEmpty { mutableListOf("No classrooms have been added") } // Get the list of subjects
        var selection1 = classroomList[0]  // Get the default selection to start with
        var selection2 = classroomList2[0]  // Get the default selection for elective to start with
        val itemList = if (requireContext().resources.getStringArray(R.array.NumberByTimes).isNotEmpty()){
            requireContext().resources.getStringArray(R.array.NumberByTimes)
        } else { arrayOf("Unresolved Numbers") }
        toggleSubjectList(binding)
        val customSpinnerAdapter = ArrayAdapter<Any?>(requireContext(), R.layout.item_simple_spinner_default,itemList)
        customSpinnerAdapter.setDropDownViewResource(R.layout.item_simple_spinner_default)

        binding.ElectiveCheckBox.setOnCheckedChangeListener { _, isChecked ->
            onElectiveChecked(binding,isChecked)
        }
        binding.NumberSetter.adapter = customSpinnerAdapter

        binding.Year1CheckBox.setOnCheckedChangeListener { _, isChecked ->
            year1 = if (isChecked){ 1 } else {0}
        }
        binding.Year2Checkbox.setOnCheckedChangeListener { _, isChecked ->
            year2 = if (isChecked){ 1 } else {0}
        }
        binding.Year3CheckBox.setOnCheckedChangeListener { _, isChecked ->
            year3 = if (isChecked){ 1 } else {0}
        }
        binding.AddButton.setOnClickListener {
            if (year1 + year2 + year3 == 0 || year1 + year2 + year3 < 0 || year1 + year2 + year3 > 3){
                isYear = false
                PopUpBox(requireContext(),"Dismiss","You have to select at least a year before proceeding.",true)
            } else {
                isYear = true
                verifySubjects(
                    binding.SubjectNameText.text.toString(),
                    binding.SubjectCode.text.toString(),
                    intArrayOf(year1,year2,year3),
                    selection1,
                    binding.ElectiveSubjectNameText.text.toString(),
                    binding.ElectiveSubjectCode.text.toString(),
                    selection2
                )
                toggleSubjectList(binding)
            }
        }

        binding.ClassroomCodeSetter.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, classroomList)
        binding.ClassroomCodeElectiveSetter.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, classroomList)

        binding.ClassroomCodeSetter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selection1 = classroomList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selection1 = classroomList[0] // Update default Selection with the first subject if nothing is selected
            }
        }

        binding.ClassroomCodeElectiveSetter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selection2 = classroomList2[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selection2 = classroomList2[1] // Update default Selection with the first subject if nothing is selected
            }
        }
        binding.BackButton.setOnClickListener {
            listener?.onBack()
        }

        return binding.root
    }

    private fun verifySubjects(
        subjectName: String,
        subjectCode: String,
        year: IntArray,
        cName: String,
        electiveSubjectName: String ="",
        electiveSubjectCode: String="",
        ecName: String = ""
    ){
        if (subjectName.isNotEmpty() && subjectCode.isNotEmpty()){
            if ( isElective){
                if (electiveSubjectName.isNotEmpty() && electiveSubjectCode.isNotEmpty()){
                    if (isYear){
                        storeSubjectData(subjectName, subjectCode, year, cName, electiveSubjectName, electiveSubjectCode, ecName)
                    } else {
                        PopUpBox(requireContext(),"Dismiss","ERR:007\nYear not selected or invalid year selection, selecting a year is important. ",true)
                    }
                } else {
                    PopUpBox(requireContext(),"Dismiss","ERR:006\nError caused due to failure in verifying Elective Subject",true)
                }
            } else {
                storeSubjectData(subjectName,subjectCode, year, cName)
            }
        } else {
            PopUpBox(requireContext(),"Dismiss","ERR:005\nError caused due to failure in verifying Subject",true)
        }
    }

    private fun onElectiveChecked(binding: FragmentManageSubjectsViewBinding, checked: Boolean) {
        if (checked){
            isElective = true
            binding.ClassroomCodeElectiveBox.visibility = View.VISIBLE
            binding.ElectiveSubjectNameText.visibility = View.VISIBLE
            binding.ElectiveSubjectNameTitle.visibility = View.VISIBLE
            binding.Divider.visibility = View.VISIBLE
        } else {
            isElective = false
            binding.ClassroomCodeElectiveBox.visibility = View.GONE
            binding.ElectiveSubjectNameText.visibility = View.GONE
            binding.ElectiveSubjectNameTitle.visibility = View.GONE
            binding.Divider.visibility = View.GONE
        }
    }
    private fun addToListView(binding: FragmentManageSubjectsViewBinding){
        var subjectList: MutableList<SubjectListModel> = mutableListOf(SubjectListModel("","", "",""))
        val prefs = requireActivity().getSharedPreferences("SubjectData",Context.MODE_PRIVATE)
        val readKeys = readList("SubjectData")
        for (keys in 0 until readKeys.size) {
            val sCode = readKeys[keys]
            val sbody = prefs.getString(sCode,"")?.split("ӿ")
            val sName = sbody?.get(0)!!
            val sYear = sbody[2]
            val cName= sbody[3]
            val esName = sbody[5]
            val esCode = sbody[4]
            val ecCode = sbody[6]

            subjectList.add(SubjectListModel(sName,sCode,sYear,cName,esName,esCode,ecCode))
        }

        subjectList.removeAt(0)
        val subjectListAdapter = ViewSubjectAdapter(requireContext(), subjectList)
        binding.SubjectList.adapter = subjectListAdapter
        binding.SubjectList.layoutManager = LinearLayoutManager(requireContext())
        binding.SubjectList.adapter.let {
            it?.notifyItemRangeInserted(0, subjectList.size)
        }
    }
    private fun classroomListView(): MutableList<String>{
        val subjects = mutableListOf<String>()
        val prefs = requireActivity().getSharedPreferences("ClassRoomData",Context.MODE_PRIVATE)
        val read = readList("ClassRoomData")
        for (keys in 0 until read.size) {
            val cCode = read[keys]
            val cName = prefs.getString(cCode,"")
            if (cCode != "") {
                subjects.add(keys, "$cName ($cCode)")
            }
        }
        return subjects
    }
    private fun storeSubjectData(
        subjectName: String,
        subjectCode: String,
        year: IntArray,
        cName: String,
        electiveSubjectName: String ="",
        electiveSubjectCode: String="",
        ecName: String =""
    ){
        val fileBuilder: FileBuilder = FileBuilder(requireContext())
        subjectData = mapOf(subjectCode to "${subjectName+"ӿ"+subjectCode+"ӿ"+yearDeterminer(year)+"ӿ"+cName+"ӿ"+electiveSubjectCode+"ӿ"+electiveSubjectName+"ӿ"+ecName}")
        fileBuilder.makeFileForStorage("SubjectData", subjectData)

    }
    private fun yearDeterminer(year: IntArray): String {
        return when {
            year.contentEquals(intArrayOf(1, 0, 0)) -> "First year"
            year.contentEquals(intArrayOf(0, 1, 0)) -> "Second year"
            year.contentEquals(intArrayOf(0, 0, 1)) -> "Third year"
            year.contentEquals(intArrayOf(1, 1, 0)) -> "First and Second year"
            year.contentEquals(intArrayOf(0, 1, 1)) -> "Second and Third year"
            year.contentEquals(intArrayOf(1, 0, 1)) -> "First and Third year"
            year.contentEquals(intArrayOf(1, 1, 1)) -> "First, Second, and Third year"
            else -> "Year not determined"
        }
    }
    private fun readList(filename: String): MutableList<String>{
        val fileBuilder: FileBuilder = FileBuilder(requireContext())
        return  fileBuilder.readDataFromFileForClassroomStorage(filename)
    }
    private fun toggleSubjectList(binding: FragmentManageSubjectsViewBinding) {
        if (readList("ClassRoomData").isEmpty()){
            binding.PlaceholderImage.visibility = View.VISIBLE
            binding.SubjectList.visibility = View.GONE
        } else {
            addToListView(binding)
            binding.PlaceholderImage.visibility = View.GONE
            binding.SubjectList.visibility = View.VISIBLE
        }
    }
}