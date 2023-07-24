package com.extremex.tablemanager.lib

data class SigninData(
    val name: String,
    val DOB: String,
    val age: Int,
    val module: List<String>,
    val Id : Int,
    val phNum: Int,
    val cCode: Int,
    val gander: String,
    val email: String,
    val passwordHash: String
)
