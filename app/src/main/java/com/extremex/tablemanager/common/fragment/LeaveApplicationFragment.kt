package com.extremex.tablemanager.common.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.FragmentLeaveApplicationBinding
import com.extremex.tablemanager.lib.SerialService
import java.util.Calendar

class LeaveApplicationFragment : Fragment(){

    interface LeaveApplicationListener{
        fun onBack()
        fun onApply(
            userName: String,
            userId: String,
            description: String,
            isUncertain: Boolean,
            selectedLeaveType: String,
            leaveApplicationDate: String,
            leaveStartDate: String,
            leaveEndDate: String
        )
    }
    private lateinit var _binding: FragmentLeaveApplicationBinding
    private val binding get() = _binding
    private var listener: LeaveApplicationListener? = null
    // save leave data
    private lateinit var leavePref : SharedPreferences
    private lateinit var leavePrefEditor : SharedPreferences.Editor

    private lateinit var TODAY_DATE :String
    private var selectedLeaveType = "none"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LeaveApplicationListener) {
            listener = context
        } else {
            throw IllegalArgumentException("LeaveApplicationListener is not implemented to root activity")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLeaveApplicationBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val leaveTypeArray = resources.getStringArray(R.array.LeaveType)

        val adapter = ArrayAdapter(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, leaveTypeArray)
        adapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
        binding.LeaveTypeDropDown.adapter = adapter


        selectedLeaveType = leaveTypeArray[0]

        binding.LeaveTypeDropDown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedLeaveType = leaveTypeArray[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedLeaveType = leaveTypeArray[0]
            }
        }

        binding.LeaveCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                binding.LeaveEndDurationTitle.alpha = 0.5f
                binding.LeaveEnd.alpha = 0.5f
                binding.LeaveEnd.text ="∞"
                binding.LeaveEnd.isClickable = false
            } else {
                binding.LeaveEndDurationTitle.alpha = 1f
                binding.LeaveEnd.alpha = 1f
                binding.LeaveEnd.text =""
                binding.LeaveEnd.isClickable = true
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
        val userNameText = binding.UserName.text.toString()
        val userIdText = binding.UserId.text.toString()
        var descriptionText = binding.UserLeaveDescription.text.toString()
        val isUncertain = binding.LeaveCheckBox.isChecked

        binding.LeaveStart.setOnClickListener {
            showDateSetter() { day, month, year ->
                startDateArray = arrayOf(day,month,year)
                binding.LeaveStart.text = "$day/$month/$year"
            }
        }
        binding.LeaveEnd.setOnClickListener {
            showDateSetter() { day, month, year ->
                endDateArray = arrayOf(day,month,year)
                binding.LeaveEnd.text = "$day/$month/$year"
            }
        }

        // Set click listeners for cancel and apply buttons
        binding.BackButton.setOnClickListener {
            listener?.onBack()
        }

        binding.ApplyButton.setOnClickListener {
            if (binding.UserLeaveDescription.text.isNullOrBlank() && binding.LeaveStart.text.isNullOrBlank() && binding.LeaveStart.text.isNullOrBlank()){
                binding.UserLeaveDescription.error = "Field cannot be empty"
                binding.LeaveStart.error = "Field cannot be empty"
                binding.LeaveEnd.error = "Field cannot be empty"
            } else {
                descriptionText = binding.UserLeaveDescription.text.toString()
                listener?.onApply(userNameText,userIdText,descriptionText,isUncertain,selectedLeaveType, TODAY_DATE, binding.LeaveStart.text.toString(), binding.LeaveEnd.text.toString())

                saveLeaveData(userNameText, userIdText, descriptionText,isUncertain,selectedLeaveType,binding.LeaveStart.text.toString(), binding.LeaveEnd.text.toString())
            }
        }

        binding.UserName.text = "John Doe"
        binding.UserId.text = "12345"
    }

    private fun showDateSetter( onDateSet: (day: Int, month: Int, year: Int) -> Unit) {
        // Get current date
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        // Show DatePickerDialog
        val datePicker = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
            onDateSet(dayOfMonth, monthOfYear + 1, year)
        }, currentYear, currentMonth, currentDay)

        datePicker.show()
    }

    private fun saveLeaveData(userName: String, userId: String, description: String, isUncertain: Boolean, selectedLeaveType: String, leaveStart: String, leaveEnd: String) :Boolean {
        leavePref = requireContext().getSharedPreferences(SerialService.LEAVE_APPLICATION_FILE, AppCompatActivity.MODE_PRIVATE)
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
        leavePrefEditor.putString("Today_s Date", TODAY_DATE)
        Log.v("leave Data: Today_s Date", TODAY_DATE)
        Log.v("leave Data:", "leave data stored successfully")
        leavePrefEditor.commit()

        return false
    }
}