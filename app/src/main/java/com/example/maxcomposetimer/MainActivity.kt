package com.example.maxcomposetimer

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
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            TimerApp(onBackPressedDispatcher)
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