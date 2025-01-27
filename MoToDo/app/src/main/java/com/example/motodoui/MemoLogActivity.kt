package com.example.motodoui

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.forEach
import androidx.lifecycle.lifecycleScope
import com.example.motodoui.databinding.ActivityMemoLogBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MemoLogActivity : AppCompatActivity(), OnClickListener {

    private var score = 0
    private lateinit var binding: ActivityMemoLogBinding
    private var result: String = ""
    private var userAnswer: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initViews
        binding.apply {
            panel1.setOnClickListener(this@MemoLogActivity)
            panel2.setOnClickListener(this@MemoLogActivity)
            panel3.setOnClickListener(this@MemoLogActivity)
            panel4.setOnClickListener(this@MemoLogActivity)
            startGame()
        }
    }

    private fun startGame() {
        result = ""
        userAnswer = ""
        disableButtons()
        lifecycleScope.launch {
            val round = (3..5).random()
            repeat(round) {
                delay(400)
                val randomPanel = (1..4).random()
                result += randomPanel

                val panel = when (randomPanel) {
                    1 -> binding.panel1
                    2 -> binding.panel2
                    3 -> binding.panel3
                    else -> binding.panel4
                }

                val drawableYellow = ActivityCompat.getDrawable(this@MemoLogActivity, R.drawable.btn_purple)
                val drawableDefault = ActivityCompat.getDrawable(this@MemoLogActivity, R.drawable.btn_state)
                panel.background = drawableYellow
                delay(1000)
                panel.background = drawableDefault
            }
            runOnUiThread {
                enableButtons()
            }
        }
    }

    private fun loseAnimation() {
        binding.apply {
            score = 0
            tvScore.text = "0"
            disableButtons()
            val drawableLose = ActivityCompat.getDrawable(this@MemoLogActivity, R.drawable.btn_lose)
            val drawableDefault = ActivityCompat.getDrawable(this@MemoLogActivity, R.drawable.btn_state)
            lifecycleScope.launch {
                binding.root.forEach { view ->
                    if (view is Button) {
                        view.background = drawableLose
                        delay(200)
                        view.background = drawableDefault
                    }
                }
                delay(1000)
                startGame()
            }
        }
    }

    private fun enableButtons() {
        binding.root.forEach { view ->
            if (view is Button) {
                view.isEnabled = true
            }
        }
    }

    private fun disableButtons() {
        binding.root.forEach { view ->
            if (view is Button) {
                view.isEnabled = false
            }
        }
    }

    override fun onClick(view: View?) {
        view?.let {
            userAnswer += when (it.id) {
                R.id.panel1 -> "1"
                R.id.panel2 -> "2"
                R.id.panel3 -> "3"
                R.id.panel4 -> "4"
                else -> ""
            }

            if (userAnswer == result) {
                Toast.makeText(this@MemoLogActivity, "W I N :)", Toast.LENGTH_SHORT).show()
                score++
                binding.tvScore.text = score.toString()
                startGame()
            } else if (userAnswer.length >= result.length) {
                loseAnimation()
            }
        }
    }
}
