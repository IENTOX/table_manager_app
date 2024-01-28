package com.extremex.tablemanager.lib

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView

class MarqueeTextView(context: Context, attrs: AttributeSet? = null) : AppCompatTextView(context, attrs) {
    private var maxLengthForMarquee = 27 // Set the maximum number of lines for marquee

    init {
        gravity = Gravity.CENTER_VERTICAL
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        // Check if the text exceeds the maximum number of lines for marquee
        if (text != null && text.length > maxLengthForMarquee ) {
            enableMarquee()
        } else {
            disableMarquee()
        }
    }

    private fun enableMarquee() {
        isSingleLine = true
        ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
        marqueeRepeatLimit = -1
        isSelected = true

    }

    private fun disableMarquee() {
        isSingleLine = false
        ellipsize = null
        isSelected = false
    }
}
