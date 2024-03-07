package com.extremex.tablemanager.admin.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.FragmentManageClassroomViewBinding
import com.extremex.tablemanager.lib.ClassroomModel
import com.extremex.tablemanager.lib.LocalStorageData
import com.extremex.tablemanager.adapter.ViewClassroomAdapter

class ManageClassroomFragment : Fragment(), ViewClassroomAdapter.ViewClassroomAdapterListener {
    interface ManageClassroomListener{
        fun onBack()
    }
    private lateinit var binding: FragmentManageClassroomViewBinding
    private var listener: ManageClassroomListener? = null
    private lateinit var localStorage: LocalStorageData
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
        localStorage = LocalStorageData(requireContext())
        addToListView()
        displayClassroom()

        binding.AddButton.setOnClickListener {
            if (binding.ClassroomNameText.text.toString().isNotBlank() && binding.ClassroomCode.text.toString().isNotBlank()){
                localStorage.createClassroom(binding.ClassroomNameText.text.toString(), binding.ClassroomCode.text.toString())
                binding.ClassroomNameText.text?.clear()
                binding.ClassroomCode.text?.clear()
                addToListView()
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
    @SuppressLint("NotifyDataSetChanged")
    private fun addToListView(){
        val classroomAdapter =
            ViewClassroomAdapter(requireContext(), localStorage.get(LocalStorageData.From.MANAGE_CLASSROOM) as MutableList<ClassroomModel>)
        classroomAdapter.setListener(this)
        classroomAdapter.attachItemTouchHelper(binding.ClassroomList)
        binding.ClassroomList.adapter = classroomAdapter
        binding.ClassroomList.layoutManager = LinearLayoutManager(requireContext())
        binding.ClassroomList.adapter?.notifyDataSetChanged()
    }
    private fun displayClassroom(){
        if ((localStorage.get(LocalStorageData.From.MANAGE_CLASSROOM) as MutableList<ClassroomModel>).isEmpty()){
            binding.ClassroomList.visibility = View.GONE
            binding.PlaceholderImage.visibility = View.VISIBLE
            binding.ClassroomViewTitle.visibility = View.GONE
        } else {
            binding.PlaceholderImage.visibility = View.GONE
            binding.ClassroomList.visibility = View.VISIBLE
            binding.ClassroomViewTitle.visibility = View.VISIBLE
        }

    }

    override fun onClassroomCleared() {
        addToListView()
        displayClassroom()
    }

}
//"ӿ"