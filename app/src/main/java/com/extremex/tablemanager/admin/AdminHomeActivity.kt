package com.extremex.tablemanager.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.R
import com.extremex.tablemanager.admin.fragment.DashboardFragment
import com.extremex.tablemanager.databinding.ActivityAdminHomeBinding
import com.extremex.tablemanager.teacher.fragment.TimetableFragment

class AdminHomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAdminHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setCurrentFrame(DashboardFragment())
        binding.BottomNavBarA.setOnItemSelectedListener {
            when(it.itemId){
                R.id.Home -> setCurrentFrame(DashboardFragment())
                //R.id.Schedule -> setCurrentFrame(TimetableFragment())
                // add more id's and set fragment as frame
                else -> {
                    setCurrentFrame(DashboardFragment())
                }
            }
            true
        }


    }
    private fun setCurrentFrame(frame: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.MainViewFrameA, frame)
            addToBackStack(frame::class.java.simpleName)
            commit()
        }
}