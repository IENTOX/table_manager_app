package com.extremex.tablemanager.fragment

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.text.trimmedLength
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.FragmentSignUpBinding
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
            if(
                checkIfEmpty(
                    binding.FirstName,
                    binding.LastName,
                    binding.EmailAddress,
                    binding.NewPassword,
                    binding.ConfirmPassword,
                    binding.IDNumber
                )
            ) {
                signUp(binding.FirstName.text.toString().trim(), binding.LastName.text.toString().trim())
            } else {
                Toast.makeText(this.requireContext(), "Invalid data", Toast.LENGTH_SHORT).show()
            }



        }
    }
    private fun checkIfEmpty(firstName: EditText, lastName: EditText, email: EditText, password: EditText, cPassword: EditText, Id: EditText) : Boolean
    {
        if(firstName.text.isEmpty())
        {
            firstName.error="This field cannot be empty"
        }
        else if(lastName.text.isEmpty())
        {
            lastName.error="This field cannot be empty"
        }
        else if(email.text.isEmpty())
        {
            email.error="This field cannot be empty"
        }
        else if(password.text.isEmpty())
        {
            password.error="This field cannot be empty"
        }
        else if(cPassword.text.isEmpty())
        {
            cPassword.error="This field cannot be empty"
        }
        else if(Id.text.isEmpty())
        {
            Id.error="This field cannot be empty"
        }
        else
        {
            return true
        }
        return false

    }
    private fun checkPassword(password: EditText, confirmPassword: EditText) : Boolean
    {
        if(password.text.toString().trim().length>8) {
            //if statement
        } else {
            password.error = StringReferences(this.requireContext()).SHORT_PASSWORD_ERROR
        }
        return false
    }
    private fun signUp( firstName: String, lastName: String ){

        val fullName: String = "$firstName $lastName"
        Toast.makeText(this.requireContext(), "You Signed up as $fullName  ", Toast.LENGTH_SHORT).show()
    }
}