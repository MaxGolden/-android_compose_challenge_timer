package com.example.maxcomposetimer.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.maxcomposetimer.MainActivity
import com.example.maxcomposetimer.R
import com.example.maxcomposetimer.receiver.DismissReceiver

private const val NOTIFICATION_ID = 0
const val TIMER_NOTIFICATION_CHANNEL_ID = "TIMER_CHANNEL"
private const val REQUEST_CODE = 0

fun NotificationManager.sendTimerNotification(messageBody: String, context: Context) {

    val contentIntent = Intent(context, MainActivity::class.java)

    val contentPendingIntent = PendingIntent.getActivity(
        context,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val dismissIntent = Intent(context, DismissReceiver::class.java)
    val dismissPendingIntent: PendingIntent = PendingIntent.getBroadcast(
        context,
        REQUEST_CODE,
        dismissIntent,
        PendingIntent.FLAG_CANCEL_CURRENT
    )

    val builder = NotificationCompat.Builder(context, TIMER_NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_timer_black_24dp)
        .setContentTitle(context.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .addAction(
            R.drawable.ic_timer_black_24dp,
            context.getString(R.string.action_dismiss),
            dismissPendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelTimerNotifications() {
    cancelAll()
}
