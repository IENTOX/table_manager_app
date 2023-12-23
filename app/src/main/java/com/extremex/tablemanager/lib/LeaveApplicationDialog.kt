package com.extremex.tablemanager.lib

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.extremex.tablemanager.R
import com.extremex.tablemanager.service.DateTimeService
import java.util.Calendar

class LeaveApplicationDialog(private val context: Context) {

    // save leave data
    private lateinit var leavePref : SharedPreferences
    private lateinit var leavePrefEditor : SharedPreferences.Editor

    private lateinit var TODAT_S_DATE :String
    private var selectedLeaveType = "none"

    // Define listener interfaces
    interface ApplyListener {
        fun onApply(
            userName: String,
            userId: String,
            description: String,
            isUncertain: Boolean,
            selectedLeaveType: String,
            leaveApplicationDate: String,
            leaveStartDate: String,
            leaveEndDate: String
        ): String
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

    // Date Time service intent
    private lateinit var serviceIntent: Intent


    private val dateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val date = intent.getStringExtra(DateTimeService.DEVICE_DATE_UPDATED)
            TODAT_S_DATE = date!!
        }
    }
    fun show() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_leave_application, null)

        // initialising DateTime updating Service
        serviceIntent = Intent(context.applicationContext, DateTimeService::class.java)
        context.registerReceiver(dateReceiver, IntentFilter(DateTimeService.DEVICE_DATE_TIME))

        val dialogBuilder = AlertDialog.Builder(context,R.style.DialogBox)
            .setView(dialogView)
        val alertDialog = dialogBuilder.create()

        // Initialize views from the dialog layout
        val userName = dialogView.findViewById<TextView>(R.id.UserName)
        val userId = dialogView.findViewById<TextView>(R.id.UserId)
        val leaveDescription = dialogView.findViewById<EditText>(R.id.UserLeaveDescription)
        val leaveCheckBox = dialogView.findViewById<CheckBox>(R.id.LeaveCheckBox)
        val cancelButton = dialogView.findViewById<Button>(R.id.CancelButton)
        val applyButton = dialogView.findViewById<Button>(R.id.ApplyButton)
        val leaveEndDurationButton = dialogView.findViewById<Button>(R.id.LeaveEnd)
        val leaveStartDurationButton = dialogView.findViewById<Button>(R.id.LeaveStart)
        val leaveEndTitle = dialogView.findViewById<TextView>(R.id.LeaveEndDurationTitle)
        val leaveType = dialogView.findViewById<Spinner>(R.id.LeaveTypeDropDown)

        val leaveTypeArray = context.resources.getStringArray(R.array.LeaveType)

        val adapter = ArrayAdapter(context, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, leaveTypeArray)
        adapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
        leaveType.adapter = adapter


        selectedLeaveType = leaveTypeArray[0]

        leaveType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedLeaveType = leaveTypeArray[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedLeaveType = leaveTypeArray[0]
            }
        }

        leaveCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                leaveEndTitle.alpha = 0.5f
                leaveEndDurationButton.alpha = 0.5f
                leaveEndDurationButton.text ="∞"
                leaveEndDurationButton.isClickable = false
            } else {
                leaveEndTitle.alpha = 1f
                leaveEndDurationButton.alpha = 1f
                leaveEndDurationButton.text =""
                leaveEndDurationButton.isClickable = true
            }
        }
        //applyButton.alpha = 0.5f
        //applyButton.isClickable = false

        // TODO: to be fixed
        /* leaveDescription.setOnEditorActionListener { _, _, _ ->
            if(!leaveDescription.text.toString().isBlank()){
                applyButton.alpha = 1f
                applyButton.isClickable = true
                true
            } else {
                applyButton.alpha = 0.5f
                applyButton.isClickable = false
                false
            }
        }
         */
        var startDateArray: Array<Int>? = null
        var endDateArray: Array<Int>? = null
        val userNameText = userName.text.toString()
        val userIdText = userId.text.toString()
        var descriptionText = leaveDescription.text.toString()
        val isUncertain = leaveCheckBox.isChecked

        leaveStartDurationButton.setOnClickListener {
            showDateSetter() { day, month, year ->
                startDateArray = arrayOf(day,month,year)
                leaveStartDurationButton.text = "$day/$month/$year"
            }
        }
        leaveEndDurationButton.setOnClickListener {
            showDateSetter() { day, month, year ->
                endDateArray = arrayOf(day,month,year)
                leaveEndDurationButton.text = "$day/$month/$year"
            }
        }

        // Set click listeners for cancel and apply buttons
        cancelButton.setOnClickListener {
            alertDialog.dismiss()
            cancelListener?.onCancel()
        }

        applyButton.setOnClickListener {
            if (leaveDescription.text.isNullOrBlank() && leaveStartDurationButton.text.isNullOrBlank() && leaveEndDurationButton.text.isNullOrBlank()){
                leaveDescription.error = "Field cannot be empty"
                leaveStartDurationButton.error = "Field cannot be empty"
                leaveEndDurationButton.error = "Field cannot be empty"
            } else {
                descriptionText = leaveDescription.text.toString()
                applyListener?.onApply(userNameText,userIdText,descriptionText,isUncertain,selectedLeaveType, TODAT_S_DATE, leaveStartDurationButton.text.toString(), leaveEndDurationButton.text.toString())

                saveLeaveData(userNameText, userIdText, descriptionText,isUncertain,selectedLeaveType,leaveStartDurationButton.text.toString(), leaveEndDurationButton.text.toString())
                // Dismiss the dialog
                alertDialog.dismiss()
            }
        }

        userName.text = "John Doe"
        userId.text = "12345"

        alertDialog.show()
    }

    private fun showDateSetter( onDateSet: (day: Int, month: Int, year: Int) -> Unit) {
        // Get current date
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        // Show DatePickerDialog
        val datePicker = DatePickerDialog(context, { _, year, monthOfYear, dayOfMonth ->
            onDateSet(dayOfMonth, monthOfYear + 1, year)
        }, currentYear, currentMonth, currentDay)

        datePicker.show()
    }

    private fun saveLeaveData(userName: String, userId: String, description: String, isUncertain: Boolean, selectedLeaveType: String, leaveStart: String, leaveEnd: String) :Boolean {
        leavePref = context.getSharedPreferences(SerialService.LEAVE_APPLICATION_FILE, AppCompatActivity.MODE_PRIVATE)
        leavePrefEditor = leavePref.edit()
        leavePrefEditor.putString("Name", userName)
        Log.v("leave Data: Name", userName)
        leavePrefEditor.putString("userId", userId)
        Log.v("leave Data: userId", userId)
        leavePrefEditor.putString("Description",description)
        Log.v("leave Data: Description",description)
        leavePrefEditor.putBoolean("isUncertain", isUncertain)
        Log.v("leave Data: isUncertain", isUncertain.toString())
        leavePrefEditor.putString("selected Leave Type",selectedLeaveType)
        Log.v("leave Data: selected Leave Type",selectedLeaveType)
        leavePrefEditor.putString("Leave From Date", leaveStart)
        Log.v("leave Data: Leave From Date", leaveStart)
        leavePrefEditor.putString("Back On Date", leaveEnd)
        Log.v("leave Data: Back On Date", leaveEnd)
        leavePrefEditor.putString("Today_s Date", TODAT_S_DATE)
        Log.v("leave Data: Today_s Date", TODAT_S_DATE)
        Log.v("leave Data:", "leave data stored successfully")
        leavePrefEditor.commit()

        return false
    }
}
