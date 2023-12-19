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
import com.extremex.tablemanager.common.fragment.LoginFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ResetPasswordDialog (context: Context){

    interface ResetPassword{
        fun onPasswordSubmitted(email :String)
    }
    private var resetPassword : ResetPassword? = null

    fun onResetPassword(listener: ResetPassword){
        this.resetPassword = listener
    }

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
        val cancelButton: FloatingActionButton = uiInflater.findViewById(R.id.CancelButton)
        val email: EditText = uiInflater.findViewById(R.id.EmailText)

        resetButton.setOnClickListener {
            if (email.text.toString().trim().isNotBlank()) {
                resetPassword?.onPasswordSubmitted(email.text.toString().trim())
            } else {
                email.error = "Field cannot be empty!"
            }
        }

        cancelButton.setOnClickListener {
            popupMenu.dismiss()
        }
    }
}
