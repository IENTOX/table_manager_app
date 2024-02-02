package com.extremex.tablemanager.lib

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.PopupMenu.OnDismissListener
import android.widget.TextView
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.DialogCustomDefalutBinding
import com.extremex.tablemanager.databinding.DialogCustomRemoveConfirmationBinding
import java.lang.IllegalArgumentException

class CustomDialog(private val context: Context, private val dismissListener: CustomDialogDismissListener? = null, private val confirmListener: CustomDialogConfirmListener? = null){

    fun createBasicCustomDialog(dismissButtonText: String, text: String, gravityCentre: Boolean = false, isTitle: Boolean = false, title: String =""){
        return basicPopUpBox(context,dismissButtonText,text,isTitle,title,gravityCentre)
    }

    fun createBasicRemoveDialog(dismissButtonText: String, confirmButtonText: String, text: String, gravityCentre: Boolean = false, isTitle: Boolean = false, title: String =""){
        return basicRemoveItemPopUpBox(context,dismissButtonText,confirmButtonText,text,isTitle,title,gravityCentre)
    }



    private fun basicPopUpBox(
        context: Context,
        dismissButtonText: String,
        text: String,
        isTitle: Boolean = false,
        title: String ="",
        gravityCentre: Boolean = false
    ){
        val binding = DialogCustomDefalutBinding.inflate(LayoutInflater.from(context))
        val popUpBox = android.app.AlertDialog.Builder(context,R.style.DialogBox).setView(binding.root).setCancelable(false)

        popUpBox.create()
        val popupMenu = popUpBox.show()
        if (gravityCentre) {
            binding.DialogBodyText.gravity = Gravity.CENTER
        }

        if (isTitle){
            binding.DialogTitleText.visibility = View.VISIBLE
            binding.DialogTitleText.text = title
        }
        binding.CloseButton.text = dismissButtonText

        binding.DialogBodyText.text = text
        binding.CloseButton.setOnClickListener {
            dismissListener?.onDismiss()
            popupMenu.dismiss()
        }
    }

    private fun basicRemoveItemPopUpBox(
        context: Context,
        dismissButtonText: String,
        confirmButtonText: String,
        text: String,
        isTitle: Boolean = false,
        title: String ="",
        gravityCentre: Boolean = false
    ){
        val binding = DialogCustomRemoveConfirmationBinding.inflate(LayoutInflater.from(context))
        val popUpBox = android.app.AlertDialog.Builder(context,R.style.DialogBox).setView(binding.root).setCancelable(false)
        popUpBox.setCancelable(false)
        popUpBox.create()
        val popupMenu = popUpBox.show()
        if (gravityCentre) {
            binding.DialogText.gravity = Gravity.CENTER
        }

        if (isTitle){
            binding.DialogTitle.visibility = View.VISIBLE
            binding.DialogTitle.text = title
        } else {
            binding.DialogTitle.visibility = View.GONE
        }

        binding.DialogDismissButton.text = dismissButtonText
        binding.DialogConfirmButton.text = confirmButtonText
        binding.DialogText.text = text

        binding.DialogDismissButton.setOnClickListener {
            dismissListener?.onDismiss()
            popUpBox.setCancelable(true)
            popupMenu.dismiss()

        }

        binding.DialogConfirmButton.setOnClickListener {
            confirmListener?.onConfirm()
            popUpBox.setCancelable(true)
            popupMenu.dismiss()
        }
    }
}