package com.example.cronometer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _timeRemaining = MutableStateFlow(0)
    val timeRemaining: StateFlow<Int> = _timeRemaining

    private var countdownJob: Job? = null

    fun startCountdown(totalSeconds: Int) {
        countdownJob?.cancel() // Cancel previous countdown job, if any

        countdownJob = viewModelScope.launch {
            _timeRemaining.value = totalSeconds
            while (_timeRemaining.value > 0) {
                delay(1000L)
                _timeRemaining.value--
            }
        }
    }
}