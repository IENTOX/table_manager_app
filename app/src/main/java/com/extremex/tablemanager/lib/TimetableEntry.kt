package com.extremex.tablemanager.lib

import com.extremex.tablemanager.models.TeacherObject

data class TimetableEntry(val day: String, val timeSlot: String, val teacher: TeacherObject?, val subject: String, val classroom: String)
