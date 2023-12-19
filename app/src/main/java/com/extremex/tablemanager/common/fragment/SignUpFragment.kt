package com.extremex.tablemanager.common.fragment

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.R
import com.extremex.tablemanager.admin.AdminHomeActivity
import com.extremex.tablemanager.lib.SigninData
import com.extremex.tablemanager.databinding.FragmentSignUpBinding
import com.extremex.tablemanager.lib.PopUpBox
import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.Period
import java.util.Calendar

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase

    interface SignupListener{
        fun backPressed()
        fun onSuccess(
            user :FirebaseUser,
            firstName: String,
            lastName: String,
            DOB: String,
            Id : Int,
            phNum: String,
            email: String,
            isTeacher: Boolean
        )
        fun onFail(message: String)
    }
    private var signUpListener :SignupListener? = null

    fun onBackClicked(listener: SignupListener){
        this.signUpListener = listener
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SignupListener){
            signUpListener = context
        } else {
            throw RuntimeException("$context must implement AccountClickListener")
        }
    }

    private var dateArray: Array<Int>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.BackBtn.setOnClickListener {
            signUpListener?.backPressed()
        }

        binding.DobSetter.setOnClickListener {
            showDateSetter() { day, month, year ->
                dateArray = arrayOf(day,month,year)
                binding.DobSetter.text = "$day/$month/$year"
            }
        }
        binding.JoinRoomCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.roomCode.visibility = if (isChecked){
                binding.InstuteTitle.visibility = View.GONE
                binding.InstuteName.visibility = View.GONE
                View.VISIBLE
            } else {
                binding.InstuteTitle.visibility = View.VISIBLE
                binding.InstuteName.visibility = View.VISIBLE
                View.GONE
            }
        }
        binding.roomInfo.setOnClickListener {
            PopUpBox(requireContext(),
                "Dismiss",
                "check the box only if you need to join an existing room. if you want to create a new room please leave it unchecked and proceed to signup.",
                true,
                true,
                "Join Existing Room"
            )
        }

        binding.SignUpButton.setOnClickListener{
            if (binding.JoinRoomCheckBox.isChecked && binding.roomCode.text.toString().isBlank()){
                PopUpBox(requireContext(),
                    "close",
                    "To join an existing room please add the room code provided by your Admin, if you are creating a new room make sure you uncheck the box before signing up.",
                    true
                )
            } else {
                verifyDetails(
                    binding.FirstName,
                    binding.LastName,
                    binding.EmailAddress,
                    binding.NewPassword,
                    binding.ConfirmPassword,
                    0,
                    dateArray,
                    binding.PhoneNumber.text.toString(),
                    binding.roomCode.text.toString(),
                    binding.JoinRoomCheckBox.isChecked,
                    binding.InstuteName.text.toString()
                )
            }
        }
    }
    private fun verifyDetails(
        firstName: EditText,
        lastName: EditText,
        email: EditText,
        password: EditText,
        cPassword: EditText,
        numberId: Int,
        birthDate: Array<Int>?,
        phNum: String,
        roomID: String,
        isTeacher: Boolean,
        instuteName: String
    ) : Boolean

    {
        if(firstName.text.isEmpty()) {
            firstName.error="This field cannot be empty"
        } else if(lastName.text.isEmpty()) {
            lastName.error="This field cannot be empty"
        } else if(email.text.isEmpty()) {
            email.error="This field cannot be empty"
        } else if (!isEmailValid(email.text.toString().trim())){
            email.error="Invalid Email"
        } else if(password.text.isEmpty()) {
            password.error="This field cannot be empty"
        } else if (!isPasswordValid(password.text.toString().trim())){
            password.error="Invalid Password: password should contain at least 1 number, 1 Capital latter, and a special character."
        } else if(cPassword.text.isEmpty()) {
            cPassword.error="This field cannot be empty"
        } else if(password.text.toString().trim() != cPassword.text.toString().trim()) {
            cPassword.error="Password does not match"
        } /* else if(numberId.text.isEmpty()) {
            numberId.error="This field cannot be empty"
        } */else if(birthDate == null){
            binding.DobSetter.error="Date of birth is required"
        } else if(!isValidPhoneNumber(phNum.toString())){
            binding.PhoneNumber.error="please enter a valid Phone number"
        } else if(binding.JoinRoomCheckBox.isChecked && roomID.isBlank()){
            binding.roomCode.error="This field cannot be empty"
        } else {
            // reformation of data before signup
            if (verifyAge(birthDate[2], birthDate[1], birthDate[0], 24)) {
                Toast.makeText(
                    this.requireContext(),
                    "You Signed up as ${firstName.text}",
                    Toast.LENGTH_SHORT
                ).show()
                signUpBuilder(
                    firstName.text.toString().trim(),
                    lastName.text.toString().trim(),
                    "${birthDate[0]}/${birthDate[1]}/${birthDate[2]}",
                    numberId,
                    phNum.toString(),
                    email.text.toString().trim(),
                    password.text.toString().trim(),
                    isTeacher
                )
                return true
            } else {
                PopUpBox(
                    requireContext(),
                    "close",
                    "Your age does not meet the minimum requirement",
                    true
                )
            }
        }
        return false
    }
    private fun showDateSetter( onDateSet: (day: Int, month: Int, year: Int) -> Unit) {
        // Get current date
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        // Show DatePickerDialog
        val datePicker = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
            onDateSet(dayOfMonth, monthOfYear + 1, year)
        }, currentYear, currentMonth, currentDay)

        datePicker.show()
    }
    private fun verifyAge(birthYear: Int, birthMonth: Int, birthDay: Int, requiredAge: Int = 18): Boolean {
        val birthDate = LocalDate.of(birthYear, birthMonth, birthDay)
        val currentDate = LocalDate.now()
        val age = Period.between(birthDate, currentDate).years
        return age >= requiredAge
    }
    private fun isPasswordValid(password: String): Boolean {
        val regex = Regex("^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}\$")
        return regex.matches(password)
    }
    private fun isEmailValid(email: String): Boolean {
        val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return regex.matches(email)
    }

    private fun isValidPhoneNumber(phoneNumber: String?): Boolean {
        if (phoneNumber == null) return false
        return phoneNumber.matches("^[0-9]{10,15}$".toRegex())
    }
    private fun signUpBuilder(
        firstName: String,
        lastName: String,
        DOB: String,
        Id : Int,
        phNum: String,
        email: String,
        password: String,
        isTeacher: Boolean
    ): List<SigninData>?{
        firebaseSignup(email,password, firstName, lastName, DOB, Id, phNum, isTeacher )
        return null
    }
    private fun firebaseSignup(email: String, password: String, firstName: String, lastName: String, DOB: String, Id : Int, phNum: String, isTeacher: Boolean) : Boolean {
        var result: Boolean = false
        firebaseAuth =Firebase.auth
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success")
                val user = firebaseAuth.currentUser
                if (user != null) {
                    user.sendEmailVerification().addOnCompleteListener {
                        result = if (it.isSuccessful){

                            val UID = user.uid
                            firebaseDatabase = FirebaseDatabase.getInstance()
                            database = firebaseDatabase.getReferenceFromUrl("https://table-manager-25147-default-rtdb.firebaseio.com")
                            database.child("Users").child(UID)
                            database.child("Users").child(UID).child("FirstName").setValue(firstName)
                            database.child("Users").child(UID).child("LastName").setValue(lastName)
                            database.child("Users").child(UID).child("DateOfBirth").setValue(DOB)
                            database.child("Users").child(UID).child("UniqueID").setValue(Id)
                            database.child("Users").child(UID).child("PhoneNumber").setValue(phNum)
                            database.child("Users").child(UID).child("Email").setValue(email)

                            PopUpBox(requireContext(),
                                "Close",
                                "Email verification has been sent to $email, proceed and verify before login.",
                                true,
                                true,
                                "Email Verification")

                            signUpListener?.onSuccess(user,firstName, lastName,DOB, Id, phNum, email, isTeacher )
                            true
                        } else{
                            signUpListener?.onFail("Failed to send a Verification email, you can try using a different email or try again after some time. ")
                            false
                        }
                    }
                } else {
                    signUpListener?.onFail("Failed to sign up, Unknown Error.")
                    result = false
                }

            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", it.exception)
                signUpListener?.onFail("Failed to sign up, please make sure you are connected to Internet.")
                result = false
            }
        }
        return result
    }

}

