package com.extremex.tablemanager.admin.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.extremex.tablemanager.databinding.FragmentManageClassroomViewBinding
import com.extremex.tablemanager.lib.ClassroomModel
import com.extremex.tablemanager.lib.FileBuilder
import com.extremex.tablemanager.lib.ViewClassroomAdapter

class ManageClassroomFragment : Fragment() {
    interface ManageClassroomListener{
        fun onBack()
    }
    private lateinit var _binding: FragmentManageClassroomViewBinding
    private val binding get() = _binding
    private var listener: ManageClassroomListener? = null
    private lateinit var classroomData : Map<String,String>

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
    ): View {
        _binding = FragmentManageClassroomViewBinding.inflate(LayoutInflater.from(context), container, false)

        binding.AddButton.setOnClickListener {
            if (binding.ClassroomNameText.text.toString().isNotBlank() && binding.ClassroomCode.text.toString().isNotBlank()){
                storeClassroomData(binding.ClassroomNameText.text.toString(), binding.ClassroomCode.text.toString())
                binding.ClassroomNameText.text?.clear()
                binding.ClassroomCode.text?.clear()
                addToListView(binding)
            } else {
                binding.ClassroomNameText.error = "Required Field"
                binding.ClassroomCode.error = "Required Field"
            }
        }

        if (readList("ClassRoomData").isEmpty()){
            binding.ClassroomList.visibility = View.GONE
            binding.PlaceholderImage.visibility = View.VISIBLE
            binding.ClassroomViewTitle.visibility = View.GONE
        } else {
            binding.PlaceholderImage.visibility = View.GONE
            binding.ClassroomList.visibility = View.VISIBLE
            binding.ClassroomViewTitle.visibility = View.VISIBLE
            addToListView(binding)
        }
        binding.BackButton.setOnClickListener {
            listener?.onBack()
        }

        return binding.root
    }
    private fun addToListView(binding: FragmentManageClassroomViewBinding){
        var classroomList: MutableList<ClassroomModel> = mutableListOf(ClassroomModel("",""))
        val prefs = requireActivity().getSharedPreferences("ClassRoomData",Context.MODE_PRIVATE)
        val read = readList("ClassRoomData")
        for (keys in 0 until read.size) {
            val cCode = read[keys]
            val cName = prefs.getString(cCode,"")
            classroomList.add(ClassroomModel(cName, cCode))
        }

        classroomList.removeAt(0)
        val classroomAdapter = ViewClassroomAdapter(requireContext(), classroomList)
        classroomAdapter.attachItemTouchHelper(binding.ClassroomList)
        classroomAdapter.attachItemTouchHelper(binding.ClassroomList)
        binding.ClassroomList.adapter = classroomAdapter
        binding.ClassroomList.layoutManager = LinearLayoutManager(requireContext())
        // live updates the list
        binding.ClassroomList.adapter.let {
            it?.notifyItemRangeInserted(0, classroomList.size)
        }
    }
    private fun storeClassroomData(classroomName: String, classroomCode: String){
        val fileBuilder: FileBuilder = FileBuilder(requireContext())
        classroomData = mapOf(classroomCode to classroomName)
        fileBuilder.makeFileForStorage("ClassRoomData", classroomData)

    }
    private fun readList(filename: String): MutableList<String>{
        val fileBuilder: FileBuilder = FileBuilder(requireContext())
        return  fileBuilder.readDataFromFileForClassroomStorage(filename)
    }
}