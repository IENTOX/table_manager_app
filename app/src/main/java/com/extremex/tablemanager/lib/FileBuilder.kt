package com.extremex.tablemanager.lib

import android.content.Context
import android.content.SharedPreferences

class FileBuilder(context: Context) {
    private val context = context
    private lateinit var prefs: SharedPreferences
    private lateinit var prefsEdit: SharedPreferences.Editor
    fun makeFile(name: String, data: List<Map<String, String>>) {
        prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        prefsEdit = prefs.edit()

        data.forEach { item ->
            item.forEach { (key, value) ->
                prefsEdit.putString(key, value)
            }
        }
        prefsEdit.apply()
    }
    fun makeFileForStorage(fileName: String, data: Map<String, String>){
        prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        prefsEdit = prefs.edit()
        data.forEach { (key, value) ->
            prefsEdit.putString(key, value)
        }
        prefsEdit.apply()
    }

    fun makeFileForStorageDoubleArrays(fileName: String, data: List<Map<Array<String>, Array<String>>>){
        prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        prefsEdit = prefs.edit()
        data.forEach { item ->
            item.forEach{ (key, value) ->
                val mainKey = key[0]
                var mainValue = ""
                    for(i in 0 until  value.size) {
                        if (value[i] != "") {
                            mainValue += "${value[i]}ӿ"
                        }
                    }
                prefsEdit.putString(mainKey, mainValue)
                prefsEdit.apply()
            }
        }
    }

    fun readDataFromFileForClassroomStorage(fileName: String,) : MutableList<String> {
        prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        val allKeys = prefs.all.keys
        return allKeys.toMutableList()
    }
}
//ӿ