package com.extremex.tablemanager.admin

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.R
import com.extremex.tablemanager.admin.fragment.AdminProfileFragment
import com.extremex.tablemanager.common.fragment.AboutAppFragment
import com.extremex.tablemanager.common.fragment.SettingsFragment
import com.extremex.tablemanager.admin.fragment.DashboardFragment
import com.extremex.tablemanager.admin.fragment.ManageClassroomFragment
import com.extremex.tablemanager.admin.fragment.ManageSubjectFragment
import com.extremex.tablemanager.admin.fragment.ManageTeacherFragment
import com.extremex.tablemanager.admin.fragment.ManageTimeSlotFragment
import com.extremex.tablemanager.databinding.ActivityAdminHomeBinding
import com.extremex.tablemanager.teacher.fragment.TimetableFragment

class AdminHomeActivity : AppCompatActivity(),
    DashboardFragment.ItemListeners,
    ManageTimeSlotFragment.TimeSlotFragmentListener,
    ManageTeacherFragment.ManageTeacherListener,
    ManageClassroomFragment.ManageClassroomListener,
    ManageSubjectFragment.ManageSubjectListener,
    SettingsFragment.SettingsListener,
    AboutAppFragment.AboutAppListener,
    AdminProfileFragment.AdminProfileListener{

    private lateinit var binding : ActivityAdminHomeBinding
    private lateinit var prefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTheme()


        setCurrentFrame(lastOpenedMainFragment())
        binding.BottomNavBarA.setOnItemSelectedListener {
            when(it.itemId){
                R.id.Home -> setCurrentFrame(lastOpenedDashboardFragment())
                R.id.Schedule -> setCurrentFrame(TimetableFragment())
                R.id.Settings -> setCurrentFrame(lastOpenedSettingsFragment())
                else -> {
                    setCurrentFrame(DashboardFragment())
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
    private fun lastOpenedDashboardFragment(): Fragment{
        prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        return when(prefs.getInt("LastFragmentDashboard",0)){
            0 -> DashboardFragment()
            1 -> ManageSubjectFragment()
            2 -> ManageTimeSlotFragment()
            3 -> ManageClassroomFragment()
            4 -> ManageTeacherFragment()
            else -> DashboardFragment()
        }
    }
    private fun lastOpenedSettingsFragment(): Fragment{
        prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        return when(prefs.getInt("LastFragmentSettings",0)){
            0 -> SettingsFragment()
            1 -> AboutAppFragment()
            2 -> AdminProfileFragment()
            else -> DashboardFragment()
        }
    }
    private fun lastOpenedMainFragment(): Fragment{
        prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        return when(prefs.getInt("LastFragmentMain",0)){
            0 -> lastOpenedDashboardFragment()
            1 -> AboutAppFragment()
            2 -> lastOpenedSettingsFragment()
            else -> lastOpenedDashboardFragment()
        }
    }
    private fun setCurrentFrame(frame: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.MainViewFrameA, frame)
            addToBackStack(frame::class.java.simpleName)
            commit()
        }
    override fun onManageTeacher() {
        prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        val prefEditor = prefs.edit()
        prefEditor.putInt("LastFragmentDashboard", 4)
        prefEditor.commit()
        setCurrentFrame(ManageTeacherFragment())
    }

    override fun onManageClassroom() {
        prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        val prefEditor = prefs.edit()
        prefEditor.putInt("LastFragmentDashboard", 3)
        prefEditor.commit()
        setCurrentFrame(ManageClassroomFragment())
    }

    override fun onAddTimeSlot() {
        prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        val prefEditor = prefs.edit()
        prefEditor.putInt("LastFragmentDashboard", 2)
        prefEditor.commit()
        setCurrentFrame(ManageTimeSlotFragment())
    }

    override fun onManageSubject() {
        prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        val prefEditor = prefs.edit()
        prefEditor.putInt("LastFragmentDashboard", 1)
        prefEditor.commit()
        setCurrentFrame(ManageSubjectFragment())
    }
    override fun onBack() {
        prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        val prefEditor = prefs.edit()
        prefEditor.putInt("LastFragmentDashboard", 0)
        prefEditor.commit()
        setCurrentFrame(DashboardFragment())
    }

    private var count = 0
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.MainViewFrameA)
        if (currentFragment is DashboardFragment) {
            count++
            if (count == 2) {
                super.onBackPressed()
                finish()
            } else {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
            }
        } else if (currentFragment is AboutAppFragment || currentFragment is AdminProfileFragment) {
            prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
            val prefEditor = prefs.edit()
            prefEditor.putInt("LastFragmentSettings", 0)
            prefEditor.commit()
            setCurrentFrame(SettingsFragment())
        } else {
            prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
            val prefEditor = prefs.edit()
            prefEditor.putInt("LastFragmentDashboard", 0)
            prefEditor.commit()
            binding.BottomNavBarA.selectedItemId = R.id.Dashboard
            setCurrentFrame(DashboardFragment())
        }
    }

    override fun onAboutApp() {
        prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        val prefEditor = prefs.edit()
        prefEditor.putInt("LastFragmentSettings", 1)
        prefEditor.commit()
        setCurrentFrame(AboutAppFragment())
    }

    override fun onPrivacyPolicy() {
        // yet to be done
    }

    override fun onProfile() {
        prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        val prefEditor = prefs.edit()
        prefEditor.putInt("LastFragmentSettings", 2)
        prefEditor.commit()
        setCurrentFrame(AdminProfileFragment())
    }

    override fun onBackSettings() {
        prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        val prefEditor = prefs.edit()
        prefEditor.putInt("LastFragmentSettings", 0)
        prefEditor.commit()
        setCurrentFrame(SettingsFragment())
    }
}