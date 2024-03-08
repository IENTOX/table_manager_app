package com.extremex.tablemanager.network

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.extremex.tablemanager.data.Gender
import com.extremex.tablemanager.data.UserType
import com.extremex.tablemanager.models.SignUpUserObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.Period
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import java.util.Base64

class ServerControl {
    enum class Action{
        SET, GET
    }
    enum class EmailVerificationStatus{
        SUCCESSFUL, COMPLETE, CANCELED, NOT_COMPLETE, UNDEFINED
    }

    @Suppress("SameParameterValue")
    class Offline{
        /*
        * This class will perform a a basic local login for offline login service.
        * this class has nothing to do with th online class hence it's a dummy class.
        *  user: {junelover -> #gitignore} -> FROM: git lab at 8:55 pm 06/03/2024.
        * */
        fun createSignUp(context: Context,action: Action, signUpUserObject: SignUpUserObject ){
            if (Helpers().isEmailValid(signUpUserObject.email)){
                if (Helpers().isPasswordValid(signUpUserObject.password)) {
                    if (Helpers().isValidPhoneNumber(signUpUserObject.phoneNumber.toString())){
                        if (
                            Helpers().verifyAge(
                                signUpUserObject.dateOfBirth[2],
                                signUpUserObject.dateOfBirth[1],
                                signUpUserObject.dateOfBirth[0],
                                24
                                )
                            ) {
                            doAction(context, action, signUpUserObject)
                        } else {
                            // invalid age
                        }
                    } else {
                        // invalid phone number
                    }
                } else {
                    // invalid password
                }
            } else {
                // invalid email
            }


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
    class Online{
        private lateinit var firebaseReference: DatabaseReference
        private lateinit var firebaseDatabase: FirebaseDatabase
        private lateinit var firebaseAuth: FirebaseAuth
        private lateinit var firebaseUser: FirebaseUser

        private fun init() {
            firebaseDatabase = FirebaseDatabase.getInstance("https://table-manager-25147-default-rtdb.firebaseio.com")
            firebaseReference = firebaseDatabase.reference
            firebaseAuth = FirebaseAuth.getInstance()
            firebaseUser = firebaseAuth.currentUser!!
        }
        private fun writeUserData(signUpUserObject: SignUpUserObject) {
            init()
            val helpers = Helpers()
            val dataObject = DataObject()

            val parcelData = helpers.createParcel(signUpUserObject)
            val userString = dataObject.userStringBuilder(parcelData)

            firebaseReference.child("users").child(firebaseUser.uid).setValue(userString)
        }
        fun writeUserData(key: String, value: Any, callback: (FirebaseUser?, FirebaseDatabase?) -> Unit) {
            init()
            val listener = DatabaseReference.CompletionListener { error, _ ->
                if (error == null) {
                    callback(firebaseUser, firebaseDatabase)
                } else {
                    callback(null, null)
                }
            }
            firebaseReference.child("users").child(firebaseUser.uid).child(key).setValue(value,listener)
        }
        private fun readUserData(callback: (SignUpUserObject?) -> Unit) {
            init()
            firebaseReference.child("users").child(firebaseUser.uid).get()
                .addOnSuccessListener { dataSnapshot ->
                    val userString = dataSnapshot.getValue(String::class.java)
                    val dataObject = DataObject()
                    val parcelData = dataObject.userArrayBuilder(userString)
                    val helpers = Helpers()
                    val signUpUserObject = parcelData?.let { helpers.extractParcel(it) }
                    callback(signUpUserObject)
                }
                .addOnFailureListener {
                    callback(null)
                }
        }

        fun signIn(email: String, password: String, callback: (FirebaseUser?, SignUpUserObject?) -> Unit) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        if (user != null) {
                            readUserData { signUpUserObject ->
                                if (user.isEmailVerified){
                                    callback(user, signUpUserObject)
                                } else {
                                    callback(null, null)
                                }
                            }
                        } else {
                            callback(null, null)
                        }
                    } else {
                        callback(null, null)
                    }
                }
        }

        fun signUp(signUpUserObject: SignUpUserObject, callback: (FirebaseUser?, EmailVerificationStatus) -> Unit) {
            init()
            val helpers = Helpers()
            if (helpers.isEmailValid(signUpUserObject.email)){
                if (helpers.isPasswordValid(signUpUserObject.password)) {
                    if (helpers.verifyAge(signUpUserObject.dateOfBirth, 24)){
                        if (helpers.isValidPhoneNumber(signUpUserObject.phoneNumber.toString())) {

                            firebaseAuth.createUserWithEmailAndPassword(signUpUserObject.email, signUpUserObject.password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val user = firebaseAuth.currentUser
                                        if (user != null) {
                                            // Write user data to Firebase Realtime Database
                                            writeUserData(signUpUserObject)
                                            user.sendEmailVerification()
                                            if (user.sendEmailVerification().isSuccessful) {
                                                callback(user, EmailVerificationStatus.SUCCESSFUL)
                                            } else if (user.sendEmailVerification().isComplete) {
                                                callback(user, EmailVerificationStatus.COMPLETE)
                                            } else {
                                                callback(user, EmailVerificationStatus.CANCELED)
                                            }
                                        } else {
                                            callback(null, EmailVerificationStatus.NOT_COMPLETE)
                                        }

                                    } else { callback(null, EmailVerificationStatus.UNDEFINED) }
                                }

                        } else {
                            // invalid Phone Number
                        }
                    } else {
                        // invalid Age
                    }
                } else {
                    // invalid Password REGEX failed
                }
            } else {
                // invalid Email
            }
        }
        fun resendVerificationEmail(){
            if (firebaseUser.email?.isNotBlank() == true) {
                firebaseUser.sendEmailVerification()
                    .addOnCompleteListener {
                        // inform user that the email has been sent
                    }
            } else {
                // user not found
            }

        }

        fun signOut() {
            init()
            firebaseAuth.signOut()
        }
        fun resetPassword(email: String) {
            init()
            if (firebaseUser.email == email){
                if (firebaseUser.uid.isBlank() && firebaseUser.isEmailVerified) {
                    firebaseAuth.sendPasswordResetEmail(email)
                } else {
                    // user not verified
                }
            } else {
                // email not found
            }
        }

        fun deleteAccount(){
            init()
            if (firebaseUser.uid.isBlank() && firebaseUser.isEmailVerified){
                firebaseUser.delete()
            } else {
                // user not verified
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

        fun userStringBuilder(userArray: Array<String>?): String? {
            return if (userArray.isNullOrEmpty()) {
                null
            } else {
                Helpers().encryptData(userArray.joinToString(separator = separator.toString()))
            }
        }

        fun userArrayBuilder(inputString: String?): Array<String>? {
            if (inputString.isNullOrBlank()) {
                return null
            }
            val finalString = Helpers().decryptData(inputString)
            val dataArray = finalString.split(separator).toTypedArray()
            return dataArray.copyOfRange(1, dataArray.size)
        }

        @SuppressLint("ApplySharedPref")
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
        fun verifyAge(birthDate: Array<Int>, requiredAge: Int = 18): Boolean {
            val birthDate = LocalDate.of(
                birthDate[2],
                birthDate[1],
                birthDate[0]
            )
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
