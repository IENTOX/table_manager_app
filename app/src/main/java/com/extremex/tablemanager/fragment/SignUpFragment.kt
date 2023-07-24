package com.extremex.tablemanager.fragment

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.InspectableProperty.ValueType
import androidx.core.text.trimmedLength
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.FragmentSignUpBinding
import com.extremex.tablemanager.lib.SigninData
import com.extremex.tablemanager.lib.StringReferences
import java.util.jar.Attributes.Name

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.SignUpButton.setOnClickListener{
            verifyDetails(
                binding.FirstName,
                binding.LastName,
                binding.EmailAddress,
                binding.NewPassword,
                binding.ConfirmPassword,
                binding.IDNumber
            )
        }
    }
    private fun verifyDetails(firstName: EditText, lastName: EditText, email: EditText, password: EditText, cPassword: EditText, Id: EditText) : Boolean
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
        } else if(password.text != cPassword.text) {
            cPassword.error="Password does not match"
        }  else if(Id.text.isEmpty()) {
            Id.error="This field cannot be empty"
        } else {
            // reformation of data before signup
            //signUpBuilder()
            Toast.makeText(this.requireContext(), "You Signed up as ${firstName.text}  ", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }
    private fun isPasswordValid(password: String): Boolean {
        val regex = Regex("^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}\$")
        return regex.matches(password)
    }
    private fun isEmailValid(email: String): Boolean {
        val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return regex.matches(email)
    }
    private fun signUpBuilder(
        firstName: String,
        lastName: String,
        DOB: String,
        module: List<String>,
        Id : Int,
        phNum: Int,
        cCode: Int,
        gander: String,
        email: String,
        password: String
    ): List<SigninData>?{
        val fullName: String = "$firstName $lastName"

        Toast.makeText(this.requireContext(), "You Signed up as $fullName  ", Toast.LENGTH_SHORT).show()

        // this function should not be null
        return null
    }
}
//@1Wwwwww
