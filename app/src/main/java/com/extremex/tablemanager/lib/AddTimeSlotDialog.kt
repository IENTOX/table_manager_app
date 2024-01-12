package com.extremex.tablemanager.lib

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.DialogTimeSlotsBinding

class AddTimeSlotDialog(private val context: Context) {

    private lateinit var binding: DialogTimeSlotsBinding
    fun show() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_time_slots, null)
        // Initialize view binding
        binding = DialogTimeSlotsBinding.inflate(LayoutInflater.from(context))

        val dialogBuilder = AlertDialog.Builder(context, R.style.DialogBox)
            .setView(dialogView)
        val alertDialog = dialogBuilder.create()



        alertDialog.show()
    }
}