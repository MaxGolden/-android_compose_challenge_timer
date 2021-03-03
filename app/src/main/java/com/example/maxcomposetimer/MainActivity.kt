package com.example.maxcomposetimer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.example.adopuppymax.ui.utils.LocalBackDispatcher
import com.example.maxcomposetimer.ui.theme.TimerMaxTheme
import com.example.maxcomposetimer.ui.timerMain.TimerMain
import com.example.maxcomposetimer.util.TIMER_NOTIFICATION_CHANNEL_ID
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        onCreateNotificationChannel(
            TIMER_NOTIFICATION_CHANNEL_ID,
            getString(R.string.notification_channel_name)
        )
        setContent {
            TimerApp(onBackPressedDispatcher)
        }
    }

    private fun onCreateNotificationChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)
            }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_description)

            val notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}

@Composable
fun TimerApp(backDispatcher: OnBackPressedDispatcher) {
    CompositionLocalProvider(LocalBackDispatcher provides backDispatcher) {
        ProvideWindowInsets {
            TimerMaxTheme {
                Scaffold(
                    backgroundColor = MaterialTheme.colors.primarySurface
                ) {
                    val modifier = Modifier.padding(it)
                    TimerMain(modifier)
                }
            }
        }
    }
}