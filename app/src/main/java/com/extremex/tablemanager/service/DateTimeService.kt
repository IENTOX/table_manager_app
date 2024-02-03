package com.extremex.tablemanager.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask


class DateTimeService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null
    private val timer = Timer()
    private var date :String = ""
    private var time :Double = 0.0
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        timer.scheduleAtFixedRate(UpdateTimeDate(),0,1000)
        return START_NOT_STICKY
    }
    inner class UpdateTimeDate : TimerTask(){
        override fun run() {
            // Get the current date and time
            val currentTime = Date()

            // Format the date and time
            val dateFormat = SimpleDateFormat("hh : mm : ss ", Locale.getDefault())
            // val dateFormat = SimpleDateFormat("HH : mm : ss ", Locale.getDefault())
            val formattedTime = dateFormat.format(currentTime)
            time = currentTime.time.toDouble()
            date = "${LocalDate.now().dayOfMonth} ${(LocalDate.now().month).toString().lowercase()} ${LocalDate.now().year}"

            val intent = Intent(DEVICE_DATE_TIME)
            intent.putExtra(DEVICE_TIME_UPDATED,formattedTime)
            intent.putExtra(DEVICE_DATE_UPDATED,date)
            sendBroadcast(intent)

            Log.v("DateTimeService", "Broadcasting: ${formattedTime}, $date")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
    companion object {
        const val DEVICE_DATE_TIME = "DeviceDateTime"
        const val DEVICE_TIME_UPDATED = "DeviceTimeUpdated"
        const val DEVICE_DATE_UPDATED = "DeviceDateUpdated"
    }
}