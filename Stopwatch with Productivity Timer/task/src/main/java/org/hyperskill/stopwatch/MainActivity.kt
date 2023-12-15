package org.hyperskill.stopwatch

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val startButton: Button by lazy {  findViewById(R.id.startButton) }
    private val resetButton: Button by lazy { findViewById(R.id.resetButton) }
    private val textView: TextView by lazy { findViewById(R.id.textView) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val vm: StopwatchViewModel by viewModels()
        startButton.setOnClickListener {
            vm.start()
        }
        resetButton.setOnClickListener {
            vm.reset()
        }

        lifecycleScope.launch {
            vm.state.collect {
                textView.text = it.elapsed
            }
        }
    }
}