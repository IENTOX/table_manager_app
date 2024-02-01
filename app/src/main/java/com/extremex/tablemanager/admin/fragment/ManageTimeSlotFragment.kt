package com.extremex.tablemanager.admin.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.FragmentManageTimeSlotsViewBinding
import com.extremex.tablemanager.lib.ViewWeekdaysAdapter

class ManageTimeSlotFragment: Fragment() {

    interface TimeSlotFragmentListener{
        fun onBack()
    }
    private lateinit var _binding: FragmentManageTimeSlotsViewBinding
    private val binding get() = _binding
    private var listener: TimeSlotFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TimeSlotFragmentListener){
            listener = context
        } else {
            throw IllegalArgumentException("TimeSlotFragmentListener has to be implemented on root Activity")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageTimeSlotsViewBinding.inflate(LayoutInflater.from(context), container, false)


        val item = requireContext().resources.getStringArray(R.array.NumberByTimes)
        val unitItem = requireContext().resources.getStringArray(R.array.LargeTimeUnits)
        val customSpinnerAdapter = ArrayAdapter<Any?>(requireContext(), R.layout.item_simple_spinner_default, item)
        customSpinnerAdapter.setDropDownViewResource(R.layout.item_simple_spinner_default)
        val customUnitSpinnerAdapter = ArrayAdapter<Any?>(requireContext(), R.layout.item_simple_spinner_default, unitItem)
        customUnitSpinnerAdapter.setDropDownViewResource(R.layout.item_simple_spinner_default)
        binding.NumberSetterDropDown.adapter = customSpinnerAdapter
        binding.HolidayNumberSetterDropDown.adapter = customSpinnerAdapter
        binding.HolidayUnitSetterDropDown.adapter = customUnitSpinnerAdapter
        val weekdays = requireContext().resources.getStringArray(R.array.WeekDays)

        // Set up the RecyclerView
        val layoutManager = GridLayoutManager(requireContext(), 7)
        binding.FixedDaysOffSelector.layoutManager = layoutManager
        binding.FixedDaysOffSelector.adapter = ViewWeekdaysAdapter(requireContext(), weekdays)

        binding.BackButton.setOnClickListener {
            listener?.onBack()
        }

        return binding.root
    }

}