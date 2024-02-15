package com.extremex.tablemanager.admin.fragment

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.FragmentManageClassroomViewBinding
import com.extremex.tablemanager.lib.ClassroomModel
import com.extremex.tablemanager.lib.DateModel
import com.extremex.tablemanager.lib.DurationUnit
import com.extremex.tablemanager.lib.FileBuilder
import com.extremex.tablemanager.lib.HolidayInfoModel
import com.extremex.tablemanager.lib.StandardCompanion
import com.extremex.tablemanager.lib.ViewClassroomAdapter

class ManageClassroomFragment : Fragment() {
    interface ManageClassroomListener{
        fun onBack()
    }
    private lateinit var binding: FragmentManageClassroomViewBinding
    private var listener: ManageClassroomListener? = null
    private lateinit var classroomData : Map<String,String>
    private lateinit var classroomPref: SharedPreferences
    private lateinit var classroomPrefEditor: Editor
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ManageClassroomListener){
            listener = context
        } else {
            throw IllegalArgumentException("ManageClassroomListener has to be implemented to the root Activity")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { return inflater.inflate(R.layout.fragment_manage_classroom_view,container, false) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentManageClassroomViewBinding.bind(view)

        classroomPref = requireContext().getSharedPreferences(StandardCompanion.CLASSROOM_DATA_FILE_MANE,AppCompatActivity.MODE_PRIVATE)
        classroomPrefEditor = classroomPref.edit()

        displayClassroom()

        binding.AddButton.setOnClickListener {
            if (binding.ClassroomNameText.text.toString().isNotBlank() && binding.ClassroomCode.text.toString().isNotBlank()){
                createClassroom(binding.ClassroomNameText.text.toString(), binding.ClassroomCode.text.toString())
                binding.ClassroomNameText.text?.clear()
                binding.ClassroomCode.text?.clear()
                addToListView(binding)
                displayClassroom()
            } else {
                binding.ClassroomNameText.error = "Required Field"
                binding.ClassroomCode.error = "Required Field"
            }
        }
        binding.BackButton.setOnClickListener {
            listener?.onBack()
        }
    }
    private fun addToListView(binding: FragmentManageClassroomViewBinding){
        val classroomAdapter = ViewClassroomAdapter(requireContext(), getClassroom())
        classroomAdapter.attachItemTouchHelper(binding.ClassroomList)
        classroomAdapter.attachItemTouchHelper(binding.ClassroomList)
        binding.ClassroomList.adapter = classroomAdapter
        binding.ClassroomList.layoutManager = LinearLayoutManager(requireContext())
        // live updates the list
        binding.ClassroomList.adapter.let {
            it?.notifyItemRangeInserted(0, getClassroom().size)
        }
    }
    private fun displayClassroom(){
        if (getClassroom().isEmpty()){
            binding.ClassroomList.visibility = View.GONE
            binding.PlaceholderImage.visibility = View.VISIBLE
            binding.ClassroomViewTitle.visibility = View.GONE
        } else {
            binding.PlaceholderImage.visibility = View.GONE
            binding.ClassroomList.visibility = View.VISIBLE
            binding.ClassroomViewTitle.visibility = View.VISIBLE
            addToListView(binding)
        }

    }

    private fun createClassroom(classroomName: String, classroomCode: String){
        val position = (classroomPref.all.keys.size + 1) ?: 0
        val holiday = classroomName+"ӿ"+classroomCode
        classroomPrefEditor.putString(position.toString(), holiday)
        classroomPrefEditor.commit()
    }

    private fun getClassroom(): MutableList<ClassroomModel>{
        var classrooms = mutableListOf<ClassroomModel>()
        val holidayObjectSize = (classroomPref.all.keys.size) ?: null
        val value_d = classroomPref.all.values.toMutableList()
        if (holidayObjectSize != null){
            for (data in value_d){
                val raw: MutableList<String> = data.toString().split("ӿ").toMutableList() ?: mutableListOf("Unknown")
                if (raw[0] != "Unknown") {
                    val classroomName = raw[0]
                    val classroomCode = raw[1]

                    classrooms.add(ClassroomModel(classroomName, classroomCode))
                }
            }
        } else {
            classroomPref.getString(1.toString(),"None")
            Log.e("Retrieved Holiday Data 2", "null")
        }
        return classrooms
    }
}
//"ӿ"