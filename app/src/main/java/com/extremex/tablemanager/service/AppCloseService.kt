package com.extremex.tablemanager.service

import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.extremex.tablemanager.lib.ManagePrefetchData

class AppCloseService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.i("AppClosedCompletely", "True")
        val prefs: SharedPreferences = applicationContext.getSharedPreferences("FRAGMENT_TRANSACTIONS", AppCompatActivity.MODE_PRIVATE)
        val prefEditor: SharedPreferences.Editor = prefs.edit()
        prefEditor.apply {
            putInt("LastFragmentDashboard", 0)
            putInt("LastFragmentHome", 0)
        }.apply()
        Log.i("Set Dashboard and Home", "Set to null")
        super.onTaskRemoved(rootIntent)
    }
}