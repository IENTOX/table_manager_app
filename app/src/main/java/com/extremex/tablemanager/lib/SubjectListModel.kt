package com.extremex.tablemanager.lib

data class SubjectListModel(
    val subjectName: String,
    val subjectCode: String,
    val subjectClassroomName: String,
    val electiveSubjectName: String?,
    val electiveSubjectCode: String?,
    val electiveSubjectClassroomName: String?,
    val subjectPerWeek: Int,
    val year: Array<Int>,
    val yearView: String
)
