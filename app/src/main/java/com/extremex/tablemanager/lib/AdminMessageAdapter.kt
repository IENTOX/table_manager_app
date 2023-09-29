package com.extremex.tablemanager.lib

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.extremex.tablemanager.databinding.ItemNotificationAdminBinding
import com.google.android.material.button.MaterialButton

class AdminMessageAdapter(
    private val messageList: List<AdminMessage>,
    private val onAcceptClick: (AdminMessage) -> Unit,
    private val onHoldClick: (AdminMessage) -> Unit,
    private val onRejectClick: (AdminMessage) -> Unit
) : RecyclerView.Adapter<AdminMessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onAcceptClick, onHoldClick, onRejectClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(messageList[position])
    }

    override fun getItemCount() = messageList.size

    inner class ViewHolder(
        private val binding: ItemNotificationAdminBinding,
        private val onAcceptClick: (AdminMessage) -> Unit,
        private val onHoldClick: (AdminMessage) -> Unit,
        private val onRejectClick: (AdminMessage) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(adminMessage: AdminMessage) {
            binding.apply {
                TeachersName.text = adminMessage.teacherName
                TeachersID.text = adminMessage.teacherID
                Title.text = adminMessage.title
                Date.text = adminMessage.date
                Subject.text = adminMessage.subject
                message.text = adminMessage.message

                AcceptButton.setOnClickListener { onAcceptClick(adminMessage) }
                HoldButton.setOnClickListener { onHoldClick(adminMessage) }
                RejectButton.setOnClickListener { onRejectClick(adminMessage) }
            }
        }
    }
}
