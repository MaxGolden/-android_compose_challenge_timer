package com.example.maxcomposetimer.util

import android.text.format.DateUtils

fun setElapsedTime(value: Long?): String {
    return if (value == null) {
        "00:00"
    } else {
        val seconds = value / 1000
        if (seconds < 60) seconds.toString() else DateUtils.formatElapsedTime(seconds)
    }
}