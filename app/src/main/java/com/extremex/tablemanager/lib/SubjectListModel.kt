package com.extremex.tablemanager.lib

data class SubjectListModel(
    val subjectName: String,
    val subjectCode: String,
    val year: String,
    val cName: String,
    val electiveSubjectName: String? ="",
    val electiveSubjectCode: String?="",
    val ecName: String =""
)
