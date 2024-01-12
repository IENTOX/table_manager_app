package com.extremex.tablemanager.lib

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.DialogAddClassroomBinding

class AddClassroomDialog(private val context: Context) {

    private lateinit var binding: DialogAddClassroomBinding
    fun show() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_classroom, null)
        // Initialize view binding
        binding = DialogAddClassroomBinding.inflate(LayoutInflater.from(context))

        val dialogBuilder = AlertDialog.Builder(context,R.style.DialogBox)
            .setView(dialogView)
        val alertDialog = dialogBuilder.create()
        // Set click listeners for buttons
        binding.CancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        binding.AddButton.setOnClickListener {
            alertDialog.dismiss()
        }

        binding.viewAllClassroomsButton.setOnClickListener {
            if (binding.ClassroomList.isVisible){
                binding.ClassroomList.visibility = View.GONE
                binding.viewAllClassroomsButton.text = "View Added Classrooms"
            } else {
                binding.ClassroomList.visibility = View.VISIBLE
                binding.viewAllClassroomsButton.text = "Hide..."
            }
        }

        alertDialog.show()
    }
}
