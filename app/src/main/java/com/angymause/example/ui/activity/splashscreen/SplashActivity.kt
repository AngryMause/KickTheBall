package com.angymause.example.ui.activity.splashscreen

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.angymause.example.databinding.ActivitySplashBinding
import com.angymause.example.ui.activity.gamescreen.GameActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!
    private var animation: ObjectAnimator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(_binding?.root)
        hideSystemBars()
        animateImage()
        setProgress()

    }

    private fun hideSystemBars() {
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    private fun animateImage() {
        animation = ObjectAnimator.ofFloat(binding.ivSplashItem, View.ROTATION_Y, 1.50f, 360f)
        animation?.duration = 1500
        animation?.repeatCount = -1
        animation?.interpolator = AccelerateDecelerateInterpolator()
        animation?.start()
    }

    private fun setProgress() {
        var progressCount = 0
        var isCanOpenGame = false
        lifecycleScope.launch(Dispatchers.IO) {
            while (!isCanOpenGame) {
                delay(30)
                withContext(Dispatchers.Main) {
                    if (progressCount != 100) {
                        progressCount++
                        binding.pbDownload.progress = progressCount
                    } else {
                        isCanOpenGame = true
                        replaceAndFinishActivity()
                    }
                }
            }
        }
    }

    private fun replaceAndFinishActivity() {
        val splashActivity = Intent(this@SplashActivity, GameActivity::class.java)
        startActivity(splashActivity)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        animation?.cancel()
    }

}

