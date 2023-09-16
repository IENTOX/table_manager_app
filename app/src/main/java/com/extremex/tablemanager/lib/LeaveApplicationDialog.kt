package com.extremex.tablemanager.lib

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.extremex.tablemanager.R

class LeaveApplicationDialog(private val context: Context) {

    // Define listener interfaces
    interface ApplyListener {
        fun onApply(userName: String, userId: String, description: String, isUncertain: Boolean)
    }
    interface CancelListener {
        fun onCancel()
    }

    private var applyListener: ApplyListener? = null
    private var cancelListener: CancelListener? = null

    // Set the listeners
    fun setApplyListener(listener: ApplyListener) {
        this.applyListener = listener
    }

    fun setCancelListener(listener: CancelListener) {
        this.cancelListener = listener
    }
    fun show() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_leave_application, null)

        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)

        val alertDialog = dialogBuilder.create()

        // Initialize views from the dialog layout
        val userName = dialogView.findViewById<TextView>(R.id.UserName)
        val userId = dialogView.findViewById<TextView>(R.id.UserId)
        val leaveDescription = dialogView.findViewById<EditText>(R.id.UserLeaveDescription)
        val leaveCheckBox = dialogView.findViewById<CheckBox>(R.id.LeaveCheckBox)
        val cancelButton = dialogView.findViewById<Button>(R.id.CancelButton)
        val applyButton = dialogView.findViewById<Button>(R.id.ApplyButton)

        // Set click listeners for cancel and apply buttons
        cancelButton.setOnClickListener {
            alertDialog.dismiss()
            cancelListener?.onCancel()
        }

        applyButton.setOnClickListener {
            val userNameText = userName.text.toString()
            val userIdText = userId.text.toString()
            val descriptionText = leaveDescription.text.toString()
            val isUncertain = leaveCheckBox.isChecked

            applyListener?.onApply(userNameText,userIdText,descriptionText,isUncertain)

            // Dismiss the dialog
            alertDialog.dismiss()
        }

        userName.text = "John Doe"
        userId.text = "12345"

        alertDialog.show()
    }
}
