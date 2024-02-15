package com.extremex.tablemanager.lib

class StandardCompanion {
    companion object{
        // store Data File Name
        const val USER_PREF_FILE_MANE: String = "user-prefetch"
        const val APP_PREF_FILE_MANE: String = "app-prefetch"
        const val TIME_SLOTS_CUSTOM_HOLIDAY_FILE_MANE: String = "holiday-prefetch"
        const val CLASSROOM_DATA_FILE_MANE: String = "classroom-prefetch"

        // add Time Slots
        const val TIME_SLOTS_LECTURE_START_TIME: String = "LectureStartTime"
        const val TIME_SLOTS_BREAK_START_TIME: String = "BreakStartTime"
        const val TIME_SLOTS_LECTURE_DURATION_TIME: String = "LectureDurationTime"
        const val TIME_SLOTS_BREAK_DURATION_TIME: String = "BreakDurationTime"
        const val TIME_SLOTS_LECTURE_PER_DAY: String = "LecturePerDay"
        const val TIME_SLOTS_SEMESTER_SIZE_WEEKS: String = "SemesterSizeInWeeks"
        const val TIME_SLOTS_FIXED_DAYS_OFF: String = "FixedDaysOff"

        //add Classroom
        const val CLASSROOM_DATA_ROOM_CODE: String = "classroomCode"
        const val CLASSROOM_DATA_ROOM_NAME: String = "classroomName"
    }
}