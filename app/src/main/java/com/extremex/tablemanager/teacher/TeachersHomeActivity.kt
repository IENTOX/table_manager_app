package com.extremex.tablemanager.teacher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.ActivityHomeTeachersBinding
import com.extremex.tablemanager.teacher.fragment.HomeFragment
import com.extremex.tablemanager.teacher.fragment.TimetableFragment

class TeachersHomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeTeachersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeTeachersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setCurrentFrame(HomeFragment())
        binding.BottomNavBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.Home -> setCurrentFrame(HomeFragment())
                R.id.Schedule -> setCurrentFrame(TimetableFragment())
                // add more id's and set fragment as frame
                else -> {
                setCurrentFrame(HomeFragment())
                }
            }
            true
        }


    }
    private fun setCurrentFrame(frame: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.MainViewFrame, frame)
            addToBackStack(frame::class.java.simpleName)
            commit()
        }
}