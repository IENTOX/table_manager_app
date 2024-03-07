package com.extremex.tablemanager.lib

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class LocalStorageData(context: Context) {
    enum class From{
        MANAGE_SUBJECT,
        MANAGE_CLASSROOM,
        MANAGE_TEACHERS,
        MANAGE_HOLIDAYS,
    }
    // initializer
    private val dateTimeFormatter = DateTimeUtil()

    private val holidayPref = context.getSharedPreferences(StandardCompanion.TIME_SLOTS_CUSTOM_HOLIDAY_FILE_MANE, AppCompatActivity.MODE_PRIVATE)
    private val holidayPrefEditor = holidayPref.edit()

    private val subjectPref = context.getSharedPreferences(StandardCompanion.SUBJECT_DATA_FILE_MANE,AppCompatActivity.MODE_PRIVATE)
    private val subjectPrefEditor= subjectPref.edit()

    private val classroomPref = context.getSharedPreferences(StandardCompanion.CLASSROOM_DATA_FILE_MANE,AppCompatActivity.MODE_PRIVATE)
    private val classroomPrefEditor = classroomPref.edit()

    // getters() / setters()
    fun createHoliday(
        holidayName: String,
        startDate: DateModel,
        numberOfHolidays: Int = 0,
        holidayUnit: DurationUnit = DurationUnit.Days,
        endDate: DateModel
    ){
        return createCustomHolidays(holidayName, startDate, numberOfHolidays, holidayUnit, endDate)
    }
    fun createClassroom(classroomName: String, classroomCode: String){
        return createCustomClassroom(classroomName, classroomCode)
    }
    fun createSubject(
        subjectName: String,
        subjectCode: String,
        subjectClassroom: String,
        electiveSubjectName: String?,
        electiveSubjectCode: String?,
        electiveSubjectClassroom: String?,
        subjectCountPerWeek: Int,
        subjectYears: Array<Int>
        ){
        return createCustomSubject(
            subjectName,
            subjectCode,
            subjectClassroom,
            electiveSubjectName,
            electiveSubjectCode,
            electiveSubjectClassroom,
            subjectCountPerWeek,
            subjectYears
        )
    }

    fun get(from: LocalStorageData.From): MutableList<out Any> {
        return when (from){
            From.MANAGE_HOLIDAYS -> getHolidaysList()
            From.MANAGE_SUBJECT -> getSubjectList()
            From.MANAGE_CLASSROOM -> getClassroom()
            else -> {getHolidaysList()}
        }
    }
    fun getSpinnerClassroomItems(): MutableList<String> {
        return classroomListView()
    }
    fun getSpinnerSubjectsItems(): MutableList<String> {
        return subjectListView()
    }



    // functionality
    private fun createCustomHolidays(
        holidayName: String,
        startDate: DateModel,
        numberOfHolidays: Int = 0,
        holidayUnit: DurationUnit = DurationUnit.Days,
        endDate: DateModel
    ){
        val position = (holidayPref.all.keys.size + 1) ?: 0
        val holidayStartDate = "${startDate.day}@${startDate.month}@${startDate.year}"
        val holidayEndDate = "${endDate.day}@${endDate.month}@${endDate.year}"
        val holiday = holidayName+"ӿ"+holidayStartDate+"ӿ"+(numberOfHolidays+1)+"ӿ"+holidayUnit+"ӿ"+holidayEndDate
        holidayPrefEditor.putString(position.toString(), holiday)
        holidayPrefEditor.commit()
    }

    private fun getHolidaysList(): MutableList<HolidayInfoModel>{
        var holidays = mutableListOf<HolidayInfoModel>()
        val holidayObjectSize = (holidayPref.all.keys.size) ?: null
        val value_d = holidayPref.all.values.toMutableList()
        if (holidayObjectSize != null){
            for (data in value_d){
                val raw: MutableList<String> = data.toString().split("ӿ").toMutableList() ?: mutableListOf("Unknown")
                if (raw[0] != "Unknown") {
                    val name = raw[0]
                    val rawStartDate = raw[1]
                    val number = raw[2].toInt()
                    val  unit = raw[3]
                    val rawEndDate = raw[4]
                    val rawStartDateFinal: MutableList<String> = rawStartDate.split("@").toMutableList()
                    val startDay = rawStartDateFinal[0].toInt()
                    val startMonth = rawStartDateFinal[1].toInt()
                    val startYear = rawStartDateFinal[2].toInt()
                    val rawEndDateFinal: MutableList<String> = rawEndDate.split("@").toMutableList()
                    val endDay = rawEndDateFinal[0].toInt()
                    val endMonth = rawEndDateFinal[1].toInt()
                    val endYear = rawEndDateFinal[2].toInt()
                    holidays.add(HolidayInfoModel(name, DateModel(startDay, startMonth, startYear), number, unit, DateModel(endDay, endMonth, endYear)))
                }
            }
        } else {
            holidayPref.getString(1.toString(),"None")
            Log.e("Retrieved Holiday Data 2", "null")
        }
        return holidays
    }

    private fun createCustomSubject(
        subjectName: String,
        subjectCode: String,
        subjectClassroom: String,
        electiveSubjectName: String?,
        electiveSubjectCode: String?,
        electiveSubjectClassroom: String?,
        subjectCountPerWeek: Int,
        subjectYears: Array<Int>,
    ){
        val position = (subjectPref.all.keys.size + 1) ?: 0
        Log.v("Null check for subject", "check resn:${electiveSubjectName} resc:${electiveSubjectCode} recc:${electiveSubjectClassroom } ")
        val selectedYears = "${subjectYears[0]}@${subjectYears[1]}@${subjectYears[2]}"
        val data = subjectName+"ӿ"+subjectCode+"ӿ"+subjectClassroom+"ӿ"+electiveSubjectName+"ӿ"+electiveSubjectCode+"ӿ"+electiveSubjectClassroom+"ӿ"+subjectCountPerWeek+"ӿ"+selectedYears
        Log.v("Null check for subject", "check resn:${electiveSubjectName} resc:${electiveSubjectCode} recc:${electiveSubjectClassroom } ")
        subjectPrefEditor.putString(position.toString(), data)
        subjectPrefEditor.commit()
    }

    private fun getSubjectList(): MutableList<SubjectListModel>{
        var dataList = mutableListOf<SubjectListModel>()
        val subjectObjectSize = (subjectPref.all.keys.size) ?: null
        val value_d = subjectPref.all.values.toMutableList()
        if (subjectObjectSize != null){
            for (data in value_d){
                val raw: MutableList<String> = data.toString().split("ӿ").toMutableList() ?: mutableListOf("Unknown")
                if (raw[0] != "Unknown") {
                    val subjectName = raw[0]
                    val subjectCode = raw[1]
                    val subjectClassroom = raw[2]
                    val subjectCountPerWeek = raw[6]

                    Log.v("Null check for subject", "NULL_t: check resn:${raw[3]} resn:${raw[4]} resn:${raw[5]} ")
                    val electiveSubjectName = if (raw[3] != "" && raw[3] != " " && raw[3] != "null" && raw[3].isNotBlank()) raw[3] else null
                    val electiveSubjectCode =  if (raw[4] != "" && raw[4] != " " && raw[4] != "null" && raw[4].isNotBlank()) raw[4] else null
                    val electiveSubjectClassroom =  if (raw[5] != "" && raw[5] != " " && raw[5] != "null" && raw[5].isNotBlank()) raw[5] else null
                    Log.v("Null check for subject", "NULL_t: check resn:${electiveSubjectName} resc:${electiveSubjectCode} recc:${electiveSubjectClassroom } ")
                    val selectedYears = raw[7]
                    val rawSelectedYears: MutableList<String> = selectedYears.split("@").toMutableList()
                    val startDay = rawSelectedYears[0].toInt()
                    val startMonth = rawSelectedYears[1].toInt()
                    val startYear = rawSelectedYears[2].toInt()

                    dataList.add(
                        SubjectListModel(
                            subjectName,
                            subjectCode,
                            subjectClassroom,
                            electiveSubjectName,
                            electiveSubjectCode,
                            electiveSubjectClassroom,
                            subjectCountPerWeek.toInt(),
                            arrayOf(startDay,startMonth,startYear),
                            dateTimeFormatter.yearDeterminer(arrayOf(startDay,startMonth,startYear))
                        ))
                }
            }
        } else {
            subjectPref.getString(1.toString(),"None")
            Log.e("Retrieved Holiday Data 2", "null")
        }
        return dataList
    }private fun createCustomClassroom(classroomName: String, classroomCode: String){
        val position = (classroomPref.all.keys.size + 1) ?: 0
        val holiday = classroomName+"ӿ"+classroomCode
        classroomPrefEditor.putString(position.toString(), holiday)
        classroomPrefEditor.commit()
    }

    private fun getClassroom(): MutableList<ClassroomModel>{
        var classrooms = mutableListOf<ClassroomModel>()
        val holidayObjectSize = (classroomPref.all.keys.size) ?: null
        val value_d = classroomPref.all.values.toMutableList()
        if (holidayObjectSize != null){
            for (data in value_d){
                val raw: MutableList<String> = data.toString().split("ӿ").toMutableList() ?: mutableListOf("Unknown")
                if (raw[0] != "Unknown") {
                    val classroomName = raw[0]
                    val classroomCode = raw[1]

                    classrooms.add(ClassroomModel(classroomName, classroomCode))
                }
            }
        } else {
            classroomPref.getString(1.toString(),"None")
            Log.e("Retrieved Holiday Data 2", "null")
        }
        return classrooms
    }

    private fun classroomListView(): MutableList<String>{
        val subjects = mutableListOf<String>()
        val holidayObjectSize = (classroomPref.all.keys.size) ?: null
        val value_d = classroomPref.all.values.toMutableList()
        if (holidayObjectSize != null){
            for (data in value_d){
                val raw: MutableList<String> = data.toString().split("ӿ").toMutableList() ?: mutableListOf("Unknown")
                if (raw[0] != "Unknown") {
                    val classroomName = raw[0]
                    val classroomCode = raw[1]
                    subjects.add("$classroomName($classroomCode)")
                }
            }
        } else {
            classroomPref.getString(1.toString(),"None")
            Log.e("Retrieved Holiday Data 2", "null")
        }
        return subjects
    }

    private fun subjectListView(): MutableList<String>{
        val subjects = mutableListOf<String>()
        val holidayObjectSize = (subjectPref.all.keys.size) ?: null
        val value_d = subjectPref.all.values.toMutableList()
        var count = 0
        if (holidayObjectSize != null){
            for (data in value_d){
                val raw: MutableList<String> = data.toString().split("ӿ").toMutableList() ?: mutableListOf("Unknown")
                if (count%2==0) {
                    if (raw[0] != "Unknown") {
                        val subjectName = raw[0]
                        val subjectCode = raw[1]
                        val electiveSubjectName =
                            if (raw[3] != "" && raw[3] != " " && raw[3] != "null" && raw[3].isNotBlank()) raw[3] else null
                        val electiveSubjectCode =
                            if (raw[4] != "" && raw[4] != " " && raw[4] != "null" && raw[4].isNotBlank()) raw[4] else null
                        subjects.add("$subjectName($subjectCode)")
                        if (!electiveSubjectName.isNullOrBlank() && !electiveSubjectCode.isNullOrBlank()){
                            subjects.add("$electiveSubjectName($electiveSubjectCode)")
                        }
                        Log.e("Retrieved Holiday Data 2", "$subjectName($subjectCode)")
                        Log.e("Retrieved Holiday Data 2", "r0: ${subjectName} r1: ${subjectCode} r2: ${null} r3: ${electiveSubjectName} r4: ${electiveSubjectCode} r5: ${null} ")
                        count += 1
                    }
                }
            }
        } else {
            classroomPref.getString(1.toString(),"None")
            Log.e("Retrieved Holiday Data 2", "null")
        }
        return subjects
    }

}