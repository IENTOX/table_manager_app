package com.extremex.tablemanager.admin.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.FragmentManageClassroomViewBinding
import com.extremex.tablemanager.databinding.FragmentManageTimeSlotsViewBinding
import com.extremex.tablemanager.lib.ClassroomModel
import com.extremex.tablemanager.lib.CustomDialog
import com.extremex.tablemanager.lib.CustomDialogDismissListener
import com.extremex.tablemanager.lib.DateModel
import com.extremex.tablemanager.lib.DurationUnit
import com.extremex.tablemanager.lib.HolidayInfoModel
import com.extremex.tablemanager.lib.StandardCompanion
import com.extremex.tablemanager.lib.ViewClassroomAdapter
import com.extremex.tablemanager.lib.ViewHolidayAdapter
import com.extremex.tablemanager.lib.ViewWeekdaysAdapter
import com.google.android.material.slider.Slider
import java.time.LocalTime
import java.util.Calendar
import kotlin.math.roundToInt

class ManageTimeSlotFragment: Fragment() {

    interface TimeSlotFragmentListener{
        fun onBack()
    }
    private lateinit var binding: FragmentManageTimeSlotsViewBinding
    private var listener: TimeSlotFragmentListener? = null
    private lateinit var userPref: SharedPreferences
    private lateinit var holidayPref: SharedPreferences
    private lateinit var userPrefEditor: Editor
    private lateinit var holidayPrefEditor: Editor
    private lateinit var lectureStartTime: Array<Int>
    private lateinit var breakStartTime: Array<Int>
    private lateinit var holidayStartDate: Array<Int>
    private var lectureDuration = 0
    private var breakDuration = 0
    private var lecturePerDay = 0
    private lateinit var datePicker: DatePickerDialog
    private var handler =  Handler(Looper.getMainLooper())
    //private lateinit var numberOfHolidays: Int

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
    ): View { return inflater.inflate(R.layout.fragment_manage_time_slots_view, container, false) }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentManageTimeSlotsViewBinding.bind(view)

        userPref = requireContext().getSharedPreferences(StandardCompanion.USER_PREF_FILE_MANE, AppCompatActivity.MODE_PRIVATE)
        userPrefEditor = userPref.edit()
        holidayPref = requireContext().getSharedPreferences(StandardCompanion.TIME_SLOTS_CUSTOM_HOLIDAY_FILE, AppCompatActivity.MODE_PRIVATE)
        holidayPrefEditor = holidayPref.edit()
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
        if (getCustomHolidays().isEmpty()){
            binding.HolidayViewPlaceHolder.visibility = View.VISIBLE
            binding.CustomHolidayList.visibility = View.GONE
        } else {
            binding.HolidayViewPlaceHolder.visibility = View.GONE
            binding.CustomHolidayList.visibility = View.VISIBLE
            addToListView(binding)
        }

        // Set up the RecyclerView
        val layoutManager = GridLayoutManager(requireContext(), 7)
        binding.FixedDaysOffSelector.layoutManager = layoutManager
        binding.FixedDaysOffSelector.adapter = ViewWeekdaysAdapter(requireContext(), weekdays)

        binding.LectureStartTimeSetter.text = userPref.getString(StandardCompanion.TIME_SLOTS_LECTURE_START_TIME,"")?:""
        Log.v("Reciever LECTURE TIME",userPref.getString(StandardCompanion.TIME_SLOTS_LECTURE_START_TIME,"")?:"")
        binding.LectureStartTimeSetter.setOnClickListener {
            showTimePickerDialog {
                lectureStartTime = arrayOf(it.hour,it.minute,it.second)
                val timeAnnotation = arrayOf("AM","PM")
                val defAnnotation = if (it.hour < 12){ timeAnnotation[0] } else { timeAnnotation[1] }
                userPrefEditor.putString(StandardCompanion.TIME_SLOTS_LECTURE_START_TIME,"${it.hour}:${it.minute}:$defAnnotation")
                userPrefEditor.commit()
                Log.v("STORED LECTURE TIME","${it.hour}:${it.minute} $defAnnotation")
                binding.LectureStartTimeSetter.text = "${it.hour}:${it.minute} $defAnnotation"
            }
        }

        binding.BreakStartTimeSetter.text = userPref.getString(StandardCompanion.TIME_SLOTS_BREAK_START_TIME,"")?:""
        Log.v("Reciever BREAK TIME",userPref.getString(StandardCompanion.TIME_SLOTS_BREAK_START_TIME,"")?:"")
        binding.BreakStartTimeSetter.setOnClickListener {
            showTimePickerDialog {
                breakStartTime = arrayOf(it.hour,it.minute,it.second)
                val timeAnnotation = arrayOf("AM","PM")
                val defAnnotation = if (it.hour < 12){ timeAnnotation[0] } else { timeAnnotation[1] }
                userPrefEditor.putString(StandardCompanion.TIME_SLOTS_BREAK_START_TIME,"${it.hour}:${it.minute}:$defAnnotation")
                userPrefEditor.commit()
                Log.v("STORED BREAK TIME","${it.hour}:${it.minute} $defAnnotation")
                binding.BreakStartTimeSetter.text = "${it.hour}:${it.minute} $defAnnotation"
            }
        }
        binding.CustomHolidayDateSetter.setOnClickListener {
            showDateSetter()
        }

        binding.SemesterSizeSlider.value = setTimeSlotSlider()
        val fromStorage = userPref.getFloat(StandardCompanion.TIME_SLOTS_SEMESTER_SIZE_WEEKS,1.0f)
        binding.SemesterSizeView.text = if (fromStorage.roundToInt() < 10){ "0${fromStorage.roundToInt()}" } else { fromStorage.roundToInt().toString() }
        binding.SemesterSizeSlider.addOnChangeListener { slider, value, fromUser ->
            if (fromUser){
                val semValue = if (value.roundToInt() < 10){ "0${value.roundToInt()}" } else { value.roundToInt().toString() }
                storeTimeSlotData(value)
                handler.postDelayed({setTimeSlotSlider(slider)},500)
                binding.SemesterSizeView.text = semValue
            }
        }

        binding.BackButton.setOnClickListener {
            listener?.onBack()
        }

        lecturePerDay = userPref.getInt(StandardCompanion.TIME_SLOTS_LECTURE_PER_DAY,0)
        binding.NumberSetterDropDown.setSelection(lecturePerDay)
        binding.NumberSetterDropDown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                userPrefEditor.putInt(StandardCompanion.TIME_SLOTS_LECTURE_PER_DAY, position)
                userPrefEditor.commit()
                Log.v("STORED LECTURE PER DAY",binding.NumberSetterDropDown.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.NumberSetterDropDown.setSelection(lecturePerDay)
            }

        }

        lectureDuration = userPref.getInt(StandardCompanion.TIME_SLOTS_LECTURE_DURATION_TIME,0)
        binding.LectureDurationSetter.setSelection(lectureDuration)
        binding.LectureDurationSetter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                userPrefEditor.putInt(StandardCompanion.TIME_SLOTS_LECTURE_DURATION_TIME, position)
                userPrefEditor.commit()
                Log.v("STORED LECTURE DURATION",binding.LectureDurationSetter.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.LectureDurationSetter.setSelection(lectureDuration)
            }

        }

        breakDuration = userPref.getInt(StandardCompanion.TIME_SLOTS_BREAK_DURATION_TIME,0)
        binding.BreakDurationSetter.setSelection(breakDuration)
        binding.BreakDurationSetter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                userPrefEditor.putInt(StandardCompanion.TIME_SLOTS_BREAK_DURATION_TIME, position)
                userPrefEditor.commit()
                Log.v("STORED BREAK DURATION",binding.BreakDurationSetter.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.BreakDurationSetter.setSelection(breakDuration)
            }

        }

        binding.AddButton.setOnClickListener {
            if (::holidayStartDate.isInitialized) {
                val name = binding.CustomHolidayNameText.text.toString().trim()
                val date = holidayStartDate
                val number = binding.HolidayNumberSetterDropDown.selectedItemPosition
                val selectedUnit = binding.HolidayUnitSetterDropDown.selectedItemPosition
                val unit = when (selectedUnit) {
                    0 -> DurationUnit.Days.name
                    1 -> DurationUnit.Weeks.name
                    2 -> DurationUnit.Months.name
                    3 -> DurationUnit.Years.name
                    else -> DurationUnit.Days.name
                }
                if (date.size > 2 || date[0].toInt() != 0 || date[1].toInt() != 0 || date[2].toInt() != 0) {
                    createCustomHolidays(
                        name,
                        DateModel(date[0], date[1], date[2]),
                        number,
                        unit
                    )
                    val popup = CustomDialog(requireContext(), object : CustomDialogDismissListener{
                        override fun onDismiss() {
                            binding.CustomHolidayNameText.text?.clear()
                            binding.CustomHolidayDateSetter.text = ""
                            binding.HolidayNumberSetterDropDown.setSelection(0)
                            binding.HolidayUnitSetterDropDown.setSelection(0)
                            addToListView(binding)
                        }

                    }, null)
                    popup.createBasicCustomDialog(
                        "Done",
                        "Holiday have been added successfully",
                        true,
                        true,
                        "Holiday Added Successfully"
                    )
                } else {
                    val popup = CustomDialog(requireContext(), null, null)
                    popup.createBasicCustomDialog(
                        "Close",
                        "Adding a holiday start date is required",
                        true,
                        true,
                        "Missing Fields"
                    )
                }
            } else {
                val popup = CustomDialog(requireContext(), null, null)
                popup.createBasicCustomDialog(
                    "Close",
                    "Adding a holiday start date is requires",
                    true,
                    true,
                    "Missing Required Fields"
                )
            }
        }


    }

    private fun addToListView(binding: FragmentManageTimeSlotsViewBinding){
        if (getCustomHolidays().isEmpty()){
            binding.HolidayViewPlaceHolder.visibility = View.VISIBLE
            binding.CustomHolidayList.visibility = View.GONE
        } else {
            binding.HolidayViewPlaceHolder.visibility = View.GONE
            binding.CustomHolidayList.visibility = View.VISIBLE
        }
        val holidayAdapter = ViewHolidayAdapter(requireContext(), getCustomHolidays())
        holidayAdapter.attachItemTouchHelper(binding.CustomHolidayList)
        holidayAdapter.attachItemTouchHelper(binding.CustomHolidayList)
        binding.CustomHolidayList.adapter = holidayAdapter
        binding.CustomHolidayList.layoutManager = LinearLayoutManager(requireContext())
        // live updates the list
        binding.CustomHolidayList.adapter.let {
            it?.notifyItemRangeInserted(0, getCustomHolidays().size)
        }
    }


    private fun showTimePickerDialog(onTimeSetListener: (LocalTime) -> Unit) {
        val currentTime = LocalTime.now()
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                val selectedTime = LocalTime.of(hourOfDay, minute)
                onTimeSetListener(selectedTime)
            },
            currentTime.hour,
            currentTime.minute,
            DateFormat.is24HourFormat(requireContext())
        )
        timePickerDialog.show()
    }

    private fun storeTimeSlotData(semValue: Float){
        userPrefEditor.putFloat(StandardCompanion.TIME_SLOTS_SEMESTER_SIZE_WEEKS,semValue)
        userPrefEditor.commit()
        Log.v(StandardCompanion.TIME_SLOTS_SEMESTER_SIZE_WEEKS, semValue.toString())
    }
    private fun setTimeSlotSlider(slider: Slider? = null): Float {
        val defaultSlider = userPref.getFloat(StandardCompanion.TIME_SLOTS_SEMESTER_SIZE_WEEKS,1.0f)?:1.0f
        slider?.value = defaultSlider
        Log.v(StandardCompanion.TIME_SLOTS_SEMESTER_SIZE_WEEKS, defaultSlider.toString())
        return defaultSlider
    }

    private fun getTimeStringFromDouble(time: Double): String {
        val result :Int = time.roundToInt()
        val hours :Int = result % 86400 / 3600
        val minutes :Int = result % 86400 % 3600 / 60
        val seconds :Int = result % 86400 % 3600 % 60
        return if (minutes == 0){
            String.format("%02ds", seconds)
        } else if ( hours == 0){
            String.format("%02dm %02ds", minutes, seconds)
        } else {
            String.format("%02dh %02dm %02ds",hours, minutes, seconds)
        }
    }

    private fun showDateSetter() {
        if (!::datePicker.isInitialized) {
            val calendar = Calendar.getInstance()
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentYear = calendar.get(Calendar.YEAR)

            datePicker = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
                holidayStartDate = arrayOf(dayOfMonth, monthOfYear + 1, year)
                binding.CustomHolidayDateSetter.text = "$dayOfMonth/${monthOfYear + 1}/$year"
            }, currentYear, currentMonth, currentDay)
        }

        datePicker.show()
    }

    private fun createCustomHolidays(
        holidayName: String,
        date: DateModel,
        numberOfHolidays: Int = 0,
        holidayUnit: String = DurationUnit.Days.name
    ){
        val position = (holidayPref.all.keys.size + 1) ?: 0
        val holidayDate = "${date.day}@${date.month}@${date.year}"
        val holiday = holidayName+"ӿ"+holidayDate+"ӿ"+(numberOfHolidays+1)+"ӿ"+holidayUnit
        holidayPrefEditor.putString(position.toString(), holiday)
        holidayPrefEditor.commit()
    }

    private fun getCustomHolidays(): MutableList<HolidayInfoModel>{
        var holidays = mutableListOf<HolidayInfoModel>()
        val position = (holidayPref.all.keys.size -1) ?: 0
        if (position > 0){
            for (i in 1.. position){
                try {
                    val raw: MutableList<String> = holidayPref.getString(i.toString(),"None")?.split("ӿ")?.toMutableList() ?: mutableListOf("Unknown")
                    if (raw[0] != "Unknown") {
                        val name = raw[0]
                        val rawDate = raw[1]
                        val number = raw[2].toInt()
                        val  unit = raw[3]
                        val rawDateDate: MutableList<String> = rawDate.split("@").toMutableList()
                        val day = rawDateDate[0].toInt()
                        val month = rawDateDate[1].toInt()
                        val year = rawDateDate[2].toInt()

                        holidays.add(HolidayInfoModel(name, DateModel(day, month, year), number, unit))
                    }
                } catch ( e: NumberFormatException){
                    // nothing to do for now
                }
            }
        } else {
            holidayPref.getString(1.toString(),"None")
        }

        return holidays
    }
}
//"ӿ"