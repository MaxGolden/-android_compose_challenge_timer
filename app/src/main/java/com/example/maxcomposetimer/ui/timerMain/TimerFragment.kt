package com.example.maxcomposetimer.ui.timerMain

import android.app.NotificationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.maxcomposetimer.R
import com.example.maxcomposetimer.ui.theme.TimerMaxTheme
import com.example.maxcomposetimer.util.LocalBackPressedDispatcher
import com.example.maxcomposetimer.util.sendTimerNotification
import com.example.maxcomposetimer.util.setElapsedTime
import dev.chrisbanes.accompanist.insets.ExperimentalAnimatedInsets
import dev.chrisbanes.accompanist.insets.LocalWindowInsets
import dev.chrisbanes.accompanist.insets.ViewWindowInsetObserver
import dev.chrisbanes.accompanist.insets.statusBarsPadding

class TimerFragment : Fragment() {

    private val viewModel: TimerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.sendTimeUpNotification.observe(viewLifecycleOwner, { notification ->
            if (notification == true) {
                viewModel.notificationSent()
                sendNotificationTimer()
            }
        })
    }

    @ExperimentalAnimationApi
    @OptIn(ExperimentalAnimatedInsets::class) // Opt-in to experiment animated insets support
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(inflater.context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        val windowInsets = ViewWindowInsetObserver(this)
            .start(windowInsetsAnimationsEnabled = true)
        setContent {
            val elapsedTime by viewModel.elapsedTime.observeAsState()
            val totalTime by viewModel.totalTimeInterval.observeAsState()
            val timerOn by viewModel.isTimerOn.observeAsState()
            val timerPause by viewModel.isTimerOnPause.observeAsState()
            val animatedTimerProcess by animateFloatAsState(
                targetValue = (elapsedTime ?: 1f).toFloat() / (totalTime ?: 1f).toFloat(),
                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
            )
            CompositionLocalProvider(
                LocalBackPressedDispatcher provides requireActivity().onBackPressedDispatcher,
                LocalWindowInsets provides windowInsets,
            ) {
                TimerMaxTheme {
                    Scaffold(
                        backgroundColor = MaterialTheme.colors.primarySurface
                    ) {
                        val modifier = Modifier.padding(it)
                        Column(
                            modifier = modifier
                                .animateContentSize()
                                .statusBarsPadding()
                        ) {
                            TimerAppBar()
                            CircularProgressIndicator(
                                progress = if (timerOn == true) animatedTimerProcess else 1f,
                                color = Color.White,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .padding(18.dp),
                                strokeWidth = 12.dp
                            )
                            TimerText(elapsedTime, timerOn)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 160.dp)
                            ) {

                                AnimatedVisibility(visible = timerOn == false) {
                                    Button(onClick = { startTimer(10000L) }) {
                                        Text(
                                            modifier = Modifier.padding(8.dp),
                                            style = MaterialTheme.typography.h6,
                                            text = stringResource(id = R.string.start)
                                        )
                                    }
                                }

                                AnimatedVisibility(visible = timerOn == true) {
                                    Row(
                                        modifier = Modifier
                                            .padding(16.dp)
                                    ) {
                                        Button(
                                            onClick = { if (timerPause == false) pauseAlarm() else viewModel.resumeTimer() },
                                            modifier = Modifier.padding(end = 16.dp)
                                        ) {
                                            Text(
                                                modifier = Modifier.padding(8.dp),
                                                style = MaterialTheme.typography.h6,
                                                text = if (timerPause == false) {
                                                    stringResource(id = R.string.pause)
                                                } else {
                                                    stringResource(id = R.string.resume)
                                                }
                                            )
                                        }
                                        Button(onClick = {
                                            cancelAlarm()
                                        }) {
                                            Text(
                                                modifier = Modifier.padding(8.dp),
                                                style = MaterialTheme.typography.h6,
                                                text = stringResource(id = R.string.cancel)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun pauseAlarm() {
        viewModel.pauseTimer()
    }

    private fun cancelAlarm() {
        viewModel.resetTimer()
    }

    private fun startTimer(timeInterval: Long) {
        viewModel.setTimerOn(timeInterval)
    }

    private fun sendNotificationTimer() {
        val notificationManager =
            ContextCompat.getSystemService(
                requireContext(),
                NotificationManager::class.java
            ) as NotificationManager
        notificationManager.sendTimerNotification(
            getString(R.string.time_up_msg),
            requireContext()
        )
    }
}

@Composable
fun TimerText(
    timerCount: Long?,
    timerOn: Boolean?
) {
    if (timerOn == true) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.h2,
            textAlign = TextAlign.Center,
            text = setElapsedTime(timerCount)
        )
    } else {
        Text(
            modifier = Modifier
                .padding(48.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center,
            text = "10 Seconds Timer"
        )
    }
}

@Composable
fun TimerAppBar() {
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier.height(48.dp)
    ) {
        Image(
            modifier = Modifier
                .padding(start = 16.dp, bottom = 4.dp)
                .align(Alignment.CenterVertically),
            painter = painterResource(id = R.drawable.ic_timer_white_24dp),
            contentDescription = null
        )
        IconButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = { }
        ) {
            Icon(
                imageVector = Icons.Filled.MoreHoriz,
                contentDescription = stringResource(R.string.cd_more)
            )
        }
    }
}