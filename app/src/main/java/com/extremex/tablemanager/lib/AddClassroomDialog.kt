package com.extremex.tablemanager.lib

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.DialogAddClassroomBinding

class AddClassroomDialog(private val context: Context) {

    private lateinit var classroomData : Map<String,String>
    fun show() {
        //val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_classroom, null)
        val binding = DialogAddClassroomBinding.inflate(LayoutInflater.from(context))
        val dialogBuilder = AlertDialog.Builder(context,R.style.DialogBox).setView(binding.root)
        val alertDialog = dialogBuilder.create()

        binding.CancelButton.setOnClickListener {
            alertDialog.dismiss()
        }
        binding.SaveButton.setOnClickListener {
            addToListView(binding)
            alertDialog.dismiss()
        }

        binding.AddButton.setOnClickListener {
            if (binding.ClassroomNameText.text.toString().isNotBlank() && binding.ClassroomCode.text.toString().isNotBlank()){
                storeClassroomData(binding.ClassroomNameText.text.toString(), binding.ClassroomCode.text.toString())
                binding.ClassroomNameText.text?.clear()
                binding.ClassroomCode.text?.clear()
            } else {
                binding.ClassroomNameText.error = "Required Field"
                binding.ClassroomCode.error = "Required Field"
            }
        }

        binding.viewAllClassroomsButton.setOnClickListener {
            if (binding.ClassroomList.isVisible){
                binding.ClassroomList.visibility = View.GONE
                binding.viewAllClassroomsButton.text = "View Added Classrooms"
                binding.PlaceholderImage.visibility = View.VISIBLE
            } else {
                addToListView(binding)
                binding.PlaceholderImage.visibility = View.GONE
                binding.ClassroomList.visibility = View.VISIBLE
                binding.viewAllClassroomsButton.text = "Hide..."
            }
        }

        alertDialog.show()
    }
    private fun addToListView(binding: DialogAddClassroomBinding){
        var classroomList: MutableList<ClassroomModel> = mutableListOf(ClassroomModel("",""))
        val prefs = context.getSharedPreferences("ClassRoomData",Context.MODE_PRIVATE)
        val read = readList("ClassRoomData")
        for (keys in 0 until read.size) {
            val cCode = read[keys]
            val cName = prefs.getString(cCode,"")
            classroomList.add(ClassroomModel(cName, cCode))
        }

        classroomList.removeAt(0)
        val classroomAdapter = ViewClassroomAdapter(context, classroomList)
        binding.ClassroomList.adapter = classroomAdapter
        binding.ClassroomList.layoutManager =
            LinearLayoutManager(context)
    }
    private fun storeClassroomData(classroomName: String, classroomCode: String){
        val fileBuilder: FileBuilder = FileBuilder(context)
        classroomData = mapOf(classroomCode to classroomName)
        fileBuilder.makeFileForStorage("ClassRoomData", classroomData)

    }
    private fun readList(filename: String): MutableList<String>{
        val fileBuilder: FileBuilder = FileBuilder(context)
        return  fileBuilder.readDataFromFileForClassroomStorage(filename)
    }
}
