package com.extremex.tablemanager.admin

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.R
import com.extremex.tablemanager.admin.fragment.DashboardFragment
import com.extremex.tablemanager.admin.fragment.ManageClassroomFragment
import com.extremex.tablemanager.admin.fragment.ManageSubjectFragment
import com.extremex.tablemanager.admin.fragment.ManageTeacherFragment
import com.extremex.tablemanager.admin.fragment.ManageTimeSlotFragment
import com.extremex.tablemanager.databinding.ActivityAdminHomeBinding

class AdminHomeActivity : AppCompatActivity(),
    DashboardFragment.ItemListeners,
    ManageTimeSlotFragment.TimeSlotFragmentListener,
    ManageTeacherFragment.ManageTeacherListener,
    ManageClassroomFragment.ManageClassroomListener,
    ManageSubjectFragment.ManageSubjectListener{

    private lateinit var binding : ActivityAdminHomeBinding
    private lateinit var prefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setCurrentFrame(lastOpened())
        binding.BottomNavBarA.setOnItemSelectedListener {
            when(it.itemId){
                R.id.Home -> {
                    setCurrentFrame(lastOpened())
                }
                //R.id.Schedule -> setCurrentFrame(TimetableFragment())
                // add more id's and set fragment as frame
                else -> {
                    setCurrentFrame(DashboardFragment())
                }
            }
            true
        }


    }
    private fun lastOpened(): Fragment{
        prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        return when(prefs.getInt("LastFragment",0)){
            0 -> DashboardFragment()
            1 -> ManageSubjectFragment()
            2 -> ManageTimeSlotFragment()
            3 -> ManageClassroomFragment()
            4 -> ManageTeacherFragment()
            else -> DashboardFragment()
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
        prefEditor.putInt("LastFragment", 4)
        prefEditor.commit()
        setCurrentFrame(ManageTeacherFragment())
    }

    override fun onManageClassroom() {
        prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        val prefEditor = prefs.edit()
        prefEditor.putInt("LastFragment", 3)
        prefEditor.commit()
        setCurrentFrame(ManageClassroomFragment())
    }

    override fun onAddTimeSlot() {
        prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        val prefEditor = prefs.edit()
        prefEditor.putInt("LastFragment", 2)
        prefEditor.commit()
        setCurrentFrame(ManageTimeSlotFragment())
    }

    override fun onManageSubject() {
        prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        val prefEditor = prefs.edit()
        prefEditor.putInt("LastFragment", 1)
        prefEditor.commit()
        setCurrentFrame(ManageSubjectFragment())
    }


    override fun onBack() {
        prefs = getSharedPreferences("FRAGMENT_TRANSACTIONS", MODE_PRIVATE)
        val prefEditor = prefs.edit()
        prefEditor.putInt("LastFragment", 0)
        prefEditor.commit()
        setCurrentFrame(DashboardFragment())
    }
}