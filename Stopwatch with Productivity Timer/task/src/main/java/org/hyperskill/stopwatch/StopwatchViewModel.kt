package org.hyperskill.stopwatch

import android.os.Looper
import android.widget.ProgressBar
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class StopwatchViewState(
    val elapsed: String = "00:00",
    val progressBarColor: Int = R.color.colorPrimary,
    val progressBarVisiblity: Int = ProgressBar.INVISIBLE
)

class StopwatchViewModel : ViewModel(){
    // TODO without specifying `elapsed` in the constructor call, I get a NullPointerException. Why, as it
    //  has a default value set in its default constructor??
    private val _state = MutableStateFlow(StopwatchViewState(
        "00:00", R.color.colorPrimary, ProgressBar.INVISIBLE
    ))
    val state: StateFlow<StopwatchViewState> = _state.asStateFlow()
    val model = StopwatchModel()

    private val handler = android.os.Handler(Looper.getMainLooper())

    private val stopwatchIncrementer = object : Runnable {
        override fun run() {
            // TODO this is not very elegant. How to do it less repetitive?
            _state.value = StopwatchViewState(
                elapsed(),
                if (model.elapsed().second % 2 == 0L) R.color.colorPrimary else R.color.colorAccent,
                progressBarVisiblity = ProgressBar.VISIBLE)
            handler.postDelayed(this, 1000)
        }
    }

    fun start() {
        if (model.isRunning) {
            return
        }
        model.start()
        _state.value = StopwatchViewState(elapsed(), R.color.colorPrimary, ProgressBar.VISIBLE)
        handler.postDelayed(stopwatchIncrementer, 1000)
    }

    fun reset() {
        model.reset()
        _state.value = StopwatchViewState(elapsed(), R.color.colorPrimary, ProgressBar.INVISIBLE)
        handler.removeCallbacks(stopwatchIncrementer)
    }

    fun elapsed(): String {
        val (min, sec) = model.elapsed()
        return "%02d:%02d".format(min, sec)
    }
}

class StopwatchModel {
    private var startTime: Long = 0L
    private var currentTime: Long = 0L
    var isRunning: Boolean = false

    fun start() {
        startTime = System.currentTimeMillis()
        isRunning = true
    }

    fun reset() {
        startTime = 0L
        currentTime = 0L
        isRunning = false
    }

    fun elapsed(): Pair<Long, Long> {
        val elapsed = if (isRunning) {
            System.currentTimeMillis() - startTime
        } else {
            currentTime - startTime
        }
        val seconds = elapsed / 1000
        val minutes = seconds / 60
        return Pair(minutes, seconds % 60)
    }
}
