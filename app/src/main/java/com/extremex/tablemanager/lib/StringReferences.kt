package com.extremex.tablemanager.lib

import android.content.Context

import com.extremex.tablemanager.R

class StringReferences(context: Context) {

    val SHORT_PASSWORD_ERROR: String? = null

    companion object{
        var SHORT_PASSWORD_ERROR: String? = null
    }

    private fun getShortPasswordError(context: Context) {
        var SHORT_PASSWORD_ERROR = context.resources.getString(R.string.short_password_error)
    }

}