package com.extremex.tablemanager.common


import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.extremex.tablemanager.service.AppCloseService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("MainActivity","App opened")
        val loginActivity = Intent(this@MainActivity, SignupInActivity::class.java)
        // Intent to go to Activity
        Log.v("MainActivity","Starting SignUpInActivity")
        startService(Intent(applicationContext, AppCloseService::class.java))
        setTheme()
        startActivity(loginActivity)
        // Close this Activity
        // finish()
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

    override fun onResume() {
        super.onResume()
        Log.i("AppClosedCompletely", "True")
        val prefs: SharedPreferences = applicationContext.getSharedPreferences("FRAGMENT_TRANSACTIONS", AppCompatActivity.MODE_PRIVATE)
        val prefEditor: SharedPreferences.Editor = prefs.edit()
        prefEditor.apply {
            putInt("LastFragmentDashboard", 0)
            putInt("LastFragmentHome", 0)
        }.apply()
        finish()
    }
}