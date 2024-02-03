package com.extremex.tablemanager.teacher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.R
import com.extremex.tablemanager.admin.fragment.DashboardFragment
import com.extremex.tablemanager.common.fragment.AboutAppFragment
import com.extremex.tablemanager.common.fragment.SettingsFragment
import com.extremex.tablemanager.databinding.ActivityHomeTeachersBinding
import com.extremex.tablemanager.teacher.fragment.HomeFragment
import com.extremex.tablemanager.teacher.fragment.TimetableFragment

class TeachersHomeActivity : AppCompatActivity(),
    SettingsFragment.SettingsListener,
    AboutAppFragment.AboutAppListener {
    private lateinit var binding : ActivityHomeTeachersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeTeachersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTheme()

        setCurrentFrame(HomeFragment())
        binding.BottomNavBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.Home -> setCurrentFrame(HomeFragment())
                R.id.Schedule -> setCurrentFrame(TimetableFragment())
                R.id.Settings -> setCurrentFrame(lastOpenedSettingsFragment())
                else -> {
                setCurrentFrame(HomeFragment())
                }
            }
            true
        }


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
    private fun lastOpenedSettingsFragment(): Fragment{
        val prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        return when(prefs.getInt("LastFragmentSettings",0)){
            0 -> SettingsFragment()
            1 -> AboutAppFragment()
            //2 -> ManageTimeSlotFragment()
            else -> DashboardFragment()
        }
    }
    private fun setCurrentFrame(frame: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.MainViewFrame, frame)
            addToBackStack(frame::class.java.simpleName)
            commit()
        }

    override fun onBackSettings() {
        val prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        val prefEditor = prefs.edit()
        prefEditor.putInt("LastFragmentSettings", 0)
        prefEditor.commit()
        setCurrentFrame(SettingsFragment())
    }

    override fun onAboutApp() {
        val prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        val prefEditor = prefs.edit()
        prefEditor.putInt("LastFragmentSettings", 1)
        prefEditor.commit()
        setCurrentFrame(AboutAppFragment())
    }

    override fun onPrivacyPolicy() {
        //T/ODO("Not yet implemented")
    }

    override fun onProfile() {
        //T/ODO("Not yet implemented")
    }
}