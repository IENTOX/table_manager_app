package com.extremex.tablemanager.lib

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.DialogAssignTeachersBinding

class AssignTeachersDialog(private val context: Context) {

    private lateinit var binding: DialogAssignTeachersBinding
    fun show() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_assign_teachers, null)
        // Initialize view binding
        binding = DialogAssignTeachersBinding.inflate(LayoutInflater.from(context))

        val dialogBuilder = AlertDialog.Builder(context, R.style.DialogBox)
            .setView(dialogView)
        val alertDialog = dialogBuilder.create()



        alertDialog.show()
    }
}