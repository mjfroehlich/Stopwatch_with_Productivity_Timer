package org.hyperskill.stopwatch

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val startButton: Button by lazy {  findViewById(R.id.startButton) }
    private val resetButton: Button by lazy { findViewById(R.id.resetButton) }
    private val textView: TextView by lazy { findViewById(R.id.textView) }
    private val progressBar: ProgressBar by lazy { findViewById(R.id.progressBar) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar.visibility = ProgressBar.INVISIBLE

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
                progressBar.visibility = it.progressBarVisiblity
                progressBar.indeterminateDrawable.colorFilter = PorterDuffColorFilter(
                    resources.getColor(it.progressBarColor), PorterDuff.Mode.SRC_IN)
                // HACK: this is a workaround for API < 21, which the tests use
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    progressBar.indeterminateTintList = resources.getColorStateList(it.progressBarColor)
                }
            }
        }
    }
}