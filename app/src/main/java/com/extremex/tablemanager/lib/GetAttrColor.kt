package com.extremex.tablemanager.lib

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import androidx.core.content.res.ResourcesCompat
import com.extremex.tablemanager.R

class GetAttrColor(private val context: Context) {

    fun getBackgroundColor(): ColorDrawable {
        return getColorFromThemeAttribute(android.R.attr.colorBackground)
    }
    fun getTextInverseColor(): ColorDrawable {
        return getColorFromThemeAttribute(R.attr.colorTextInverse)
    }
    fun getTextColor(): ColorDrawable {
        return getColorFromThemeAttribute(android.R.attr.textColor)
    }

    private fun getColorFromThemeAttribute(attributeId: Int): ColorDrawable {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attributeId, typedValue, true)
        return ColorDrawable(typedValue.data)
    }
}