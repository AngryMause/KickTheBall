package com.angymause.example.ui.fragment.gamescreen

import android.animation.Animator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.angymause.example.R
import com.angymause.example.databinding.FragmentGameBinding
import com.angymause.example.toast
import com.angymause.example.ui.fragment.BaseFragment
import com.angymause.example.ui.fragment.menuscreen.MenuFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class GameFragment : BaseFragment<FragmentGameBinding>(FragmentGameBinding::inflate) {

    private var startBallAnimation: ViewPropertyAnimator? = null
    private var kickBallAnimation: ViewPropertyAnimator? = null
    private var startGameTimer: CountDownTimer? = null
    private var startBallTransY = 0f
    private var screenHeight = 0f
    private var screenWidth = 0f
    private var ballWidth = 0f
    private var countHeart = 3
    private var difficult = 0L
    private var score = 0
    private val gameViewModel: GameViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        loadSelectBal()
        startGame()


        gameViewModel.score.observe(viewLifecycleOwner) {

        }
    }

    private fun loadSelectBal() {
        gameViewModel.viewModelScope.launch(Dispatchers.IO) {
            val bal = gameViewModel.loadSelectBal()
            binding.ivBall.setImageResource(bal)
        }
    }

    private fun startGame() {
        binding.tvTimer.isVisible = true
        binding.alertContainer.isVisible = false
        startGameTimer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvTimer.text = (millisUntilFinished / 1000).toString()
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                dropDownBall()
                binding.tvTimer.isVisible = false
            }
        }
        startGameTimer?.start()
    }


    private fun initialize() {
        binding.gameContainer.post {
            binding.gameContainer.translationY = startBallTransY
            screenHeight = binding.gameContainer.height.toFloat()
            screenWidth = binding.gameContainer.width.toFloat()
            ballWidth = binding.ivBall.width.toFloat()
            startBallTransY = -binding.ivBall.height.toFloat()
        }
    }

    private fun dropDownBall() {
        var isStart = true
        var isBalKicked = true
        startBallAnimation =
            binding.ivBall.animate().translationY(screenHeight).translationX(randomFlightPath())
                .setDuration(difficult()).setInterpolator(LinearInterpolator())
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {

                    }

                    override fun onAnimationEnd(animation: Animator) {
                        if (isStart) {
                            binding.ivBall.translationY = startBallTransY
                            dropDownBall()
                            ballIsOutOfBound()
                        }
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        isStart = false
                    }

                    override fun onAnimationRepeat(animation: Animator) {

                    }
                })
        startBallAnimation!!.start()
        binding.ivBall.setOnClickListener {
            if (isBalKicked) {
                startBallAnimation?.cancel()
                kickBall()
                saveScoreToShared()
                isBalKicked = false
            }
        }
    }

    private fun difficult(): Long {
        when (score) {
            0 -> {
                difficult = 2520
            }
            20 -> {
                difficult = 2000L
                requireActivity().toast("Speed Up")
            }
            50 -> {
                difficult = 1500L
                requireActivity().toast("Speed Up")
            }
            100 -> {
                difficult = 1000L
                requireActivity().toast("Speed Up")
            }
        }
        return difficult
    }

    private fun kickBall() {
        kickBallAnimation =
            binding.ivBall.animate().translationY(startBallTransY).translationX(randomFlightPath())
                .setInterpolator(AccelerateInterpolator(1f)).setDuration(1000L)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {

                    }

                    @SuppressLint("SetTextI18n")
                    override fun onAnimationEnd(animation: Animator) {
                        dropDownBall()

                    }

                    override fun onAnimationCancel(animation: Animator) {

                    }

                    override fun onAnimationRepeat(animation: Animator) {

                    }
                })
        kickBallAnimation!!.start()
    }

    private fun ballIsOutOfBound() {
        when (countHeart) {
            3 -> {
                binding.ivHeartOne.setImageResource(R.drawable.il_heart)
                countHeart--
            }
            2 -> {
                binding.ivHeartTwo.setImageResource(R.drawable.il_heart)
                countHeart--
            }
            1 -> {
                binding.ivHeartThree.setImageResource(R.drawable.il_heart)
                startBallAnimation?.cancel()
                endGameAlert()
                countHeart = 3
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun endGameAlert() {
        binding.alertContainer.isVisible = true
        lifecycleScope.launch {
            gameViewModel.score.observe(viewLifecycleOwner) {
                binding.tvAlertScore.text = "Total score: $it"
            }
        }
        binding.tvAlertRestartGame.setOnClickListener {
            binding.tvScore.text = "Score: 0"
            score = 0
            updateHeart()
            startGame()
        }
        binding.tvAlertExit.setOnClickListener {
            replace(MenuFragment())
        }
    }

    private fun updateHeart() {
        binding.ivHeartOne.isVisible = true
        binding.ivHeartThree.isVisible = true
        binding.ivHeartTwo.isVisible = true
        binding.ivHeartThree.setImageResource(R.drawable.il_black_heart)
        binding.ivHeartTwo.setImageResource(R.drawable.il_black_heart)
        binding.ivHeartOne.setImageResource(R.drawable.il_black_heart)
    }

    private fun saveScoreToShared() {
        score++
        gameViewModel.writeScore(gameViewModel.score.value!! + 1)
        binding.tvScore.text = "Score: $score"
    }

    private fun randomFlightPath(): Float {
        val offset = (screenWidth / 2 - ballWidth / 2).toInt()
        return Random.nextInt(-offset, offset).toFloat()
    }

    override fun onDestroyView() {
        kickBallAnimation?.cancel()
        startBallAnimation?.cancel()
        startGameTimer?.cancel()
        super.onDestroyView()
    }
}

