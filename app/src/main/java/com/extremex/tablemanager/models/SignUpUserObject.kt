package com.extremex.tablemanager.models

import com.extremex.tablemanager.data.Gender
import com.extremex.tablemanager.data.UserType


data class SignUpUserObject (
    val firstName: String,
    val lastName: String,
    val gender: Gender,
    val dateOfBirth: Array<Int>,
    val countryCode: String,
    val phoneNumber: Int,
    val email: String,
    val password: String,
    val userType: UserType,
    val instituteCode: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SignUpUserObject

        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (gender != other.gender) return false
        if (!dateOfBirth.contentEquals(other.dateOfBirth)) return false
        if (countryCode != other.countryCode) return false
        if (phoneNumber != other.phoneNumber) return false
        if (email != other.email) return false
        if (password != other.password) return false
        if (userType != other.userType) return false
        return instituteCode == other.instituteCode
    }

    override fun hashCode(): Int {
        var result = firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + gender.hashCode()
        result = 31 * result + dateOfBirth.contentHashCode()
        result = 31 * result + countryCode.hashCode()
        result = 31 * result + phoneNumber
        result = 31 * result + email.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + userType.hashCode()
        result = 31 * result + (instituteCode?.hashCode() ?: 0)
        return result
    }
}
