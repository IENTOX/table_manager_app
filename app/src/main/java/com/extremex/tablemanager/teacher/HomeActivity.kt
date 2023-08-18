package com.extremex.tablemanager.teacher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.ActivityHomeBinding
import com.extremex.tablemanager.fragment.TimetableFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BottomNavBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.Schedule -> setCurrentFrame(TimetableFragment())
                // add more id's and set fragment as frame
                else -> {
                // do nothing
                // }
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