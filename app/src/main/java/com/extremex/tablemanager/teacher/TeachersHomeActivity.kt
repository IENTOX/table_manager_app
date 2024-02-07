package com.extremex.tablemanager.teacher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.R
import com.extremex.tablemanager.admin.fragment.AdminProfileFragment
import com.extremex.tablemanager.admin.fragment.DashboardFragment
import com.extremex.tablemanager.common.fragment.AboutAppFragment
import com.extremex.tablemanager.common.fragment.LeaveApplicationFragment
import com.extremex.tablemanager.common.fragment.SettingsFragment
import com.extremex.tablemanager.databinding.ActivityHomeTeachersBinding
import com.extremex.tablemanager.teacher.fragment.HomeFragment
import com.extremex.tablemanager.teacher.fragment.TeacherProfileFragment
import com.extremex.tablemanager.teacher.fragment.TimetableFragment

class TeachersHomeActivity : AppCompatActivity(),
    HomeFragment.HomeListener,
    SettingsFragment.SettingsListener,
    AboutAppFragment.AboutAppListener,
    TeacherProfileFragment.TeacherProfileListener,
    LeaveApplicationFragment.LeaveApplicationListener{
    private lateinit var binding : ActivityHomeTeachersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeTeachersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTheme()

        setCurrentFrame(lastOpenedMainFragment())
        binding.BottomNavBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.Home -> setCurrentFrame(lastOpenedHomeFragment())
                R.id.Schedule -> setCurrentFrame(TimetableFragment())
                R.id.Settings -> setCurrentFrame(lastOpenedSettingsFragment())
                else -> { setCurrentFrame(HomeFragment())
                }
            }
            true
        }


    }
    private fun setTheme(){
        val pref = getSharedPreferences("APP_DATA_PREFS", AppCompatActivity.MODE_PRIVATE)
        when(pref.getFloat("SetTheme",2.0f)) {
            1f -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            2f -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            3f -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_UNSPECIFIED)
        }
    }
    private fun lastOpenedSettingsFragment(): Fragment{
        val prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        return when(prefs.getInt("LastFragmentSettings",0)){
            0 -> SettingsFragment()
            1 -> AboutAppFragment()
            2 -> TeacherProfileFragment()
            else -> SettingsFragment()
        }
    }

    private fun lastOpenedMainFragment(): Fragment {
        val prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        return when (prefs.getInt("LastFragmentMain", 0)) {
            0 -> lastOpenedHomeFragment()
            1 -> TimetableFragment()
            2 -> lastOpenedSettingsFragment()
            else -> lastOpenedHomeFragment()
        }
    }

    private fun lastOpenedHomeFragment(): Fragment{
        val prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        return when(prefs.getInt("LastFragmentHome",0)){
            0 -> HomeFragment()
            1 -> LeaveApplicationFragment()
            else -> HomeFragment()
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
        val prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        val prefEditor = prefs.edit()
        prefEditor.putInt("LastFragmentSettings", 2)
        prefEditor.commit()
        setCurrentFrame(TeacherProfileFragment())
    }

    override fun onBack() {
        val prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        val prefEditor = prefs.edit()
        prefEditor.putInt("LastFragmentHome", 0)
        prefEditor.commit()
        setCurrentFrame(HomeFragment())
    }

    override fun onApply(
        userName: String,
        userId: String,
        description: String,
        isUncertain: Boolean,
        selectedLeaveType: String,
        leaveApplicationDate: String,
        leaveStartDate: String,
        leaveEndDate: String
    ){
        //T/ODO("Not yet implemented")
    }

    override fun onLeaveApplication() {
        val prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        val prefEditor = prefs.edit()
        prefEditor.putInt("LastFragmentHome", 1)
        prefEditor.commit()
        setCurrentFrame(LeaveApplicationFragment())
    }

    private var count = 0
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        val currentFragment = supportFragmentManager.findFragmentById(R.id.MainViewFrame)
        if (currentFragment is HomeFragment) {
            count++
            if (count == 2) {
                super.onBackPressed()
                finish()
            } else {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
            }
        } else if (currentFragment is AboutAppFragment || currentFragment is TeacherProfileFragment) {
            val prefEditor = prefs.edit()
            prefEditor.putInt("LastFragmentSettings", 0)
            prefEditor.commit()
            setCurrentFrame(SettingsFragment())
        } else {
            val prefEditor = prefs.edit()
            prefEditor.putInt("LastFragmentHome", 0)
            prefEditor.commit()
            binding.BottomNavBar.selectedItemId = R.id.Home
            setCurrentFrame(HomeFragment())
        }
    }
}