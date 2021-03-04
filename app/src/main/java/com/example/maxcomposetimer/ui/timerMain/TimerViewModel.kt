package com.example.maxcomposetimer.ui.timerMain

import android.os.CountDownTimer
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {

    private val second: Long = 1_000L

    private val _elapsedTime = MutableLiveData<Long>()
    val elapsedTime: LiveData<Long>
        get() = _elapsedTime

    private val _totalTimeInterval = MutableLiveData<Long>()
    val totalTimeInterval: LiveData<Long>
        get() = _totalTimeInterval

    private var _isTimerOn = MutableLiveData<Boolean>(false)
    val isTimerOn: LiveData<Boolean>
        get() = _isTimerOn

    private var _isTimerOnPause = MutableLiveData<Boolean>(false)
    val isTimerOnPause: LiveData<Boolean>
        get() = _isTimerOnPause

    private var _sendTimeUpNotification = MutableLiveData<Boolean>(false)
    val sendTimeUpNotification: LiveData<Boolean>
        get() = _sendTimeUpNotification

    private lateinit var timer: CountDownTimer

    fun setTimerOn(timeInterval: Long, fromResume: Boolean = false) {
        val time = SystemClock.elapsedRealtime() + timeInterval
        if (!fromResume) {
            _totalTimeInterval.postValue(timeInterval)
        }
        _isTimerOn.postValue(true)
        _isTimerOnPause.postValue(false)
        viewModelScope.launch {
            timer = object : CountDownTimer(time, second) {
                override fun onTick(millisUntilFinished: Long) {
                    _elapsedTime.value = time - SystemClock.elapsedRealtime()
                    if (_elapsedTime.value!! <= 0) {
                        _sendTimeUpNotification.postValue(true)
                        resetTimer()
                    }
                }

                override fun onFinish() {
                    resetTimer()
                }
            }
            timer.start()
        }
    }

    fun resetTimer() {
        timer.cancel()
        _elapsedTime.postValue(0L)
        _isTimerOn.postValue(false)
        _totalTimeInterval.postValue(1L)
    }

    fun pauseTimer() {
        _isTimerOnPause.postValue(true)
        timer.cancel()
    }

    fun resumeTimer() {
        _isTimerOnPause.postValue(false)
        setTimerOn(_elapsedTime.value ?: 0L, fromResume = true)
    }

    fun notificationSent() {
        _sendTimeUpNotification.postValue(false)
    }
}