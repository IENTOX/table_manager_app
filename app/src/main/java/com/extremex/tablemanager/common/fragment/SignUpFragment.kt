package com.extremex.tablemanager.common.fragment

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.FragmentSignUpBinding
import com.extremex.tablemanager.lib.FileBuilder
import com.extremex.tablemanager.lib.PopUpBox
import com.extremex.tablemanager.lib.SigninData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.Period
import java.util.Calendar

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var datePicker: DatePickerDialog
    private lateinit var dateArray: Array<Int>
    private var isTeacher = false

    interface SignupListener {
        fun backPressed()
        fun onSuccess(user: FirebaseUser, firstName: String, lastName: String, DOB: String, Id: Int, phNum: String, email: String, isTeacher: Boolean)
        fun onFail(message: String)
    }

    private var signUpListener: SignupListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        signUpListener = context as? SignupListener ?: throw RuntimeException("$context must implement SignupListener")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding.BackBtn.setOnClickListener { signUpListener?.backPressed() }

        binding.DobSetter.setOnClickListener { showDateSetter() }

        binding.JoinRoomCheckBox.setOnCheckedChangeListener { _, isChecked ->
            val visibility = if (isChecked) View.VISIBLE else View.GONE
            with(binding) {
                roomCode.visibility = visibility
                InstuteTitle.visibility = if (isChecked) View.GONE else View.VISIBLE
                InstuteName.visibility = if (isChecked) View.GONE else View.VISIBLE
            }
        }

        binding.roomInfo.setOnClickListener {
            PopUpBox(requireContext(), "Dismiss", "Check the box only if you need to join an existing room. If you want to create a new room, please leave it unchecked and proceed to signup.", true, true, "Join Existing Room")
        }

        binding.SignUpButton.setOnClickListener {
            verifyDetails()
        }
    }

    private fun showDateSetter() {
        if (!::datePicker.isInitialized) {
            val calendar = Calendar.getInstance()
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentYear = calendar.get(Calendar.YEAR)

            datePicker = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
                dateArray = arrayOf(dayOfMonth, monthOfYear + 1, year)
                binding.DobSetter.text = "$dayOfMonth/${monthOfYear + 1}/$year"
            }, currentYear, currentMonth, currentDay)
        }

        datePicker.show()
    }

    private fun verifyDetails() {
        // Your existing code for verification here...

        if (verifyAge(dateArray[2], dateArray[1], dateArray[0], 18)) {
            verifyUserDetails(
                binding.FirstName,
                binding.LastName,
                binding.EmailAddress,
                binding.NewPassword,
                binding.ConfirmPassword,
                0,
                dateArray,
                "",
                "",
                isTeacher,
                binding.InstuteName.text.toString().trim()
            )
        } else {
            PopUpBox(requireContext(), "close", "Your age does not meet the minimum requirement", true)
        }
    }

    private fun signUpBuilder(firstName: String, lastName: String, DOB: String, Id: Int, phNum: String, email: String, password: String, isTeacher: Boolean) {
        firebaseSignup(email, password, firstName, lastName, DOB, Id, phNum, isTeacher)
    }
    private fun verifyUserDetails(firstName: EditText, lastName: EditText, email: EditText, password: EditText, cPassword: EditText, numberId: Int, birthDate: Array<Int>?, phNum: String, roomID: String, isTeacher: Boolean, instuteName: String) : Boolean

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

                val firstName = firstName.text.toString().trim()
                val lastName = lastName.text.toString().trim()
                val birthDate = "${birthDate[0]}/${birthDate[1]}/${birthDate[2]}"
                val phNum = phNum.toString()
                val email = email.text.toString().trim()
                val password = password.text.toString().trim()

                val data = listOf(
                    mapOf("Key" to "FirstName", "Value" to firstName),
                    mapOf("Key" to "LastName", "Value" to lastName),
                    mapOf("Key" to "BirthDate", "Value" to birthDate),
                    mapOf("Key" to "NumberId", "Value" to numberId.toString()),
                    mapOf("Key" to "PhoneNumber", "Value" to phNum),
                    mapOf("Key" to "Email", "Value" to email),
                    mapOf("Key" to "Password", "Value" to password),
                    mapOf("Key" to "IsTeacher", "Value" to isTeacher.toString())
                )

                val fileBuilder = FileBuilder(requireContext())
                fileBuilder.makeFile("userTemp",data,)
                signUpBuilder(firstName, lastName, birthDate, numberId, phNum, email, password, isTeacher)
                return true
            } else {
                PopUpBox(requireContext(), "close", "Your age does not meet the minimum requirement", true)
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
    private fun firebaseSignup(email: String, password: String, firstName: String, lastName: String, DOB: String, Id : Int, phNum: String, isTeacher: Boolean) : Boolean {
        var result: Boolean = false
        firebaseAuth = Firebase.auth
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