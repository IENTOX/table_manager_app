package com.extremex.tablemanager.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.FragmentHomeBinding
import com.extremex.tablemanager.lib.LeaveApplicationDialog
import com.extremex.tablemanager.lib.LeaveProgressController
import com.extremex.tablemanager.lib.LeaveProgressStatus
import com.extremex.tablemanager.lib.SerialService
import com.extremex.tablemanager.service.DateTimeService

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    // save leave data
    private lateinit var leavePref : SharedPreferences
    private lateinit var leavePrefEditor : SharedPreferences.Editor

    // Date Time service intent
    private lateinit var serviceIntent: Intent


    private val dateTimeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val time = intent.getStringExtra(DateTimeService.DEVICE_TIME_UPDATED)
            val date = intent.getStringExtra(DateTimeService.DEVICE_DATE_UPDATED)

            binding.DateView.text = date
            binding.TimeView.text = time
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // initialising DateTime updating Service
        serviceIntent = Intent(requireActivity().applicationContext, DateTimeService::class.java)
        this.requireActivity().registerReceiver(dateTimeReceiver, IntentFilter(DateTimeService.DEVICE_DATE_TIME))
        requireActivity().startService(serviceIntent)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // init leave prefs
        leavePref = requireContext().getSharedPreferences(SerialService.LEAVE_APPLICATION_FILE, AppCompatActivity.MODE_PRIVATE)
        leavePrefEditor = leavePref.edit()

        binding.LeaveApplicationButton.setOnClickListener {
            createLeaveApplication(LeaveProgressStatus.requested.name)
        }
    }

    private fun createLeaveApplication(applicationStatus: String?, reason: String="") {
        val leaveApplicationDialog = LeaveApplicationDialog(requireContext())

        leaveApplicationDialog.setApplyListener(object : LeaveApplicationDialog.ApplyListener {
            override fun onApply(
                userName: String,
                userId: String,
                description: String,
                isUncertain: Boolean,
                selectedLeaveType: String
            ): String {
                    binding.LeaveApplicationProgressView.visibility = View.VISIBLE
                when (applicationStatus) {
                    LeaveProgressStatus.requested.name -> {
                        statusType("Leave Application", description, applicationStatus, "Your request has been Submitted")
                    }
                    LeaveProgressStatus.awaiting.name -> {
                        statusType("Leave Application", description, applicationStatus, "Your request for leave is on hold, it might a while due to $reason.")
                    }
                    LeaveProgressStatus.approved.name -> {
                        statusType("Leave Application", description, applicationStatus, "Your leave application have been approved ")
                    }
                    LeaveProgressStatus.regected.name -> {
                        statusType("Leave Application", description, applicationStatus, "Sorry to inform you that your leave application has been rejected due to $reason.")
                    }
                    else -> {
                        Log.e("leave Data:", "leave Application submitted")
                        statusType("Leave Application", description, applicationStatus, "Undefined")
                    }
                }
                binding.TimeTableShortView.text = selectedLeaveType
                return ""
            }
        })

        leaveApplicationDialog.setCancelListener(object : LeaveApplicationDialog.CancelListener {
            override fun onCancel() {
                binding.LeaveApplicationProgressView.visibility = View.GONE
            }
        })
        leaveApplicationDialog.show()
    }

    private fun statusType(title: String, description: String, applicationStatus: String?, status: String?){
        binding.StatusTitle.text = title
        binding.UserDescription.text = description
        binding.StatusDescription.text = status
        LeaveProgressController(requireContext(), binding.LeaveProgress, applicationStatus!!, binding.LeaveStatus)
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().stopService(serviceIntent)
    }
}