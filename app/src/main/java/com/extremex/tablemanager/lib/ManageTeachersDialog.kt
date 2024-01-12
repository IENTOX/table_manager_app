package com.extremex.tablemanager.lib

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.DialogManageTeachersBinding

class ManageTeachersDialog(private val context: Context) {

    private lateinit var binding: DialogManageTeachersBinding
    fun show() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_manage_teachers, null)
        // Initialize view binding
        binding = DialogManageTeachersBinding.inflate(LayoutInflater.from(context))

        val dialogBuilder = AlertDialog.Builder(context, R.style.DialogBox)
            .setView(dialogView)
        val alertDialog = dialogBuilder.create()



        alertDialog.show()
    }
}