package com.example.test1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var gameScoreText: TextView
    private lateinit var gameTimerText: TextView
    private lateinit var btnTap: Button
    private var score: Int = 0
    private var isGameStart: Boolean = false
    private lateinit var countDownTimer: CountDownTimer
    private var initialCountDown: Long = 60000
    private var countDownInterval: Long = 1000
    private var timeLeft: Int = 60

    companion object {
        private const val SCORE_KEY="SCORE_KEY"
        private const val TIME_LEFT_KEY="TIME_LEFT_KEY"
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, score)
        outState.putInt(TIME_LEFT_KEY, timeLeft)
        countDownTimer.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameScoreText = findViewById(R.id.gameScore)
        gameTimerText = findViewById(R.id.gameTimer)
        btnTap = findViewById(R.id.btnTap)

        btnTap.setOnClickListener { v ->
            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce);
            v.startAnimation(bounceAnimation)
            incrementScore()
        }

        if (savedInstanceState != null) {
            score=savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getInt(TIME_LEFT_KEY)

            restoreGame()
        } else {

            resetGame()
        }

    }

    private fun restoreGame() {
        val restoredScore=getString(R.string.text_game_score, score)
        gameScoreText.text = restoredScore
        val restoredTime = getString(R.string.text_game_timer, timeLeft)
        gameTimerText.text = restoredTime
        countDownTimer=object : CountDownTimer((timeLeft * 1000).toLong(), countDownInterval){
            override fun onTick(millisUntilFinishe: Long) {
                timeLeft = millisUntilFinishe.toInt()/1000
                val timeLeftString = getString(R.string.text_game_timer, timeLeft)
                gameTimerText.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }
        countDownTimer.start()
        isGameStart = true
    }

    private fun incrementScore() {
        score++
        gameScoreText.text = getString(R.string.text_game_score, score)
        if (!isGameStart) {
            startGame()
        }
    }

    private fun resetGame() {
    score = 0
        val initialScore = getString(R.string.text_game_score, score)
        gameScoreText.text = initialScore

        val initialTimeLeft = getString(R.string.text_game_timer, timeLeft)
        gameTimerText.text = initialTimeLeft

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinishe: Long) {
                timeLeft = millisUntilFinishe.toInt()/1000
                val timeLeftString = getString(R.string.text_game_timer, timeLeft)
                gameTimerText.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }
        isGameStart = false
    }

    private fun startGame() {
        countDownTimer.start()
        isGameStart = true
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.text_message, score), Toast.LENGTH_LONG).show()
        resetGame()
    }
}