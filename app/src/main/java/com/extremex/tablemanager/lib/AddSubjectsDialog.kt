package com.extremex.tablemanager.lib

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.DialogAddSubjectsBinding

class AddSubjectsDialog(private val context: Context) {

    private lateinit var binding: DialogAddSubjectsBinding
    fun show() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_subjects, null)
        // Initialize view binding
        binding = DialogAddSubjectsBinding.inflate(LayoutInflater.from(context))

        val dialogBuilder = AlertDialog.Builder(context, R.style.DialogBox)
            .setView(dialogView)
        val alertDialog = dialogBuilder.create()



        alertDialog.show()
    }
}