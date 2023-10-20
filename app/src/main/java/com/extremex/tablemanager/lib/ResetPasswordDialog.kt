package com.extremex.tablemanager.lib

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.extremex.tablemanager.R
import com.google.android.material.button.MaterialButton

class ResetPasswordDialog (context: Context){
    init {
        resetPasswordDialog(context)
    }
    @SuppressLint("MissingInflatedId")
    private fun resetPasswordDialog(context: Context,){
        val uiInflater = LayoutInflater.from(context).inflate(R.layout.reset_password, null)
        //context.window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
        val popUpBox = android.app.AlertDialog.Builder(context,R.style.DialogBox).setView(uiInflater).setCancelable(false)

        popUpBox.create()
        val popupMenu = popUpBox.show()
        val resetButton: MaterialButton = uiInflater.findViewById(R.id.ResetButton)
        val email: EditText = uiInflater.findViewById(R.id.EmailText)

        resetButton.setOnClickListener {
            popupMenu.dismiss()
        }
    }
}
