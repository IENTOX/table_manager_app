package com.extremex.tablemanager.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.R
import com.extremex.tablemanager.common.fragment.LoginFragment
import com.extremex.tablemanager.common.fragment.SignUpFragment
import com.extremex.tablemanager.lib.PopUpBox
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import java.util.EventListener

class SignupInActivity : AppCompatActivity(),LoginFragment.AccountClickListener, SignUpFragment.SignupListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_in)
        setCurrentFrame(LoginFragment())
    }
    private  fun  setCurrentFrame(frame: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.SignInFrame, frame)
            commit()
        }

    override fun onCreateAccount() {
        setCurrentFrame(SignUpFragment())
    }

    override fun backPressed() {
        setCurrentFrame(LoginFragment())
    }

    override fun onSuccess(
        user :FirebaseUser, firstName: String, lastName: String, DOB: String, Id : Int, phNum: String, email: String, isTeacher: Boolean
    ) {
        setCurrentFrame(LoginFragment())
    }

    override fun onFail(message: String) {
        //setCurrentFrame(SignUpFragment())
        PopUpBox(this,
            "close",
            message,
            true)
    }
}