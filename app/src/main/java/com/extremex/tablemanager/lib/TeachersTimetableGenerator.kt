package com.extremex.tablemanager.lib

import com.extremex.tablemanager.data.Gender
import com.extremex.tablemanager.models.TeacherObject
import java.util.*

class TeachersTimetableGenerator(private val teacherNames: List<String>, private val subjectList: List<String>) {

    private val random = Random()

    fun generateTimetableForMonth(year: Int, month: Int, teacherTimetables: Map<String, List<TimetableEntry>>): List<TimetableEntry> {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val timetable = mutableListOf<TimetableEntry>()

        while (calendar.get(Calendar.MONTH) == month) {
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                val day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
                for (timeSlot in timeSlots) {
                    val availableTeachers = teacherNames.map { TeacherObject(it, Gender.OTHER,"","",subjectList,
                        arrayOf(0)
                    ) }
                        .filter { teacher -> isTeacherAvailable(teacher, day, timeSlot, teacherTimetables) }

                    if (availableTeachers.isNotEmpty()) {
                        val teacher = availableTeachers.random()
                        timetable.add(
                            TimetableEntry(
                                day,
                                timeSlot,
                                teacher,
                                teacher.subjects.random(),
                                "Classroom ${day[0]}${timeSlot[0]}"
                            )
                        )
                    } else {
                        val substituteTeacher = findSubstituteTeacher(teacherNames, day, timeSlot)
                        if (substituteTeacher != null) {
                            timetable.add(
                                TimetableEntry(
                                    day,
                                    timeSlot,
                                    substituteTeacher,
                                    substituteTeacher.subjects.random(),
                                    "Classroom ${day[0]}${timeSlot[0]}"
                                )
                            )
                        }
                    }
                }
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return timetable
    }

    private fun isTeacherAvailable(
        teacher: TeacherObject,
        day: String,
        timeSlot: String,
        teacherTimetables: Map<String, List<TimetableEntry>>
    ): Boolean {
        if (day.equals("Sunday", ignoreCase = true)) {
            return false
        }
        val teacherTimetable = teacherTimetables[teacher.name] ?: emptyList()
        return teacherTimetable.none { entry ->
            entry.day.equals(day, ignoreCase = true) && entry.timeSlot == timeSlot
        }
    }

    private fun findSubstituteTeacher(teacherNames: List<String>, day: String, timeSlot: String): TeacherObject? {
        return if (teacherNames.size > 1) {
            val substituteTeacherName = teacherNames.filterNot { it == day }.random()
            TeacherObject("", Gender.OTHER,"","",subjectList, arrayOf(0))
        } else {
            null
        }
    }

    companion object {
        private val timeSlots = listOf("08:00 - 09:00", "09:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00")
    }
}
