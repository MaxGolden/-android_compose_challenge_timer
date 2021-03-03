package com.example.maxcomposetimer.util

import android.text.format.DateUtils

fun setElapsedTime(value: Long): String {
    val seconds = value / 1000
    return if (seconds < 60) seconds.toString() else DateUtils.formatElapsedTime(seconds)
}