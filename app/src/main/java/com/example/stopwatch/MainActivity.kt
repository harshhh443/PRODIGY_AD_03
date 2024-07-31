package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import com.example.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var isRunning = false
    private var startTime = 0
    private var elapsedTime = 0


    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            val currentTime = SystemClock.elapsedRealtime()
            elapsedTime = (currentTime - startTime).toInt()

            val milliseconds = (elapsedTime % 1000)
            val seconds = (elapsedTime / 1000)% 60
            val minutes = (elapsedTime / 1000 / 60) % 60
            val hours = (elapsedTime / 1000 / 60 / 60)

            val time = String.format("%02d:%02d:%02d:%03d", hours, minutes, seconds, milliseconds)
            binding.timerText.text = time

            if (isRunning) {
                handler.postDelayed(this, 10)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startBtn.setOnClickListener {
            startTimer()
        }

        binding.stopBtn.setOnClickListener {
            stopTimer()
        }

        binding.resetBtn.setOnClickListener {
            resetTimer()
        }
    }

    private fun startTimer() {
        if(!isRunning){
            startTime = (SystemClock.elapsedRealtime() - elapsedTime).toInt()
            handler.postDelayed(runnable, 0)
            isRunning = true

            binding.startBtn.isEnabled = false
            binding.stopBtn.isEnabled = true
            binding.resetBtn.isEnabled = true

        }
    }

    private fun stopTimer() {
        if(isRunning){
            handler.removeCallbacks(runnable)
            isRunning = false

            binding.startBtn.isEnabled = true
            binding.startBtn.text = "RESUME"
            binding.stopBtn.isEnabled = false
            binding.resetBtn.isEnabled = true

        }
    }

    private fun resetTimer() {
        stopTimer()

        elapsedTime = 0
        binding.timerText.text = "00:00:00:00"

        binding.startBtn.isEnabled = true
        binding.resetBtn.isEnabled = false
        binding.startBtn.text = "START"

    }
}