package com.extremex.tablemanager.teacher.fragment

import android.content.IntentFilter
import android.content.SharedPreferences
import com.extremex.tablemanager.lib.TeachersTimetableGenerator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.extremex.tablemanager.databinding.FragmentTimetableBinding
import com.extremex.tablemanager.lib.Teacher
import com.extremex.tablemanager.lib.TimetableEntry
import com.extremex.tablemanager.lib.TimetableAdapter // Correct import statement for TimetableAdapter
import java.util.Calendar

class TimetableFragment : Fragment() {

    private var _binding: FragmentTimetableBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimetableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = requireContext().getSharedPreferences("FRAGMENT_TRANSACTIONS", AppCompatActivity.MODE_PRIVATE)
        init()

        val year = 2023 // Replace with the desired year
        val month = Calendar.AUGUST // Replace with the desired month

        // Assuming you have the teacher names and subject list as provided data
        val teacherNames = listOf("Teacher1", "Teacher2", "Teacher3", "Teacher4")
        val subjectList = listOf("Math", "Physics", "English", "Chemistry")

        // Create a com.extremex.tablemanager.lib.TeachersTimetableGenerator instance
        val timetableGenerator = TeachersTimetableGenerator(teacherNames, subjectList)

        // Assuming you have the teacher timetables as a list of com.extremex.tablemanager.lib.TimetableEntry objects for each teacher
        val timetableEntriesListTeacher1 = listOf(
            TimetableEntry("Monday", "08:00 - 09:00", Teacher("Teacher1", listOf("Math")), "Math", "Classroom M1"),
            TimetableEntry("Monday", "09:00 - 10:00", Teacher("Teacher1", listOf("Math")), "Physics", "Classroom P2"),
            TimetableEntry("Wednesday", "08:00 - 09:00", Teacher("Teacher1", listOf("Math")), "English", "Classroom E5")
            // Add more timetable entries for Teacher1
        )

        val timetableEntriesListTeacher2 = listOf(
            // Timetable entries for Teacher2
            TimetableEntry("Monday", "08:00 - 09:00", Teacher("Teacher2", listOf("english")), "Math", "Classroom M1"),
            TimetableEntry("Monday", "09:00 - 10:00", Teacher("Teacher2", listOf("english")), "Physics", "Classroom P2"),
            TimetableEntry("Monday", "09:00 - 10:00", Teacher("Teacher2", listOf("english")), "Physics", "Classroom P2"),
            TimetableEntry("Monday", "09:00 - 10:00", Teacher("Teacher2", listOf("english")), "Physics", "Classroom P2"),
            TimetableEntry("Monday", "09:00 - 10:00", Teacher("Teacher2", listOf("english")), "Physics", "Classroom P2"),
            TimetableEntry("Monday", "09:00 - 10:00", Teacher("Teacher2", listOf("english")), "Physics", "Classroom P2"),
            TimetableEntry("Monday", "09:00 - 10:00", Teacher("Teacher2", listOf("english")), "Physics", "Classroom P2"),
            TimetableEntry("Monday", "09:00 - 10:00", Teacher("Teacher2", listOf("english")), "Physics", "Classroom P2"),
            TimetableEntry("Wednesday", "08:00 - 09:00", Teacher("Teacher2", listOf("english")), "English", "Classroom E5")
        )

        // Create a map with teacher names as keys and their respective lists of timetable entries as values
        val teacherTimetables = mapOf(
            "Teacher1" to timetableEntriesListTeacher1,
            "Teacher2" to timetableEntriesListTeacher2,
            // Add more entries for other teachers
        )

        val timetable = timetableGenerator.generateTimetableForMonth(year, month, teacherTimetables)

        // Display the generated timetable using RecyclerView and an adapter
        val adapter = TimetableAdapter(timetable) // Use the correct import statement for TimetableAdapter
        binding.timetableRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.timetableRecyclerView.adapter = adapter
    }
    private fun init(){
        val prefEditor = prefs.edit()
        prefEditor.putInt("LastFragmentMain", 1)
        prefEditor.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
