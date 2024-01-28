package com.extremex.tablemanager.lib

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.DialogTimeSlotsBinding

class AddTimeSlotDialog(private val context: Context) {

    private lateinit var binding: DialogTimeSlotsBinding
    fun show() {
        //val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_time_slots, null)
        // Initialize view binding
        binding = DialogTimeSlotsBinding.inflate(LayoutInflater.from(context))

        val dialogBuilder = AlertDialog.Builder(context, R.style.DialogBox)
            .setView(binding.root)
        val alertDialog = dialogBuilder.create()
        val item = context.resources.getStringArray(R.array.NumberByTimes)
        val unitItem = context.resources.getStringArray(R.array.LargeTimeUnits)
        val customSpinnerAdapter = ArrayAdapter<Any?>(context, R.layout.item_simple_spinner_default, item)
        customSpinnerAdapter.setDropDownViewResource(R.layout.item_simple_spinner_default)
        val customUnitSpinnerAdapter = ArrayAdapter<Any?>(context, R.layout.item_simple_spinner_default, unitItem)
        customUnitSpinnerAdapter.setDropDownViewResource(R.layout.item_simple_spinner_default)
        binding.NumberSetterDropDown.adapter = customSpinnerAdapter
        binding.HolidayNumberSetterDropDown.adapter = customSpinnerAdapter
        binding.HolidayUnitSetterDropDown.adapter = customUnitSpinnerAdapter
        val weekdays = context.resources.getStringArray(R.array.WeekDays)

        // Set up the RecyclerView
        val layoutManager = GridLayoutManager(context, 7)
        binding.FixedDaysOffSelector.layoutManager = layoutManager
        binding.FixedDaysOffSelector.adapter = ViewWeekdaysAdapter(context, weekdays)
        alertDialog.show()
    }
}