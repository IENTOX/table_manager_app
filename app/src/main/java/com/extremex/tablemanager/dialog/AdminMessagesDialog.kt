package com.extremex.tablemanager.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.DialogAdminMessagesBinding
import com.extremex.tablemanager.lib.AdminMessage
import com.extremex.tablemanager.adapter.AdminMessageAdapter
import java.time.LocalDate

class AdminMessagesDialog(private val context: Context) {

    // Define listener interfaces
    interface AcceptListener {
        fun onAccept(message: AdminMessage)
    }

    interface HoldListener {
        fun onHold(message: AdminMessage)
    }

    interface RejectListener {
        fun onReject(message: AdminMessage)
    }

    interface CloseListener {
        fun onClose()
    }

    private var acceptListener: AcceptListener? = null
    private var holdListener: HoldListener? = null
    private var rejectListener: RejectListener? = null
    private var closeListener: CloseListener? = null

    fun setAcceptListener(listener: AcceptListener) {
        this.acceptListener = listener
    }

    fun setHoldListener(listener: HoldListener) {
        this.holdListener = listener
    }

    fun setRejectListener(listener: RejectListener) {
        this.rejectListener = listener
    }

    fun setCloseListener(listener: CloseListener) {
        this.closeListener = listener
    }

    fun show() {
        val binding = DialogAdminMessagesBinding.inflate(LayoutInflater.from(context))
        val dialogBuilder = AlertDialog.Builder(context, R.style.DialogBox).setView(binding.root)
        val alertDialog = dialogBuilder.create()

        // Generate the list of messages
        val messageList = generateMessageList()



        val adapter = AdminMessageAdapter(
            messageList,
            onAcceptClick = { acceptListener?.onAccept(it) },
            onHoldClick = { holdListener?.onHold(it) },
            onRejectClick = { rejectListener?.onReject(it) }
        )

        binding.NotificationView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context)
        }

        // Set click listeners for the close button
        binding.CloseButton.setOnClickListener {
            alertDialog.dismiss()
            closeListener?.onClose()
        }

        alertDialog.show()
    }

    private fun generateMessageList() = listOf(
        AdminMessage(
            "Rudi Fernandes",
            "007",
            "time pass",
            "${LocalDate.now().dayOfMonth} ${(LocalDate.now().month).toString().lowercase()} ${LocalDate.now().year}",
            "time pass holidays",
            "i want holidays on 12 of december"
        ),
        AdminMessage(
            "admin Fernandes",
            "007",
            "time pass",
            "${LocalDate.now().dayOfMonth} ${(LocalDate.now().month).toString().lowercase()} ${LocalDate.now().year}",
            "time pass holidays",
            "i want holidays on 15 of november"
        )
    )
}
