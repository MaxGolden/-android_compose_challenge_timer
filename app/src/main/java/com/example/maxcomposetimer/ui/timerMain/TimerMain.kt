package com.example.maxcomposetimer.ui.timerMain

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.maxcomposetimer.R
import com.example.maxcomposetimer.ui.theme.TimerMaxTheme
import dev.chrisbanes.accompanist.insets.statusBarsPadding

@Composable
fun TimerMain(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
    ) {
        TimerAppBar()
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

@Preview
@Composable
fun PreviewTimerAppBar() {
    TimerMaxTheme {
        TimerAppBar()
    }
}