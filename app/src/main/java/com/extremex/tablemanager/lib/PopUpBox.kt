package com.extremex.tablemanager.lib

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.extremex.tablemanager.R

class PopUpBox (context: Context,dismissButtonText: String, text: String, gravityCentre: Boolean = false, isTitle: Boolean = false, title: String =""){
    init {
        popupBox(context,dismissButtonText,text,isTitle,title,gravityCentre)
    }
    private fun popupBox(context: Context,dismissButtonText: String, text: String, isTitle: Boolean = false, title: String ="", gravityCentre: Boolean = false){
        val uiInflater = LayoutInflater.from(context).inflate(R.layout.t_c_layout, null)
        //context.window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
        val popUpBox = android.app.AlertDialog.Builder(context,R.style.DialogBox).setView(uiInflater).setCancelable(false)

        popUpBox.create()
        val popupMenu = popUpBox.show()
        val dismissButton: Button = uiInflater.findViewById(R.id.CloseButton)
        val textView: TextView = uiInflater.findViewById(R.id.TnCView)
        val textHead: TextView = uiInflater.findViewById(R.id.Headertnc)
        if (gravityCentre) {
            textView.gravity = Gravity.CENTER
        }
        //textView.setTextAppearance(R.style.TextAppearance_Material3_BodyLarge)

        if (isTitle){
            textHead.visibility = View.VISIBLE
            textHead.text = title
        }
        dismissButton.text = dismissButtonText

        textView.text = text
        dismissButton.setOnClickListener {
            popupMenu.dismiss()
        }
    }
}