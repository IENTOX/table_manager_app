package com.extremex.tablemanager.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.R
import com.extremex.tablemanager.common.fragment.LoginFragment
import com.extremex.tablemanager.common.fragment.SignUpFragment
import com.extremex.tablemanager.lib.PopUpBox
import com.extremex.tablemanager.network.ServerControl
import com.google.firebase.auth.FirebaseUser

class SignupInActivity : AppCompatActivity(),LoginFragment.AccountClickListener, SignUpFragment.SignupListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("SignUpInActivity","Loading GUI")
        setContentView(R.layout.activity_sign_up_in)
        Log.v("SignUpInActivity","Loading GUI Completed")
        Log.v("SignUpInActivity","Starting LoginFragment")
        setTheme()
        setCurrentFrame(LoginFragment())
    }

    private fun setTheme(){
        val pref = getSharedPreferences("APP_DATA_PREFS", AppCompatActivity.MODE_PRIVATE)
        when(pref.getFloat("SetTheme",2.0f)) {
            1f -> {
                Log.v("ThemeValue", "set : 1.0f")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            2f -> {
                Log.v("ThemeValue", "set : 2.0f")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }

            3f -> {
                Log.v("ThemeValue", "set : 3.0f")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else -> {
                //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_UNSPECIFIED)
                Log.v("ThemeValue", "set : 1")
                //slider.value = 1f
            }
        }
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

    override fun onSuccess(user: FirebaseUser?, status: ServerControl.EmailVerificationStatus){
        setCurrentFrame(LoginFragment())
    }

    override fun onFail(message: String, status: ServerControl.EmailVerificationStatus) {
        //setCurrentFrame(SignUpFragment())
        PopUpBox(this,
            "close",
            message,
            true)
    }
}