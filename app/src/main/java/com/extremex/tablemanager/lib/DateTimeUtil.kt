package com.extremex.tablemanager.lib

import kotlin.math.roundToInt

class DateTimeUtil {
    fun yearDeterminer(year: Array<Int>): String {
        return when {
            year.contentEquals(arrayOf(1, 0, 0)) -> "First year"
            year.contentEquals(arrayOf(0, 1, 0)) -> "Second year"
            year.contentEquals(arrayOf(0, 0, 1)) -> "Third year"
            year.contentEquals(arrayOf(1, 1, 0)) -> "First and Second year"
            year.contentEquals(arrayOf(0, 1, 1)) -> "Second and Third year"
            year.contentEquals(arrayOf(1, 0, 1)) -> "First and Third year"
            year.contentEquals(arrayOf(1, 1, 1)) -> "First, Second, and Third year"
            else -> "Year not determined"
        }
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

}