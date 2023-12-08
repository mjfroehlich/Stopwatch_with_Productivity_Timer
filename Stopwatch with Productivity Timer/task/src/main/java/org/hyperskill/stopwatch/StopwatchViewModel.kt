package org.hyperskill.stopwatch

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class StopwatchState(
    val isRunning: Boolean = false,
    val startTime: Long = 0L,
    val currentTime: Long = 0L,
)

class StopwatchViewModel : ViewModel(){
    private val _state = MutableStateFlow(StopwatchState())
    val state: StateFlow<StopwatchState> = _state
}