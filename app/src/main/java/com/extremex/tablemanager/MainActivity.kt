package com.extremex.tablemanager


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loginActivity = Intent(this@MainActivity, SignupInActivity::class.java)
        // Intent to go to Activity
        startActivity(loginActivity)

        // Close this Activity
        finish()
    }
}