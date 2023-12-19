package com.extremex.tablemanager.lib

import android.content.Context
import android.content.SharedPreferences
import java.util.Dictionary

class FileBuilder() {
    fun makeFile(name: String, data: List<Map<String, String>>, context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        val prefsEdit: SharedPreferences.Editor = prefs.edit()

        data.forEach { item ->
            item.forEach { (key, value) ->
                prefsEdit.putString(key, value)
            }
        }
        prefsEdit.apply()
    }
}