package com.extremex.tablemanager.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.R
import com.extremex.tablemanager.common.fragment.LoginFragment
import com.extremex.tablemanager.common.fragment.SignUpFragment
import com.extremex.tablemanager.lib.PopUpBox
import com.google.firebase.auth.FirebaseUser

class SignupInActivity : AppCompatActivity(),LoginFragment.AccountClickListener, SignUpFragment.SignupListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("SignUpInActivity","Loading GUI")
        setContentView(R.layout.activity_sign_up_in)
        Log.v("SignUpInActivity","Loading GUI Completed")
        Log.v("SignUpInActivity","Starting LoginFragment")
        setCurrentFrame(LoginFragment())
    }
    private  fun  setCurrentFrame(frame: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.SignInFrame, frame)
            commit()
        }

    override fun onCreateAccount() {
        Log.v("SignUpInActivity","Starting CreateAccountFragment")
        setCurrentFrame(SignUpFragment())
    }

    override fun backPressed() {
        Log.v("SignUpInActivity","back pressed")
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