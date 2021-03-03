package com.example.adopuppymax.ui.utils

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.*

internal val LocalBackDispatcher = staticCompositionLocalOf<OnBackPressedDispatcher> {
    error("No Back Dispatcher provided")
}
