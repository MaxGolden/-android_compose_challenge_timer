package com.example.maxcomposetimer.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.maxcomposetimer.R
import com.example.maxcomposetimer.util.sendTimerNotification

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendTimerNotification(
            context.getText(R.string.time_up_msg).toString(),
            context
        )
    }
}