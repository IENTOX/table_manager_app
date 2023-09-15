package com.extremex.tablemanager.lib

import android.content.Context
import android.widget.ProgressBar
import android.widget.TextView
import com.extremex.tablemanager.R

class LeaveProgressController(context: Context,progressBar: ProgressBar, progressStatus: String, statusView: TextView) {
    init {
        checkStatus(context, progressStatus, progressBar,statusView)
    }
    private fun checkStatus(context: Context,status: String, progressBar: ProgressBar, statusView: TextView){
        progressBar.max = 150
        progressBar.min = 0
        if (status == LeaveProgressStatus.regected.name) {
            statusView.text = LeaveProgressStatus.regected.name
            statusView.setTextColor(context.resources.getColor(R.color.md_theme_light_error))
        } else {
            statusView.text = LeaveProgressStatus.approved.name
        }
        when (status) {
            LeaveProgressStatus.requested.name -> {
                progressBar.progress = 25
            }
            LeaveProgressStatus.awaiting.name -> {
                progressBar.progress = 75
            }
            LeaveProgressStatus.approved.name -> {
                progressBar.progress = 125
            }
            LeaveProgressStatus.regected.name -> {
                progressBar.progress = 125
            }
            else -> {
                progressBar.progress = progressBar.max
            }
        }
    }


}