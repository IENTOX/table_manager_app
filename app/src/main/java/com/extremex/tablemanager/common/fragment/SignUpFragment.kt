package com.extremex.tablemanager.common.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.data.Gender
import com.extremex.tablemanager.data.UserType
import com.extremex.tablemanager.databinding.FragmentSignUpBinding
import com.extremex.tablemanager.lib.PopUpBox
import com.extremex.tablemanager.models.SignUpUserObject
import com.extremex.tablemanager.network.ServerControl
import com.google.firebase.auth.FirebaseUser
import java.util.Calendar

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var datePicker: DatePickerDialog
    private lateinit var dateArray: Array<Int>
    private var isTeacher = false

    interface SignupListener {
        fun backPressed()
        fun onSuccess(user: FirebaseUser?, status: ServerControl.EmailVerificationStatus)
        fun onFail(message: String, status: ServerControl.EmailVerificationStatus)
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
    }
    private fun verifyUserDetails(firstName: EditText, lastName: EditText, email: EditText, password: EditText, cPassword: EditText, numberId: Int, birthDate: Array<Int>?, phNum: String, roomID: String, isTeacher: Boolean, instuteName: String) : Boolean {
        val server = ServerControl.Online()
        if(firstName.text.isEmpty()) {
            firstName.error="This field cannot be empty"
        } else if(lastName.text.isEmpty()) {
            lastName.error="This field cannot be empty"
        } else if(email.text.isEmpty()) {
            email.error="This field cannot be empty"
        } else if(password.text.isEmpty()) {
            password.error="This field cannot be empty"
        }  else if(cPassword.text.isEmpty()) {
            cPassword.error="This field cannot be empty"
        } else if(password.text.toString().trim() != cPassword.text.toString().trim()) {
            cPassword.error="Password does not match"
        } else if(birthDate == null){
            binding.DobSetter.error="Date of birth is required"
        } else if(binding.JoinRoomCheckBox.isChecked && roomID.isBlank()){
            binding.roomCode.error="This field cannot be empty"
        } else {
            Toast.makeText(
                this.requireContext(),
                "You Signed up as ${firstName.text}",
                Toast.LENGTH_SHORT
            ).show()

            val firstName = firstName.text.toString().trim()
            val lastName = lastName.text.toString().trim()
            val birthDate = arrayOf(birthDate[0], birthDate[1], birthDate[2])
            val phNum = phNum
            val email = email.text.toString().trim()
            val password = password.text.toString().trim()

            val userObject = SignUpUserObject(
                firstName,
                lastName,
                Gender.MALE,
                birthDate,
                "",
                phNum.toInt(),
                email,
                password,
                UserType.ADMIN,
                "0000000000"
            )

            server.signUp(userObject){ user, status ->
                if (
                    status == ServerControl.EmailVerificationStatus.SUCCESSFUL ||
                    status == ServerControl.EmailVerificationStatus.COMPLETE
                    ) {
                    signUpListener?.onSuccess(user, status)
                } else {
                    signUpListener?.onFail("", status)
                }
            }

            return true
        }
        return false
    }
}