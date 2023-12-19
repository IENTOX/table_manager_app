package com.extremex.tablemanager.common


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("MainActivity","App opened")
        val loginActivity = Intent(this@MainActivity, SignupInActivity::class.java)
        // Intent to go to Activity
        Log.v("MainActivity","Starting SignUpInActivity")
        startActivity(loginActivity)
        // Close this Activity
        finish()
    }
}