package com.extremex.tablemanager.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.R
import com.extremex.tablemanager.common.fragment.LoginFragment
import com.extremex.tablemanager.common.fragment.SignUpFragment

class SignupInActivity : AppCompatActivity(),LoginFragment.AccountClickListener, SignUpFragment.OnBack {
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
}