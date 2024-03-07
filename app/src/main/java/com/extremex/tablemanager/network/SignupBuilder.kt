package com.extremex.tablemanager.network

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.extremex.tablemanager.data.Gender
import com.extremex.tablemanager.data.UserType
import com.extremex.tablemanager.models.SignUpUserObject
import java.time.LocalDate
import java.time.Period
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import java.util.Base64

class SignupBuilder {
    enum class Action{
        SET, GET
    }

    class Dummy{

        private fun createSignUpPackage(context: Context, signUpUserObject: SignUpUserObject ){


        }

        private fun doAction(context: Context, action: Action, signUpUserObject: SignUpUserObject? = null): Array<String>? {
            return when (action) {
                Action.GET -> { DataObject().getStored(context)}
                Action.SET -> {
                    DataObject().createAndStore(context, Helpers().createParcel(signUpUserObject))
                    null
                }
            }
        }
    }


    private class DataObject{
        private val separator: Char = 'ӿ'
        private val hash = "TMASECUREKEY"

        fun getHashKey(): String {
            return hash
        }

        fun createAndStore(context: Context, userArray: Array<String>?){
            return createFile(context,userStringBuilder(userArray))
        }

        fun getStored(context: Context): Array<String>? {
            return userArrayBuilder(getData(context))
        }

        private fun getFileName(): String{
            return "TMSAx2605-app-util-network-service"
        }

        private fun getUserCredentials(): String{
            return "network-Service-key"
        }

        private fun userStringBuilder(userArray: Array<String>?): String? {
            return if (userArray.isNullOrEmpty()) {
                null
            } else {
                Helpers().encryptData(userArray.joinToString(separator = separator.toString()))
            }
        }

        private fun userArrayBuilder(inputString: String?): Array<String>? {
            if (inputString.isNullOrBlank()) {
                return null
            }
            val finalString = Helpers().decryptData(inputString)
            val dataArray = finalString.split(separator).toTypedArray()
            return dataArray.copyOfRange(1, dataArray.size)
        }

        private fun createFile(context: Context, data: String?){
            if (data != null) {
                val pref =
                    context.getSharedPreferences(getFileName(), AppCompatActivity.MODE_PRIVATE)
                val prefEditor = pref.edit()
                prefEditor.putString(getUserCredentials(), data)
                prefEditor.commit()
            }
        }

        private fun getData(context: Context): String?{
            val pref = context.getSharedPreferences(getFileName(),AppCompatActivity.MODE_PRIVATE)
            return pref.getString(getUserCredentials(),null)
        }
    }

    @SuppressLint("GetInstance")
    private class Helpers {

        fun verifyAge(birthYear: Int, birthMonth: Int, birthDay: Int, requiredAge: Int = 18): Boolean {
            val birthDate = LocalDate.of(birthYear, birthMonth, birthDay)
            val currentDate = LocalDate.now()
            val age = Period.between(birthDate, currentDate).years
            return age >= requiredAge
        }
         fun isPasswordValid(password: String): Boolean {
            val regex = Regex("^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}\$")
            return regex.matches(password)
        }
         fun isEmailValid(email: String): Boolean {
            val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
            return regex.matches(email)
        }
         fun isValidPhoneNumber(phoneNumber: String?): Boolean {
            if (phoneNumber == null) return false
            return phoneNumber.matches("^[0-9]{10,15}$".toRegex())
        }

        fun encryptData(data: String): String {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, getKey())
            val encryptedBytes = cipher.doFinal(data.toByteArray())
            return Base64.getEncoder().encodeToString(encryptedBytes)
        }

        fun decryptData(encryptedData: String): String {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, getKey())
            val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData))
            return String(decryptedBytes)
        }
        fun createParcel(userObject: SignUpUserObject?): Array<String>? {
            if (userObject != null) {
                val genderString = when (userObject.gender) {
                    Gender.MALE -> "MALE"
                    Gender.FEMALE -> "FEMALE"
                    Gender.OTHER -> "OTHER"
                }
                val userTypeString = when (userObject.userType) {
                    UserType.STUDENT -> "STUDENT"
                    UserType.TEACHER -> "TEACHER"
                    UserType.ADMIN -> "ADMIN"
                }
                val finalCode = if (userObject.userType != UserType.ADMIN) {
                    "null"
                } else {
                    userObject.instituteCode ?: "null"
                }
                return arrayOf(
                    userObject.firstName,
                    userObject.lastName,
                    genderString,
                    userObject.dateOfBirth.joinToString(separator = "/"),
                    userObject.countryCode,
                    userObject.phoneNumber.toString(),
                    userObject.email,
                    userObject.password,
                    userTypeString,
                    finalCode
                )
            } else {
                return null
            }
        }

        fun extractParcel(parcelData: Array<String>?): SignUpUserObject? {
            if (parcelData == null || parcelData.size < 9) {
                return null
            }
            val gender = when (parcelData[2]) {
                "MALE" -> Gender.MALE
                "FEMALE" -> Gender.FEMALE
                "OTHER" -> Gender.OTHER
                else -> return null
            }
            val userType = when (parcelData[8]) {
                "STUDENT" -> UserType.STUDENT
                "TEACHER" -> UserType.TEACHER
                "ADMIN" -> UserType.ADMIN
                else -> return null
            }
            val dateOfBirthArray = parcelData[3].split("/").map { it.toIntOrNull() ?: return null }.toTypedArray()
            return SignUpUserObject(
                firstName = parcelData[0],
                lastName = parcelData[1],
                gender = gender,
                dateOfBirth = dateOfBirthArray,
                countryCode = parcelData[4],
                phoneNumber = parcelData[5].toIntOrNull() ?: return null,
                email = parcelData[6],
                password = parcelData[7],
                userType = userType,
                instituteCode = parcelData[9].ifBlank { null }
            )
        }
        private fun getKey(): Key {
            val keyBytes = DataObject().getHashKey().toByteArray()
            return SecretKeySpec(keyBytes, "AES")
        }
    }
}
