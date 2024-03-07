package com.extremex.tablemanager.models

import com.extremex.tablemanager.data.Gender

data class TeacherObject(
    val uniqueID: String,
    val gander: Gender,
    val name: String,
    val mainSubject: String,
    val subjects: List<String>,
    val eligibleYear: Array<Int>
) {
    // DO NOT MODIFY ANY CODE BELOW THIS POINT UNLESS NECESSARY...
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TeacherObject

        if (uniqueID != other.uniqueID) return false
        if (gander != other.gander) return false
        if (name != other.name) return false
        if (mainSubject != other.mainSubject) return false
        if (subjects != other.subjects) return false
        return eligibleYear.contentEquals(other.eligibleYear)
    }

    override fun hashCode(): Int {
        var result = uniqueID.hashCode()
        result = 31 * result + gander.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + mainSubject.hashCode()
        result = 31 * result + subjects.hashCode()
        result = 31 * result + eligibleYear.contentHashCode()
        return result
    }
}